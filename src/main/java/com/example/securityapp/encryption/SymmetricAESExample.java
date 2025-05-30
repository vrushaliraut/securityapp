package com.example.securityapp.encryption;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class SymmetricAESExample {
    public static void main(String[] args) throws NoSuchAlgorithmException,
            NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException,
            BadPaddingException {
        // Step 1: Generate AES Secret Key
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); // 256-bit key
        SecretKey secretKey = keyGen.generateKey();

        // Step 2: Encrypt Data - using AES Cipher
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] encryptedBytes = cipher.doFinal("This is a secret message".getBytes());
        System.out.println("Encrypted Message: " + new String(encryptedBytes));

        // Step 3: Decrypt Data - using AES Cipher
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
        System.out.println("Decrypted Message: " + new String(decryptedBytes));
    }
}
