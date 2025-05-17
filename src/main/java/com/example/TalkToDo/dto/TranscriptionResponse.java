package com.example.TalkToDo.dto;

import com.example.TalkToDo.model.WhisperSegment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TranscriptionResponse {
    private String fullText;
    private List<WhisperSegment> segments;
} 