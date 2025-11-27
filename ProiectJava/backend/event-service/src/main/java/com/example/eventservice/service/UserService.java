package com.example.eventservice.service;

import com.example.eventservice.model.UserEntity;
import com.example.eventservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public List<UserEntity> getAllUsers2() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public Optional<UserEntity> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity createUser(UserEntity user) {
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Emailul este deja utilizat.");
        }
        return userRepository.save(user);
    }

    public UserEntity updateUser(Integer id, UserEntity updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setEmail(updatedUser.getEmail());
                    user.setParola(updatedUser.getParola());
                    user.setRol(updatedUser.getRol());
                    return userRepository.save(user);
                }).orElseThrow(() -> new IllegalArgumentException("Utilizatorul nu exista."));
    }

    public void deleteUser(Integer id) {
        if(!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Utilizatorul nu exista.");
        }
        userRepository.deleteById(id);
    }
}
