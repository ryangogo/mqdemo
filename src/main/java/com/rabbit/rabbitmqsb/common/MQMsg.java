package com.rabbit.rabbitmqsb.common;


public class MQMsg {

    public final static String QUEUE_TO_MYSQL = "mysql";
    public final static String QUEUE_TO_EMAIL = "email";
    public final static String QUEUE_TO_OTHER = "other";

    /**
     * 延时队列
     */
    public final static String QUEUE_TO_DELAYED = "delayed";
    public final static String EXCHANGE = "exchange";
    public final static String DELAYED_EXCHANGE_NAME = "delayed_exchange";

}
