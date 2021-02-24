package com.revature.util;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * This is a singleton class that will encrypt a string that is passed
 * to it
 */
public class Encryption {

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

        try {
            cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
        }

        SecretKey secretKey = new SecretKeySpec(encryptionKeyBytes, "AES");

        try {
            if(cipher == null){
                throw new RuntimeException("Cipher is null");
            }
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            encryptedMessage = cipher.doFinal(message.getBytes());
        } catch (InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        if(encryptedMessage == null){
            return null;
        }

        return Arrays.toString(encryptedMessage);
    }
}
