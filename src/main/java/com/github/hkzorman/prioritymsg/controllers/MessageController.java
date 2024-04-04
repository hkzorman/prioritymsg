package com.github.hkzorman.prioritymsg.controllers;

import com.github.hkzorman.prioritymsg.models.Message;
import org.springframework.http.HttpHeaders;
import org.springframework.http.RequestEntity;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestHeader;

import java.time.LocalTime;

@Controller
public class MessageController {
    @MessageMapping("/messages")
    @SendTo("/topic/messages")
    public Message normalMessage(MessageHeaders headers, Message msg) {
        msg.setPriority(msg.isPriority());
        msg.setUsername(getUserNameFromHeaders(headers));
        msg.setTime(LocalTime.now());
        return msg;
    }

    private static String getUserNameFromHeaders(MessageHeaders headers) {
        UsernamePasswordAuthenticationToken authToken = (UsernamePasswordAuthenticationToken)headers.get("simpUser");
        assert authToken != null;
        User user = (User)authToken.getPrincipal();
        return user.getUsername();
    }
}
