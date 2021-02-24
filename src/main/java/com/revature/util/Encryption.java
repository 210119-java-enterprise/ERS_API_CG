package com.revature.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * This is a singleton class that will encrypt a string that is passed
 * to it
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
            logger.error(e.getMessage());
            //e.printStackTrace();
        }

        SecretKey secretKey = new SecretKeySpec(encryptionKeyBytes, "AES");

        try {
            if(cipher == null){
                logger.error("Cipher is null");
            }else {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                encryptedMessage = cipher.doFinal(message.getBytes());
                logger.info("Encrypted the password");
            }
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            logger.error(e.getMessage());
            //e.printStackTrace();
        }

        if(encryptedMessage == null){
            logger.error("The encrypted password is null");
            return null;
        }

        return new String(encryptedMessage);
    }
}
