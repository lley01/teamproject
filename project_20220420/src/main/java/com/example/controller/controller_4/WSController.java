// package com.example.controller.controller_4;

// import com.example.dto.Message;
// import com.example.service.service_4.WSService;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// public class WSController {

//     @Autowired
//     private WSService service;

//     @PostMapping("/send-message")
//     public void sendMessage(@RequestBody final Message message) {
//         service.notifyFrontend(message.getMessageContent());

//     }
    
// }
