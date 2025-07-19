package com.azki;

import com.azki.user.model.crud.CreateUserRequest;
import com.azki.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;

@SpringBootApplication
public class AzkiReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AzkiReservationApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserService userService) {
        return args -> {
            int count = 1_000_000;
            CreateUserRequest[] createUserRequests = new CreateUserRequest[count];
            for (int i = 0; i < count; i++) {
                createUserRequests[i] = new CreateUserRequest(
                        "user" + i,
                        "password" + i,
                        "user" + i + "@azki.com"
                );
            }
            long a = System.currentTimeMillis();
            userService.createUsers(Arrays.asList(createUserRequests));
            System.out.println(System.currentTimeMillis() - a);

        };
    }

}
