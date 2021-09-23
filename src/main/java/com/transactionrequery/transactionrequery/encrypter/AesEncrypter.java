package com.transactionrequery.transactionrequery.encrypter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class AesEncrypter {

    private static final String AES_ALGORITHM = "AES/GCM/NoPadding";
    private static final int AUTH_TAG_LENGTH = 128;
    private static final Charset CHARSET = StandardCharsets.UTF_8;
    private SecureRandom secureRandom = new SecureRandom();

    @Value("${app.encryption.key:FA2CD123BBA23DFB}")
    private String encryptionKey;

    public String encrypt(String data) throws Exception {
        if (StringUtils.isEmpty(data)) {
            throw new Exception("Invalid data supplied");
        }

        try {
            final Cipher cipher = Cipher.getInstance(AES_ALGORITHM);

            String nonce = getIV();

            byte[] iv = nonce.getBytes();

            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(), new GCMParameterSpec(AUTH_TAG_LENGTH, iv));

            byte[] encryptedDataBytes = cipher.doFinal(data.getBytes(CHARSET));

            String encryptedData = Base64.getEncoder().encodeToString(encryptedDataBytes);
            String builder = nonce + ":" + encryptedData;

            return Base64.getEncoder().encodeToString(builder.getBytes(CHARSET));

        } catch (Exception e) {
            throw new Exception("An error occurred while encrypting data", e);
        }

    }

    private String getIV() {
        byte[] iv = new byte[12];
        secureRandom.nextBytes(iv);
        return new String(iv);
    }

    public String decrypt(String data) throws Exception {

        if (StringUtils.isEmpty(data)) {
            throw new Exception("Invalid data supplied");
        }

        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM);


            String combinedData = new String(Base64.getDecoder().decode((data)));

            int lastIndexOfColon = combinedData.lastIndexOf(':');

            String nonce = combinedData.substring(0, lastIndexOfColon);

            String encryptedDataString = combinedData.substring(lastIndexOfColon + 1);

            byte[] iv = nonce.getBytes();

            cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(), new GCMParameterSpec(AUTH_TAG_LENGTH, iv));

            byte[] encryptedData = Base64.getDecoder().decode(encryptedDataString);

            byte[] decryptedDataBytes = cipher.doFinal(encryptedData);

            return new String(decryptedDataBytes);
        } catch (Exception e) {
            throw new Exception("An error occurred while decrypting data", e);
        }
    }

    private SecretKeySpec getSecretKeySpec() {
        return new SecretKeySpec(encryptionKey.getBytes(CHARSET), "AES");
    }
}
