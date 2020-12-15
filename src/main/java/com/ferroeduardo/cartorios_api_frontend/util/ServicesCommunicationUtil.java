package com.ferroeduardo.cartorios_api_frontend.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ServicesCommunicationUtil {

    public final String serviceUsername;

    public final String servicePassword;

    public final String usernameHeaderName;

    public final String passwordHeaderName;

    public ServicesCommunicationUtil(@Value(value = "${service.username}") String serviceUsername,
                                     @Value(value = "${service.password}") String servicePassword,
                                     @Value(value = "${service.header.username}") String usernameHeaderName,
                                     @Value(value = "${service.header.password}") String passwordHeaderName) {
        this.serviceUsername = serviceUsername;
        this.servicePassword = servicePassword;
        this.usernameHeaderName = usernameHeaderName;
        this.passwordHeaderName = passwordHeaderName;
    }
}
