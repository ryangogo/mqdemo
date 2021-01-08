package com.rabbit.rabbitmqsb.service.impl;

import com.rabbit.rabbitmqsb.service.TestMQTopic2;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.rabbit.rabbitmqsb.common.MQMsg.QUEUE_TO_EMAIL;

@Service
public class TestMQTopic2Impl implements TestMQTopic2 {

    @RabbitListener(queues = {QUEUE_TO_EMAIL})
    public void process(Channel channel, Message message) throws IOException {
        String msg = new String(message.getBody());
//        channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        //true 返回队列 false 不返回队列
        //channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
        System.out.println("ReceiverMail  : " + msg);

    }

}
