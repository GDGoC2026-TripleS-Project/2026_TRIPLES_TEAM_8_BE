package com.example.gread.app.upload;

public record IngestResponse(
        int requestedSeeds,
        int fromPage,
        int toPage,
        int pageSize,
        int saved
) {}
