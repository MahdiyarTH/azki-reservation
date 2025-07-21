package com.azki;

import com.azki.availableslot.model.crud.CreateAvailableSlotRequest;
import com.azki.availableslot.service.AvailableSlotService;
import com.azki.user.model.crud.CreateUserRequest;
import com.azki.user.service.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Calendar;

@EnableCaching
@SpringBootApplication
public class AzkiReservationApplication {

    public static void main(String[] args) {
        SpringApplication.run(AzkiReservationApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(UserService userService,
                                               AvailableSlotService availableSlotService) {
        return args -> {
//            insertUsers(userService);
//            insertAvailableSlots(availableSlotService);
        };
    }

    private static void insertUsers(UserService userService) {
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
    }

    private void insertAvailableSlots(AvailableSlotService availableSlotService) {
        Calendar calendar = Calendar.getInstance();
        for (int i = 0; i < 5_000_000; i++) {
            Instant startInstant = calendar.toInstant();

            calendar.add(Calendar.HOUR_OF_DAY, 1);
            Instant endInstant = calendar.toInstant();

            availableSlotService.createAvailableSlot(
                    CreateAvailableSlotRequest.builder()
                            .startTime(
                                    LocalDateTime.ofInstant(
                                            startInstant,
                                            calendar.getTimeZone().toZoneId()
                                    )
                            )
                            .endTime(
                                    LocalDateTime.ofInstant(
                                            endInstant,
                                            calendar.getTimeZone().toZoneId()
                                    )
                            )
                            .build()
            );
        }

        System.out.println("DONE");
    }

}
