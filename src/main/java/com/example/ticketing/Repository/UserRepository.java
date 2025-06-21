package com.example.ticketing.Repository;

import com.example.ticketing.Model.User;

public interface UserRepository {
    
    User getUser (String username);

}
