package com.yassir.bank.user;

import com.yassir.bank.exception.DuplicateResourceException;
import com.yassir.bank.exception.InvalidInputException;
import com.yassir.bank.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    @Transactional
    public User createUser(User user) {

        log.info("Checking the usage of the email"+user.toString());
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new DuplicateResourceException("Email '" + user.getEmail() + "' is already used");
        });
        user.setUserId(null);
        userRepository.save(user);
        log.info("User was saved successfully "+user.toString());
        return user;
    }

    @Transactional
    public User updateUser(Long id, User updated) {
        User exists = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        //check if mail already exists
        if (!exists.getEmail().equals(updated.getEmail())) {
            userRepository.findByEmail(updated.getEmail()).ifPresent(u -> {
                throw new DuplicateResourceException("Email '" + updated.getEmail() + "' is already used");
            });
        }

        exists.setName(updated.getName());
        exists.setEmail(updated.getEmail());
        userRepository.save(exists);
        log.info("the user was updated to "+exists.toString());
        return exists;
    }

    @Transactional
    public void deleteById(Long id) {
        User exists = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        userRepository.delete(exists);
    }

}
