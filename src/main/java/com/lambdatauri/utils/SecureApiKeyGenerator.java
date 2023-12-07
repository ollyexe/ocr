package com.lambdatauri.utils;

import java.security.SecureRandom;
import java.util.Base64;

public class SecureApiKeyGenerator {

    private static final int KEY_LENGTH = 32;

    public static String generateSecureApiKey() {
        byte[] randomBytes = new byte[KEY_LENGTH];
        new SecureRandom().nextBytes(randomBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
    }

}