package com.example.gread.app.home.service;

import com.example.gread.app.home.dto.HomeResponseDto;
import com.example.gread.app.home.dto.ReviewResponseDto;
import com.example.gread.app.login.domain.User;
import com.example.gread.app.login.repository.UserRepository;
import com.example.gread.app.review.domain.Review;
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
                .reviews(convertToDtoList(reviews))
                .build();
    }

    private HomeResponseDto getGuestHomeData() {
        List<Review> reviews = reviewRepository.findRandom5(PageRequest.of(0, 5));
        return HomeResponseDto.builder()
                .nickname("방문자")
                .readerType(null)
                .reviews(convertToDtoList(reviews))
                .build();
    }

    private List<ReviewResponseDto> convertToDtoList(List<Review> reviews) {
        return reviews.stream()
                .map(r -> ReviewResponseDto.builder()
                        .reviewId(r.getReviewId())
                        .category(r.getCategory())
                        .content(r.getReviewContent())
                        .authorNickname(r.getProfile() != null ? r.getProfile().getNickname() : "독자")
                        .createdAt(r.getCreatedAt() != null ? r.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : "")
                        .bookTitle(r.getBook() != null ? r.getBook().getTitle() : "제목 없음")
                        .build())
                .collect(Collectors.toList());
    }
}