package se.callista.microservises.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.context.ConfigurableApplicationContext;

@EnableConfigServer
@EnableDiscoveryClient
@SpringBootApplication
public class ConfigServerApplication {

    private static final Logger LOG = LoggerFactory.getLogger(ConfigServerApplication.class);

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(ConfigServerApplication.class, args);

        LOG.info("Connected to RabbitMQ at: {}", ctx.getEnvironment().getProperty("spring.rabbitmq.host"));
    }
}
