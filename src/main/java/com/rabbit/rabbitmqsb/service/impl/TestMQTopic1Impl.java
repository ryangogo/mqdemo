package com.rabbit.rabbitmqsb.service.impl;

import com.rabbit.rabbitmqsb.service.TestMQTopic1;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.rabbit.rabbitmqsb.common.MQMsg.QUEUE_TO_EMAIL;
import static com.rabbit.rabbitmqsb.common.MQMsg.QUEUE_TO_MYSQL;

@Service
@RabbitListener(queues = {QUEUE_TO_MYSQL})
public class TestMQTopic1Impl implements TestMQTopic1 {

    @RabbitHandler
    public void process(String msg) {
        System.out.println("ReceiverDB  : " + msg);
    }


}
