package com.example.TalkToDo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WhisperSegment {
    private String text;
    private double start;
    private double end;
} 