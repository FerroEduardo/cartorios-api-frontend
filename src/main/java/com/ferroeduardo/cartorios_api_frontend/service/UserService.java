package com.ferroeduardo.cartorios_api_frontend.service;

import com.ferroeduardo.cartorios_api_frontend.entity.User;
import com.ferroeduardo.cartorios_api_frontend.exception.UserNotFoundException;
import com.ferroeduardo.cartorios_api_frontend.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final Logger logger;

    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        logger = LoggerFactory.getLogger(UserService.class);
    }

    public User findByUsername(String username) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findFirstByUsername(username);
        return optionalUser.orElseThrow(() -> new UserNotFoundException(username));
    }

    public User findById(Long userId) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.orElseThrow(() -> new UserNotFoundException(userId));
    }

    public User findByEmail(String email) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findFirstByEmail(email);
        return optionalUser.orElseThrow(() -> new UserNotFoundException(email));
    }

    public void save(User user) throws NullPointerException {
        if (user == null) {
            logger.warn("Ponteiro do usuário é null, não foi possivel salvar no banco de dados");
            throw new NullPointerException("Ponteiro do usuário é null, não foi possivel salvar no banco de dados");
        }
        userRepository.save(user);
    }

}
