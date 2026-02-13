package com.example.gread.app.upload;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/nlc")
public class NlcIngestController {

    private final NlcIngestService nlcIngestService;

    @PostMapping("/ingest/all")
    public ResponseEntity<IngestResponse> ingestAll(@Valid @RequestBody IngestRequest req) throws Exception {
        int saved = nlcIngestService.ingestAll(req);

        int seedCount = (req.getSeeds() == null || req.getSeeds().isEmpty())
                ? nlcIngestService.defaultSeeds().size()
                : req.getSeeds().size();

        return ResponseEntity.ok(new IngestResponse(
                seedCount,
                req.getFromPage(),
                req.getToPage(),
                req.getPageSize(),
                saved
        ));
    }
}
