package com.example.TalkToDo.service;

import com.example.TalkToDo.dto.TranscriptionResponse;
import com.example.TalkToDo.model.WhisperResponse;
import com.example.TalkToDo.model.WhisperSegment;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.File;
import java.util.List;

@Service
public class AudioProcessingService {

    @Value("${openai.api.key}")
    private String openAiApiKey;

    private final RestTemplate restTemplate;
    private static final String WHISPER_API_URL = "https://api.openai.com/v1/audio/transcriptions";

    public AudioProcessingService() {
        this.restTemplate = new RestTemplate();
    }

    public TranscriptionResponse transcribeAudio(File audioFile) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.setBearerAuth(openAiApiKey);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("file", new FileSystemResource(audioFile));
            body.add("model", "whisper-1");
            body.add("language", "ko");
            body.add("response_format", "verbose_json");
            body.add("timestamp_granularities", "segment");

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

            ResponseEntity<WhisperResponse> response = restTemplate.exchange(
                WHISPER_API_URL,
                HttpMethod.POST,
                requestEntity,
                WhisperResponse.class
            );

            if (response.getBody() != null) {
                // System.out.println("Whisper API 응답: " + response.getBody());
                WhisperResponse whisperResponse = response.getBody();
                // System.out.println("변환된 WhisperResponse: " + whisperResponse);
                return new TranscriptionResponse(
                    whisperResponse.getText(),
                    whisperResponse.getSegments()
                );
            }
            
            throw new RuntimeException("No response from Whisper API");
        } catch (Exception e) {
            System.err.println("음성 변환 중 오류가 발생했습니다: " + e.getMessage());
            throw new RuntimeException("음성 변환 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    public String analyzeTranscription(String transcription) {
        try {
            ChatLanguageModel model = OpenAiChatModel.builder()
                    .apiKey(openAiApiKey)
                    .build();

            String prompt = String.format("""
                다음 회의 내용을 분석하고 주요 포인트를 요약해주세요:
                """, transcription);

            return model.generate(prompt);
        } catch (Exception e) {
            System.err.println("텍스트 분석 중 오류가 발생했습니다: " + e.getMessage());
            throw new RuntimeException("텍스트 분석 중 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }
} 