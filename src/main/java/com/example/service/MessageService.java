package com.example.service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message createMessage(Message message) {
        if (message.getMessageText() == null || message.getMessageText().isBlank()
                || message.getMessageText().length() > 255) {
            return null;
        }

        return messageRepository.save(message);
    }

    public Message getMessageById(Integer id) {
        return messageRepository.findById(id).orElse(null);
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public List<Message> getMessagesByUserId(Integer userId) {
        return messageRepository.findByPostedBy(userId);
    }

    public int deleteMessage(Integer id) {
        if (messageRepository.existsById(id)) {
            messageRepository.deleteById(id);
            return 1; // One row deleted
        }
        return 0; // Message not found
    }

    public int updateMessage(Integer id, String messageText) {
        if (messageText == null || messageText.isBlank() || messageText.length() > 255) {
            return 0;
        }
        Optional<Message> optionalMessage = messageRepository.findById(id);
        if (optionalMessage.isPresent()) {
            Message message = optionalMessage.get();
            message.setMessageText(messageText);
            messageRepository.save(message);
            return 1;
        }
        return 0; // Message not found
    }

}
