package com.yassir.bank.service;

import com.yassir.bank.exception.DuplicateResourceException;
import com.yassir.bank.exception.ResourceNotFoundException;
import com.yassir.bank.model.User;
import com.yassir.bank.repos.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
    }

    public User createUser(User user) {
        //TODO check user
        return userRepository.save(user);
    }

    public User updateUser(Long id, User updated) {
        User existing = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        /*if (updated.getEmail() == null || updated.getName() == null) {
            throw new IllegalArgumentException("Name and email must not be null");
        }*/

        //TODO if email changed, ensure no other user has it

        existing.setName(updated.getName());
        existing.setEmail(updated.getEmail());
        return userRepository.save(existing);
    }

    @Transactional
    public void deleteById(Long id) {
        User existing = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        userRepository.delete(existing);
    }

}
