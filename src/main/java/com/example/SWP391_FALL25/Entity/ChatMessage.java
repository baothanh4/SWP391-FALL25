package com.example.SWP391_FALL25.Entity;


import com.example.SWP391_FALL25.Enum.MessageType;
import lombok.Data;

@Data
public class ChatMessage {
    private String sender;
    private String receiver;
    private String content;
    private MessageType type;
}
