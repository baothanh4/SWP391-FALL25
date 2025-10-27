package com.example.SWP391_FALL25.Controller;


import com.example.SWP391_FALL25.Entity.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    private final SimpMessagingTemplate template;

    public ChatController(SimpMessagingTemplate template) {
        this.template = template;
    }

    @MessageMapping("/chat.send")
    public void sendMessage(ChatMessage chatMessage){
        String destination="/queue/messages/"+chatMessage.getReceiver();
        template.convertAndSend(destination, chatMessage);
    }

    @MessageMapping("/chat.join")
    public ChatMessage join(ChatMessage chatMessage){
        chatMessage.setContent(chatMessage.getSender()+"Join the chat");
        return chatMessage;
    }
}
