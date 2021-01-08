package com.rabbit.rabbitmqsb.config;

import com.rabbit.rabbitmqsb.common.MQMsg;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.rabbit.rabbitmqsb.common.MQMsg.DELAYED_EXCHANGE_NAME;

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
    public Queue queueDelayed() {
        return new Queue(MQMsg.QUEUE_TO_DELAYED);
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

    @Bean
    public CustomExchange customExchange() {
        Map<String, Object> args = new HashMap<>();
        args.put("x-delayed-type", "direct");
        return new CustomExchange(DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, args);
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
     * 绑定延时队列交换机
     * @param queueDelayed
     * @param customExchange
     * @return
     */
    @Bean
    Binding bindingExchangeDelayed(Queue queueDelayed, CustomExchange customExchange) {
        //return BindingBuilder.bind(queueOther).to(exchange).with("test.*");//*表示一个词
        return BindingBuilder.bind(queueDelayed).to(customExchange).with("test.delayed").noargs();
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





    /**
     * 定制化amqp模版
     　　　* connectionFactory:包含了yml文件配置参数
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        // 必须设置为 true，不然当 发送到交换器成功，但是没有匹配的队列，不会触发 ReturnCallback 回调
        // 而且 ReturnCallback 比 ConfirmCallback 先回调，意思就是 ReturnCallback 执行完了才会执行 ConfirmCallback
        rabbitTemplate.setMandatory(true);
        // 设置 ConfirmCallback 回调   yml需要配置 publisher-confirms: true
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            // 如果发送到交换器都没有成功（比如说删除了交换器），ack 返回值为 false
            // 如果发送到交换器成功，但是没有匹配的队列（比如说取消了绑定），ack 返回值为还是 true （这是一个坑，需要注意）
            if (ack) {
                String messageId = correlationData.getId();
                System.out.println("confirm:"+messageId);
            } else {
                System.out.println("发送到交换机失败");
                //TODO 处理发送失败的消息
                //1:发送消息前,绑定并保存msgId和message的关系(可以加到缓存中)
                //2:根据ack类别等,分别处理. 例如return或者ack=false则说明有问题,报警, 如果ack=true则删除关系
                //3:定时检查这个绑定关系列表,如果发现一些已经超时(自己设定的超时时间)未被处理(即未return和confirm),则手动处理这些消息
            }
        });
        // 设置 ReturnCallback 回调   yml需要配置 publisher-returns: true
        // 如果发送到交换器成功，但是没有匹配的队列，就会触发这个回调
        rabbitTemplate.setReturnCallback((message, replyCode, replyText,
                                          exchange, routingKey) -> {
            //请注意!如果你使用了延迟队列插件，那么一定会调用该callback方法，
            // 因为数据并没有提交上去，而是提交在交换器中，过期时间到了才提交上去，
            // 并非是bug！你可以用if进行判断交换机名称来捕捉该报错
            if(exchange.equals(MQMsg.DELAYED_EXCHANGE_NAME)){
                return;
            }
            String messageId = message.getMessageProperties().getMessageId();
            System.out.println("return:"+messageId);
            //TODO 处理同上
        });
        return rabbitTemplate;
    }


}