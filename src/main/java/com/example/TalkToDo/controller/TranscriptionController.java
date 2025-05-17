package com.example.TalkToDo.controller;

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
@RequestMapping("/api/transcribe")
@RequiredArgsConstructor
public class TranscriptionController {

    private final AudioProcessingService audioProcessingService;

    @PostMapping("/meeting")
    public ResponseEntity<String> transcribeMeeting(@RequestParam("file") MultipartFile file) {
        try {
            Path tempFile = Files.createTempFile("meeting_", ".mp3");
            file.transferTo(tempFile.toFile());
            
            String transcription = audioProcessingService.transcribeAudio(tempFile.toFile()).toString();
            Files.deleteIfExists(tempFile);
            return ResponseEntity.ok(transcription);
            
        } catch (IOException e) {
            System.err.println("Error transcribing meeting audio: " + e.getMessage());
            return ResponseEntity.internalServerError().body("Failed to transcribe meeting audio");
        }
    }
} 