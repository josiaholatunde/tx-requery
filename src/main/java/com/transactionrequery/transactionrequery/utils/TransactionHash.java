package com.transactionrequery.transactionrequery.utils;

import org.apache.commons.codec.digest.DigestUtils;

import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class TransactionHash {

    public static String generateSHA512Hash(String input) throws NoSuchAlgorithmException {

        StringBuilder hashtext = new StringBuilder(DigestUtils.sha512Hex(input));

        // Add preceding 0s to make it 32 bit
        while (hashtext.length() < 32) {
            hashtext.append("0");
        }
        // return the HashText
        return hashtext.toString();
    }

    public static String base64EncodeCredentials(String clientId, String clientSecret) {
        String signature = new String(Base64.getEncoder().encode(String.format("%s:%s", clientId, clientSecret).getBytes()));
        String authorizationHeader = String.format("Basic %s", signature);
        return authorizationHeader;
    }



}
