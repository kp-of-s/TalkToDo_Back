package com.example.TalkToDo.controller;

import com.example.TalkToDo.dto.TranscriptionResponse;
import com.example.TalkToDo.service.AudioProcessingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/audio")
@RequiredArgsConstructor
public class AudioController {

    private final AudioProcessingService audioProcessingService;

    @PostMapping("/process")
    public ResponseEntity<TranscriptionResponse> processAudio(@RequestParam("file") MultipartFile file) {
        try {
            Path tempFile = Files.createTempFile("audio_", ".mp3");
            file.transferTo(tempFile.toFile());
            
            TranscriptionResponse response = audioProcessingService.transcribeAudio(tempFile.toFile());
            Files.deleteIfExists(tempFile);
            
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            System.err.println("Error processing audio file: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
} 