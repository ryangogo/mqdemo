package com.rabbit.rabbitmqsb.service.impl;

import com.rabbit.rabbitmqsb.service.TestMQTopic2;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.rabbit.rabbitmqsb.common.MQMsg.QUEUE_TO_EMAIL;

@Service
@RabbitListener(queues = {QUEUE_TO_EMAIL})
public class TestMQTopic2Impl implements TestMQTopic2 {

    @RabbitHandler
    public void process(String msg) {
        System.out.println("ReceiverMail  : " + msg);
    }

}
