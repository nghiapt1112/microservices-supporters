package com.nghiatut.mss.support;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class NotiticationApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(NotiticationApplication.class).run(args);
    }
}
