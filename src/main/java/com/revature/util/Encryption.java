package com.revature.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import java.util.Base64;

/**
 * This is a singleton class that will encrypt a string that is passed
 * to it
 * @author Cole Space
 * @author Gabrielle Luna
 */
public class Encryption {

    private static final Logger logger = LogManager.getLogger(Encryption.class);

    /**
     * This method will encrypt the message passed to it with AES/ECB encryption
     * using 128 bits and padded with PKCS5
     * @param message the message to be encrypted
     * @return the encrypted message string
     */
    public static String encrypt(String message){

        byte[] encryptionKeyBytes = S3BucketReader.getAesPrivateKey().getBytes();
        byte[] encryptedMessage = null;
        Cipher cipher = null;

        logger.info("Getting the cipher instance");

        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            logger.fatal(e.getMessage());
            return null;
        }

        SecretKey secretKey = new SecretKeySpec(encryptionKeyBytes, "AES");

        try {
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            if(message == null || message.trim().equals("")){
                return null;
            }
            encryptedMessage = cipher.doFinal(message.getBytes(StandardCharsets.UTF_8));
            logger.info("Encrypted the password");
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            logger.fatal(e.getMessage());
            return null;
        }

        return Base64.getEncoder().encodeToString(encryptedMessage);
    }
}
