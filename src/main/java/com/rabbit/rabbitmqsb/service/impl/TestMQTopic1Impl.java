package com.rabbit.rabbitmqsb.service.impl;

import com.rabbit.rabbitmqsb.service.TestMQTopic1;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;



import static com.rabbit.rabbitmqsb.common.MQMsg.QUEUE_TO_MYSQL;

@Service

public class TestMQTopic1Impl implements TestMQTopic1 {


    @RabbitListener(queues = {QUEUE_TO_MYSQL})
    public void process(Channel channel, String msg) {
//        channel.basicReject(msg.get, true);
        System.out.println("ReceiverDB  : " + msg);
    }



}
