package com.democrata.tecnica.service;

import com.democrata.tecnica.domain.model.User;

public interface UserService {

    User findById(Long id);

    User create(User userToCreate);
}