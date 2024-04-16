package com.github.hkzorman.prioritymsg.services;

import com.github.hkzorman.prioritymsg.models.Message;
import com.microsoft.cognitiveservices.speech.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
public class SpeechSynthesizerService {
    private Logger logger = LoggerFactory.getLogger(SpeechSynthesizerService.class);

    private final String speechKey;
    private final String speechRegion;
    private final String basePath;
    private SpeechSynthesizer speechSynthesizer;

    public SpeechSynthesizerService() {
        this.speechKey = System.getenv("SPEECH_KEY");
        this.speechRegion = System.getenv("SPEECH_REGION");
        this.basePath = System.getenv().getOrDefault("BASE_PATH", Path.of(System.getProperty("user.dir"), "audio").toString());

        SpeechConfig speechConfig = SpeechConfig.fromSubscription(speechKey, speechRegion);
        speechConfig.setSpeechSynthesisVoiceName("en-US-AvaMultilingualNeural");

        try {
            this.speechSynthesizer = new SpeechSynthesizer(speechConfig);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CompletableFuture<Message.AudioInfo> synthesizeSpeechAsync(String text) {
        if (text.isEmpty()) return CompletableFuture.completedFuture(new Message.AudioInfo(false));
        var messageBuilder = new StringBuilder();

        return CompletableFuture.supplyAsync(() -> {
            try {
                return new Message.AudioInfo(true, "/static/audio/example.wav", 100);
//                SpeechSynthesisResult speechSynthesisResult = speechSynthesizer.SpeakTextAsync(text).get();
//
//                if (speechSynthesisResult.getReason() == ResultReason.SynthesizingAudioCompleted) {
//                    logger.info("Audio data generated successfully - saving audio file...");
//
//                    var fileName = UUID.randomUUID();
//                    var path = Path.of(basePath, fileName + ".wav");
//                    Files.write(path, speechSynthesisResult.getAudioData());
//                    logger.info("Successfully wrote file '" + path + "'");
//
//                    return new Message.AudioInfo(true, path.toString(), speechSynthesisResult.getAudioDuration());
//                }
//                else if (speechSynthesisResult.getReason() == ResultReason.Canceled) {
//                    SpeechSynthesisCancellationDetails cancellation = SpeechSynthesisCancellationDetails.fromResult(speechSynthesisResult);
//                    messageBuilder.append("CANCELED: Reason=").append(cancellation.getReason())
//                            .append(System.lineSeparator());
//
//                    if (cancellation.getReason() == CancellationReason.Error) {
//                        messageBuilder.append("CANCELED: ErrorCode=").append(cancellation.getErrorCode())
//                                .append(System.lineSeparator())
//                                .append("CANCELED: ErrorDetails=").append(cancellation.getErrorDetails())
//                                .append(System.lineSeparator())
//                                .append("CANCELED: Did you set the speech resource key and region values?");
//                    }
//                }
            }
            catch (Exception e) {
                logger.error("Error while using the speech synthesis service");
                e.printStackTrace();
            }

            return new Message.AudioInfo(false);
        });
    }
}
