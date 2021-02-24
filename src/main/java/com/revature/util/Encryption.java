package com.revature.util;

public class Encryption {

    private static Encryption encryption = new Encryption();

    public static Encryption getEncryption() {
        return encryption;
    }

    public String encrypt(String message){
        return message;
    }
}
