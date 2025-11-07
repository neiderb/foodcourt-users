package com.foodcourt.users;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.times;

class UsersApplicationTest {

    @Test
    void shouldInvokeSpringApplicationRunWhenMainIsCalled() {
        try (MockedStatic<SpringApplication> springAppMock = Mockito.mockStatic(SpringApplication.class)) {
            springAppMock.when(() -> SpringApplication.run(Mockito.eq(UsersApplication.class), Mockito.any(String[].class)))
                    .thenReturn(Mockito.mock(ConfigurableApplicationContext.class));

            UsersApplication.main(new String[]{});

            springAppMock.verify(() -> SpringApplication.run(Mockito.eq(UsersApplication.class), Mockito.any(String[].class)), times(1));
        }
    }

}

