package com.java.techhub.email.demo.listener;


import com.fasterxml.jackson.databind.*;
import com.java.techhub.email.demo.model.*;
import com.java.techhub.email.demo.producer.*;
import com.java.techhub.email.demo.service.*;
import com.rabbitmq.client.*;
import lombok.extern.slf4j.*;
import org.modelmapper.*;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.amqp.support.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.stereotype.*;
import org.springframework.ui.*;

import java.io.*;
import java.util.*;

@Slf4j
@Service
public class NotificationListener  {

    @Autowired
    private NotificationProducer producer;

    @Autowired
    private EmailService emailService;

    @Autowired
    ModelMapper modelMap;


    @Autowired
    ObjectMapper jsonMaileUser;

    @RabbitListener(queues = "mailesend")
    public void handleMessageMaileSend(Notification notification,Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag ) throws IOException {
        log.debug("Maile send listener, Notification: {}",notification.toString());
        channel.basicAck(tag, true);

        try {

            MaileUser user=new MaileUser();
            user= jsonMaileUser.readValue(notification.getMessage(),MaileUser.class);

            notification.setMessage(jsonMaileUser.writeValueAsString(emailService.sendEmail(user)));
            notification.setSeen(true);
            producer.sendToQueueMaileInfo(notification);
        }catch (Exception ex){
            notification.setMessage("Send Error");
            notification.setSeen(true);
            producer.sendToQueueMaileInfo(notification);
        }


    }



//    @RabbitListener(queues = "mailesinfo")
//    public void handleMessageMailseInfo(Notification notification,Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag )throws IOException {
//        System.out.println("maileinfo Message received..");
//        System.out.println(notification.toString());
//        channel.basicAck(tag, false);
//
//    }
}
