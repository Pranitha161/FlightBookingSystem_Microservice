package com.flightapp.demo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitConfig {
	public static final String EXCHANGE = "booking.exchange";
	public static final String QUEUE = "booking.queue";
	public static final String ROUTING_KEY = "booking.routingkey";
	@Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
	

	@Bean
	public Queue queue() {
		return new Queue(QUEUE, true);
	}

	@Bean
	public DirectExchange exchange() {
		return new DirectExchange(EXCHANGE);
	}

	@Bean
	public Binding binding(Queue queue, DirectExchange exchange) {
	    return BindingBuilder.bind(queue).to(exchange).with(RabbitConfig.ROUTING_KEY);
	}
}
