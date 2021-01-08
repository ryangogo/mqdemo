package com.rabbit.rabbitmqsb.service.impl;

import com.rabbit.rabbitmqsb.service.TestMQTopic1;

import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;


import java.io.IOException;

import static com.rabbit.rabbitmqsb.common.MQMsg.QUEUE_TO_DELAYED;
import static com.rabbit.rabbitmqsb.common.MQMsg.QUEUE_TO_MYSQL;

/**
 * 延时队列
 */
@Service
public class TestMQTopic4Impl implements TestMQTopic1 {


    @RabbitListener(queues = {QUEUE_TO_DELAYED})
    public void process(Channel channel, Message message) throws IOException {
        //获取消息
        String msg = new String(message.getBody());
        channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
        System.out.println("ReceiverDelayed  : " + msg);

    }



}
