package com.rabbit.rabbitmqsb.service.impl;

import com.rabbit.rabbitmqsb.service.TestMQTopic3;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.rabbit.rabbitmqsb.common.MQMsg.QUEUE_TO_OTHER;

@Service
@RabbitListener(queues = {QUEUE_TO_OTHER})
public class TestMQTopic3Impl implements TestMQTopic3 {

    @RabbitHandler
    public void process(String msg) {
        System.out.println("ReceiverOther  : " + msg);
    }

}
