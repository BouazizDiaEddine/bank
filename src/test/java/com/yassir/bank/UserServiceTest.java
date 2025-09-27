package com.yassir.bank;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.yassir.bank.exception.DuplicateResourceException;
import com.yassir.bank.exception.ResourceNotFoundException;
import com.yassir.bank.user.User;
import com.yassir.bank.user.UserRepository;
import com.yassir.bank.user.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;
    private User otherUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId(1L);
        user.setEmail("king@example.com");
        user.setName("Kerry King");

        otherUser = new User();
        otherUser.setUserId(2L);
        otherUser.setEmail("hell@example.com");
        otherUser.setName("Gordan Ramzi");
    }

    @Test
    void testCreateUser_success() {

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        User createdUser = userService.createUser(user);

        assertNotNull(createdUser);
        assertEquals("king@example.com", createdUser.getEmail());
    }

    @Test
    void testCreateUser_duplicateEmail_throwsException() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        DuplicateResourceException exception = assertThrows(
            DuplicateResourceException.class,
            () -> userService.createUser(user)
        );

        assertEquals("Email 'king@example.com' is already used", exception.getMessage());
    }

    //test update

    @Test
    void testUpdateUser_success() {

        User updatedUser = new User();
        updatedUser.setEmail("new@example.com");
        updatedUser.setName("New Name");


        when(userRepository.findById(user.getUserId())).thenReturn(Optional.of(user));
        when(userRepository.findByEmail(updatedUser.getEmail())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);


        User result = userService.updateUser(user.getUserId(), updatedUser);


        assertNotNull(result);
        assertEquals("New Name", result.getName());
        assertEquals("new@example.com", result.getEmail());

    }

    @Test
    void testUpdateUser_userNotFound_throwsException() {
        Long userId = 1L;
        User updatedUser = new User();
        updatedUser.setEmail("new@example.com");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.updateUser(userId, updatedUser)
        );

        assertEquals("User not found with id 1", ex.getMessage());
    }

    @Test
    void testUpdateUser_duplicateEmail_throwsException() {
        User otherUser2 = new User();
        otherUser2.setEmail("king@example.com");

        when(userRepository.findById(otherUser.getUserId())).thenReturn(Optional.of(otherUser));
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        DuplicateResourceException ex = assertThrows(
                DuplicateResourceException.class,
                () -> userService.updateUser(2L, otherUser2)
        );

        assertEquals("Email 'king@example.com' is already used", ex.getMessage());
    }

}
