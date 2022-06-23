package com.ndr.socialasteroids.presentation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ndr.socialasteroids.business.service.FriendshipService;
import com.ndr.socialasteroids.domain.entity.Friendship;
import com.ndr.socialasteroids.domain.entity.User;

import lombok.RequiredArgsConstructor;

@WebMvcTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FriendshipControllerTest
{
    final MockMvc mockMvc;
    final ObjectMapper objectMapper;

    @MockBean
    FriendshipService friendShipService;

    User USER_1 = new User("Fr0stZero", "frost@gmail.com", "1234");
    User USER_2 = new User("coldzera", "cold@gmail.com", "1234");
    User USER_3 = new User("fallen", "fall@hotmail.com", "1234");
    User USER_4 = new User("lolzero", "talon@gmail.com", "1234");

    //1 invited 2, 2 invited 4
    Friendship INVITE_1_2 = new Friendship(USER_1, USER_2, false);
    Friendship INVITE_2_4 = new Friendship(USER_2, USER_4, false);
    
    //1 and 3, 3 and 4 are friends
    Friendship FRIENDSHIP_1_3 = new Friendship(USER_1, USER_3, true);
    Friendship FRIENDSHIP_3_1 = new Friendship(USER_3, USER_1, true);
    Friendship FRIENDSHIP_4_3 = new Friendship(USER_4, USER_3, true);
    Friendship FRIENDSHIP_3_4 = new Friendship(USER_3, USER_4, true);
    Friendship FRIENDSHIP_2_3 = new Friendship(USER_2, USER_3, true);
    Friendship FRIENDSHIP_3_2 = new Friendship(USER_3, USER_2, true);
    
}
