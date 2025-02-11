package com.app.Controller;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class NotificationController {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Method to send notification when a candidate result is consumed
    public void sendNotification(String message) {
        System.out.println("notification");
        messagingTemplate.convertAndSend("/topic/candidateResults", message); // Send notification
    }
}