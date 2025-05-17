package com.example.TalkToDo.model;

import lombok.Data;
import java.util.List;

@Data
public class WhisperResponse {
    private String text;
    private List<WhisperSegment> segments;
} 