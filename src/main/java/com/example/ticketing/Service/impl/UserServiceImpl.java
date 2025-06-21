package com.example.ticketing.Service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ticketing.Model.User;
import com.example.ticketing.Repository.UserRepository;
import com.example.ticketing.Service.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Override
    public User getUser(String uName) {
        return userRepository.getUser(uName);
    }
    
}
