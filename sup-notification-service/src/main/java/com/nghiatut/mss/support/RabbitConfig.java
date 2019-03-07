package com.nghiatut.mss.support;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    @Autowired
    private ConnectionFactory connectionFactory;

    // TODO: -> nghiapt must check the configuration in link: https://github.com/spring-projects/spring-amqp/blob/master/spring-rabbit/src/test/java/org/springframework/amqp/rabbit/annotation/EnableRabbitIntegrationTests.java#L851
    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory() {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(new Jackson2JsonMessageConverter());
        factory.setReceiveTimeout(10L);
        factory.setReplyRecoveryCallback(c -> null);
//        factory.setErrorHandler(TODO: implement errorHandler here);
//        factory.setConsumerTagStrategy(TODO: consumer tag strategy);
//        factory.setRetryTemplate(TODO: retry template);
        factory.setBeforeSendReplyPostProcessors(message -> {
            message.getMessageProperties().getHeaders().put("replyMPPApplied", true);
            return message;
        });
        return factory;
    }

}
