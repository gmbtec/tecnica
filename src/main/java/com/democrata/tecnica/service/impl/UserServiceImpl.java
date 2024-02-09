package com.democrata.tecnica.service.impl;

import com.democrata.tecnica.domain.model.User;
import com.democrata.tecnica.service.UserService;
import com.democrata.tecnica.domain.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findById(Long id) {
        //semantica no exception padrao
        return userRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @Override
    public User create(User userToCreate) {
        //se o usuario ja existe no banco entao monta a informacao
        //usando uma select com um join sem a necessidade de usar uma query nativa
        if (userRepository.existsByAccountNumber(userToCreate.getAccount().getNumber())) {
            throw new IllegalArgumentException("This Account number already exists.");
        }
        return userRepository.save(userToCreate);
    }
}
