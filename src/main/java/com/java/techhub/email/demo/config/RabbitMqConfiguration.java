package com.java.techhub.email.demo.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.*;
import org.springframework.amqp.rabbit.connection.*;
import org.springframework.amqp.rabbit.core.*;
import org.springframework.amqp.support.converter.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;

@Configuration
public class RabbitMqConfiguration {


    @Value("${sr.rabbit.routing.name.mailesend}")
    private String routingNameSend;

    @Value("${sr.rabbit.routing.name.mailesinfo}")
    private String routingNameInfo;

    @Value("${sr.rabbit.routing.name.maileserror}")
    private String routingNameError;

    @Value("${sr.rabbit.exchange.name}")
    private String exchangeName;

    @Value("${spring.rabbitmq.addresses}")
    private String addresses;


// yapılacaklar topic

    @Bean
    TopicExchange exchange() {
        return new TopicExchange("nezha-topicExchange");
    }

    @Bean
    public Queue queueMaileSend() {
        return new Queue(routingNameSend, true);
    }


    @Bean
    public Queue queueMaileInfo() {
        return new Queue(routingNameInfo, true);
    }


    @Bean
    public Queue queueMaileError() {
        return new Queue(routingNameError, true);
    }


    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding bindingMaileSend(final Queue queueMaileSend, final DirectExchange directExchange){

        return BindingBuilder.bind(queueMaileSend).to(directExchange).with(routingNameSend);
    }

    @Bean
    public Binding bindingMaileInfo(final Queue queueMaileInfo, final DirectExchange directExchange){

        return BindingBuilder.bind(queueMaileInfo).to(directExchange).with(routingNameInfo);
    }
    @Bean
    public Binding bindingMaileError(final Queue queueMaileError, final DirectExchange directExchange){

        return BindingBuilder.bind(queueMaileError).to(directExchange).with(routingNameError);
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }


}
