package com.mindhub.homebanking.utils;

import java.util.UUID;

public final class ClientUtils {

    public ClientUtils() {
    }

    public static String getAuthCode(){
        return UUID.randomUUID().toString();
    }
}
