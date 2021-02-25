package com.revature.util;

import org.junit.Assert;
import org.junit.Test;

public class EncryptionTest {

    @Test
    public void test_NullString(){
        String test = null;

        String result = Encryption.encrypt(test);

        Assert.assertNull(result);
    }
    @Test
    public void test_encryptingEmptyString(){
        String test = "";

        String result = Encryption.encrypt(test);

        Assert.assertNull(result);
    }
    @Test
    public void test_realString(){
        String test = "pass123";

        String result = Encryption.encrypt(test);

        Assert.assertEquals("vaZ1uuZ3c07w9aOGZwkGlw==", result);
    }
    @Test
    public void test_allSpacesString(){
        String test = "               ";

        String result = Encryption.encrypt(test);

        Assert.assertNull(result);
    }
}
