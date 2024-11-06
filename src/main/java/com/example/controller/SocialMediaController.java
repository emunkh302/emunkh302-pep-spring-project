package com.example.controller;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use
 * the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations.
 * You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */

@RestController
public class SocialMediaController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private MessageService messageService;

    // ==============================
    // User Registration Endpoint
    // ==============================

    @PostMapping("/register")
    public ResponseEntity<Account> register(@RequestBody Account account) {
        // Validate username and password
        if (account.getUsername() == null || account.getUsername().isBlank()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (account.getPassword() == null || account.getPassword().length() <= 4) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        // Check if username already exists
        if (accountService.getAccountByUsername(account.getUsername()) != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT); // 409 Conflict
        }
        Account newAccount = accountService.createAccount(account);
        return new ResponseEntity<>(newAccount, HttpStatus.OK);
    }

    // ==============================
    // User Login Endpoint
    // ==============================

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody Account account) {
        Account existingAccount = accountService.login(account);
        if (existingAccount != null) {
            return new ResponseEntity<>(existingAccount, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    // Create Message Endpoint
    @PostMapping("/messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message) {
        // Validate message text
        if (message.getMessageText() == null || message.getMessageText().isBlank()
                || message.getMessageText().length() > 255) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        // Check if user exists
        Account account = accountService.getAccountById(message.getPostedBy());
        if (account == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Message newMessage = messageService.createMessage(message);
        if (newMessage != null) {
            return new ResponseEntity<>(newMessage, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // Update Message Endpoint
    @PatchMapping("/messages/{id}")
    public ResponseEntity<Integer> updateMessage(@PathVariable Integer id, @RequestBody Message message) {
        // Validate message text
        if (message.getMessageText() == null || message.getMessageText().isBlank()
                || message.getMessageText().length() > 255) {
            return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
        }
        int rowsModified = messageService.updateMessage(id, message.getMessageText());
        if (rowsModified == 1) {
            return new ResponseEntity<>(1, HttpStatus.OK);
        } else {
            // Return 400 Bad Request if message not found
            return new ResponseEntity<>(0, HttpStatus.BAD_REQUEST);
        }
    }

    // ==============================
    // Delete Message by ID Endpoint
    // ==============================

    @DeleteMapping("/messages/{id}")
    public ResponseEntity<Integer> deleteMessageById(@PathVariable Integer id) {
        int rowsDeleted = messageService.deleteMessage(id);
        if (rowsDeleted == 1) {
            return new ResponseEntity<>(1, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.OK); // Empty body
        }
    }

    // ==============================
    // Get Message by ID Endpoint
    // ==============================

    @GetMapping("/messages/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable Integer id) {
        Message message = messageService.getMessageById(id);
        return new ResponseEntity<>(message, HttpStatus.OK);
    }

    // ==============================
    // Get All Messages Endpoint
    // ==============================

    @GetMapping("/messages")
    public ResponseEntity<List<Message>> getAllMessages() {
        List<Message> messages = messageService.getAllMessages();
        return new ResponseEntity<>(messages, HttpStatus.OK);
    }

    // ==============================
    // Get All Messages from User Endpoint
    // ==============================

    @GetMapping("/accounts/{userId}/messages")
    public ResponseEntity<List<Message>> getMessagesByUserId(@PathVariable Integer userId) {
        List<Message> messages = messageService.getMessagesByUserId(userId);
        return new ResponseEntity<>(messages != null ? messages : Collections.emptyList(), HttpStatus.OK);
    }

}