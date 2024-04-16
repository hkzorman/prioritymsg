package com.github.hkzorman.prioritymsg.controllers;

import com.github.hkzorman.prioritymsg.models.Message;
import com.github.hkzorman.prioritymsg.services.SpeechSynthesizerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;

import java.nio.file.Path;
import java.time.LocalTime;
import java.util.concurrent.ExecutionException;

@Controller
public class MessageController {

    private Logger logger = LoggerFactory.getLogger(MessageController.class);
    private SpeechSynthesizerService synthesizerService;

    public MessageController(SpeechSynthesizerService synthesizerService) {
        this.synthesizerService = synthesizerService;
    }

    @MessageMapping("/send")
    @SendTo("/topic/messages")
    public Message normalMessage(MessageHeaders headers, Message msg) {
        var user = getUserNameFromHeaders(headers);
        logger.info("Received message from user: " + user);

        msg.setPriority(msg.isPriority());
        msg.setUsername(user);
        msg.setTime(LocalTime.now());

        return msg;
    }

    /**
     * This method will use Azure TTS service to generate an audio file that will be stored
     * locally for this message. Then, it will augment the existing message object with the
     * additional audio file information.
     * @param msg
     * @return
     * @throws InterruptedException
     * @throws ExecutionException
     */
    @MessageMapping("/readAloud")
    @SendTo("/topic/messages/tts")
    public Message playback(MessageHeaders headers, Message msg) throws InterruptedException, ExecutionException {
        var user = getUserNameFromHeaders(headers);
        logger.info("Received TTS message from user: " + user);

        var synthesiserFuture = this.synthesizerService.synthesizeSpeechAsync(msg.getMessage());
        var audioInfo = synthesiserFuture.get();
        msg.setUsername(user);
        msg.setAudioInfo(audioInfo);

        // Remove "/static" from URL
        audioInfo.setAudioFilePath(audioInfo.getAudioFilePath().replace("/static", ""));

        return msg;
    }

    private static String getUserNameFromHeaders(MessageHeaders headers) {
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken)headers.get("simpUser");
        assert authToken != null;
        User user = (User)authToken.getPrincipal();
        return user.getUsername();
    }
}
