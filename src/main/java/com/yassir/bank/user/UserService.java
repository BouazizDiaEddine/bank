package com.yassir.bank.user;

import com.yassir.bank.exception.DuplicateResourceException;
import com.yassir.bank.exception.InvalidInputException;
import com.yassir.bank.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Transactional
    public User createUser(User user) {

        //check if email already exists
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            throw new DuplicateResourceException("Email '" + user.getEmail() + "' is already used");
        });

        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(Long id, User updated) {
        User exists = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));

        //check the existence of mail and name
        if (updated.getEmail() == null || updated.getEmail().isEmpty()
                || updated.getName() == null || updated.getName().isEmpty()) {
            throw new InvalidInputException("Name and email are mandatory");
        }

        //check if mail already exists
        if (!exists.getEmail().equals(updated.getEmail())) {
            userRepository.findByEmail(updated.getEmail()).ifPresent(u -> {
                throw new DuplicateResourceException("Email '" + updated.getEmail() + "' is already used");
            });
        }

        exists.setName(updated.getName());
        exists.setEmail(updated.getEmail());
        return userRepository.save(exists);
    }

    @Transactional
    public void deleteById(Long id) {
        User exists = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        userRepository.delete(exists);
    }

}
