package com.nghiatut.mss.support.notification;

import com.nghiatut.mss.support.notification.model.Email;
import com.nghiatut.mss.support.notification.model.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EmailEvent {

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "${mss.rabbitmq.email.queue}")
    public void email(Email value) {
        this.emailService.send(value);
    }

}
