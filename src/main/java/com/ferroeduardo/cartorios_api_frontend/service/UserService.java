package com.ferroeduardo.cartorios_api_frontend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ferroeduardo.cartorios_api_frontend.entity.User;
import com.ferroeduardo.cartorios_api_frontend.entity.UserSafeData;
import com.ferroeduardo.cartorios_api_frontend.exception.UserNotFoundException;
import com.ferroeduardo.cartorios_api_frontend.repository.UserRepository;
import com.ferroeduardo.cartorios_api_frontend.util.RequestsUtil;
import com.ferroeduardo.cartorios_api_frontend.util.ServicesCommunicationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

@Service
public class UserService {

    private final Logger logger;

    private UserRepository userRepository;

    private ServicesCommunicationUtil servicesCommunicationUtil;

    public UserService(UserRepository userRepository, ServicesCommunicationUtil servicesCommunicationUtil) {
        this.userRepository = userRepository;
        this.servicesCommunicationUtil = servicesCommunicationUtil;
        logger = LoggerFactory.getLogger(UserService.class);
    }

    @Transactional(readOnly = true)
    public User findByUsername(String username) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findFirstByUsername(username);
        return optionalUser.orElseThrow(() -> new UserNotFoundException(username));
    }

    @Transactional(readOnly = true)
    public User findById(Long userId) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findById(userId);
        return optionalUser.orElseThrow(() -> new UserNotFoundException(userId));
    }

    @Transactional(readOnly = true)
    public User findByEmail(String email) throws UserNotFoundException {
        Optional<User> optionalUser = userRepository.findFirstByEmail(email);
        return optionalUser.orElseThrow(() -> new UserNotFoundException(email));
    }

    @Transactional(readOnly = false)
    public void save(User user) throws NullPointerException {
        if (user == null) {
            logger.warn("Ponteiro do usuário é null, não foi possivel salvar no banco de dados");
            throw new NullPointerException("Ponteiro do usuário é null, não foi possivel salvar no banco de dados");
        }
        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public List<UserSafeData> findUsersWithoutAccessToApi(Pageable pageable) {
        List<UserSafeData> users = userRepository.findAllByApiAccessibleIsFalse(pageable);
        return users;
    }

    @Transactional(readOnly = true)
    public List<UserSafeData> findUsersWithAccessToApi(Pageable pageable) {
        List<UserSafeData> users = userRepository.findAllByApiAccessibleIsTrue(pageable);
        return users;
    }

    @Transactional(readOnly = false)
    public void authorizeUserAccess(Long userId) throws UserNotFoundException, JsonProcessingException {
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> new UserNotFoundException(userId));
        user.setApiAccessible(true);
        userRepository.save(user);
        String requestUrl = "http://localhost:8080/api/key/authorize";
        Map<String, Object> requestBodyMap = new TreeMap<>();
        requestBodyMap.put("userId", userId);
        HttpHeaders headers = RequestsUtil.jsonTypeAuthenticated(
                servicesCommunicationUtil.usernameHeaderName, servicesCommunicationUtil.serviceUsername,
                servicesCommunicationUtil.passwordHeaderName, servicesCommunicationUtil.servicePassword);
        ResponseEntity<String> responseEntity = RequestsUtil.makePostRequest(requestUrl, headers, requestBodyMap, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            logger.info("Usuário de ID:{} teve o acesso autorizado com sucesso", userId);
        } else {
            logger.info("Talvez o usuário de ID:{} não tenha tido o acesso autorizado. HttpStatus: {}", userId, responseEntity.getStatusCodeValue());
        }
    }

    @Transactional(readOnly = false)
    public void revokeUserAccess(Long userId) throws UserNotFoundException, JsonProcessingException {
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.orElseThrow(() -> new UserNotFoundException(userId));
        user.setApiAccessible(false);
        userRepository.save(user);
        String requestUrl = "http://localhost:8080/api/key/revoke";
        Map<String, Object> requestBodyMap = new TreeMap<>();
        requestBodyMap.put("userId", userId);
        HttpHeaders headers = RequestsUtil.jsonTypeAuthenticated(
                servicesCommunicationUtil.usernameHeaderName, servicesCommunicationUtil.serviceUsername,
                servicesCommunicationUtil.passwordHeaderName, servicesCommunicationUtil.servicePassword);
        ResponseEntity<String> responseEntity = RequestsUtil.makePostRequest(requestUrl, headers, requestBodyMap, String.class);
        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            logger.info("Usuário de ID:{} teve o acesso revogado com sucesso", userId);
        } else {
            logger.info("Talvez o usuário de ID:{} não tenha tido o acesso revogado. HttpStatus: {}", userId, responseEntity.getStatusCodeValue());
        }
    }

}
