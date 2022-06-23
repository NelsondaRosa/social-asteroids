package com.ndr.socialasteroids.presentation;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndr.socialasteroids.presentation.controller.UserController;

import lombok.RequiredArgsConstructor;

@SpringBootTest(classes = UserController.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserControllerTest {
    final MockMvc mockMvc;
    final ObjectMapper mapper;

    @Test
    public void getUserById_success() throws Exception
    {
        
    }
}
