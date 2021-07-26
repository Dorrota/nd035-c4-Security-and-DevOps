package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;
    private UserRepository userRepo = mock(UserRepository.class);
    private CartRepository cartRepo = mock(CartRepository.class);
    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setUp() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepo);
        TestUtils.injectObjects(userController, "cartRepository", cartRepo);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    @Test
    public void createUserHappyPathTest() {
        when(encoder.encode("testPassword")).thenReturn("ThisIsPassword");
        CreateUserRequest request = new CreateUserRequest();
        request.setUsername("name");
        request.setPassword("testPassword");
        request.setConfirmPassword("testPassword");

        final ResponseEntity response = userController.createUser(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = (User) response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals("name", u.getUsername());
        assertEquals("ThisIsPassword", u.getPassword());

    }
}
