package com.example.gread.app.home.service;

import com.example.gread.app.home.dto.HomeResponseDto;
import com.example.gread.app.home.dto.ReviewResponseDto;
import com.example.gread.app.login.domain.User;
import com.example.gread.app.login.repository.UserRepository;
import com.example.gread.app.review.domain.Review;
import com.example.gread.app.review.dto.ReviewResDto;
import com.example.gread.app.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HomeService {

    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public HomeResponseDto getHomeData(String subject) {
        if (subject == null || "anonymousUser".equals(subject)) {
            return getGuestHomeData();
        }

        try {
            Long userId = Long.parseLong(subject);
            return userRepository.findById(userId)
                    .map(this::getMemberHomeData)
                    .orElseGet(this::getGuestHomeData);
        } catch (NumberFormatException e) {
            return userRepository.findByEmail(subject)
                    .map(this::getMemberHomeData)
                    .orElseGet(this::getGuestHomeData);
        }
    }

    private HomeResponseDto getMemberHomeData(User user) {
        if (user.getReaderType() == null) return getGuestHomeData();

        List<String> categories = user.getReaderType().getCategoryCodes().stream()
                .map(String::valueOf)
                .collect(Collectors.toList());

        List<Review> reviews = reviewRepository.findRandomByCategoryCodes(categories, PageRequest.of(0, 5));

        return HomeResponseDto.builder()
                .nickname(user.getProfile() != null ? user.getProfile().getNickname() : user.getName())
                .readerType(user.getReaderType().name())
                .readerTitle(user.getReaderType().getTitle())
                .reviews(convertToDtoList(reviews))
                .build();
    }

    private HomeResponseDto getGuestHomeData() {
        List<Review> reviews = reviewRepository.findRandom5(PageRequest.of(0, 5));
        return HomeResponseDto.builder()
                .nickname("방문자")
                .readerType(null)
                .readerTitle(null)
                .reviews(convertToDtoList(reviews))
                .build();
    }

    private List<ReviewResponseDto> convertToDtoList(List<Review> reviews) {
        return reviews.stream()
                .map(r -> {
                    com.example.gread.app.review.dto.ReviewResDto sharedDto = com.example.gread.app.review.dto.ReviewResDto.from(r);

                    return ReviewResponseDto.builder()
                            .reviewId(r.getReviewId())
                            .profileId(r.getProfile() != null ? r.getProfile().getId() : null)
                            .bookId(r.getBook() != null ? r.getBook().getId() : null)
                            .nickname(r.getProfile() != null ? r.getProfile().getNickname() : "독자")
                            .reviewColor(r.getReviewColor() != null ? r.getReviewColor().name() : "GRAY")
                            .reviewContent(r.getReviewContent())
                            .category(r.getMinorCode())
                            .createdAt(r.getCreatedAt() != null ? r.getCreatedAt().toString() : "")
                            .updatedAt(r.getUpdatedAt() != null ? r.getUpdatedAt().toString() : "")
                            .createdTimeAgo(sharedDto.getCreatedTimeAgo())
                            .build();
                })
                .collect(Collectors.toList());
    }
}