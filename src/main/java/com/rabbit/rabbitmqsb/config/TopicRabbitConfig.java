package com.rabbit.rabbitmqsb.config;

import com.rabbit.rabbitmqsb.common.MQMsg;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TopicRabbitConfig {


    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue queueMysql() {
        return new Queue(MQMsg.QUEUE_TO_MYSQL);
    }

    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue queueEmail() {
        return new Queue(MQMsg.QUEUE_TO_EMAIL);
    }


    /**
     * 创建队列
     *
     * @return
     */
    @Bean
    public Queue queueOther() {
        return new Queue(MQMsg.QUEUE_TO_OTHER);
    }

    /**
     * 创建交换机
     *
     * @return
     */
    @Bean
    TopicExchange exchange() {
        return new TopicExchange(MQMsg.EXCHANGE);
    }

    /**
     * 通过routing key将队列queueMysql 绑定到 交换机MQMsg.EXCHANGE 上
     *
     * @param queueMysql
     * @param exchange
     * @return
     */
    @Bean
    Binding bindingExchangeMysql(Queue queueMysql, TopicExchange exchange) {
        return BindingBuilder.bind(queueMysql).to(exchange).with("test.msq");
    }

    /**
     * 通过routing key将队列queueEmail 绑定到 交换机MQMsg.EXCHANGE 上
     *
     * @param queueEmail
     * @param exchange
     * @return
     */
    @Bean
    Binding bindingExchangeEmail(Queue queueEmail, TopicExchange exchange) {
        return BindingBuilder.bind(queueEmail).to(exchange).with("test.eml");
    }


    /**
     * 通过routing key将队列queueEmail 绑定到 交换机MQMsg.EXCHANGE 上
     * 其中routing key 表示在发送的时候指定的test.xxxxxx格式的routing key都能被该队列接收
     *
     * @param queueOther
     * @param exchange
     * @return
     */
    @Bean
    Binding bindingExchangeOther(Queue queueOther, TopicExchange exchange) {
        //return BindingBuilder.bind(queueOther).to(exchange).with("test.*");//*表示一个词
        return BindingBuilder.bind(queueOther).to(exchange).with("test.#");//#表示零个或多个词
    }

}