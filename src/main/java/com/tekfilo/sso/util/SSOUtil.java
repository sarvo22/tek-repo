package com.tekfilo.sso.util;

import com.tekfilo.sso.auth.InviteForm;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Random;

@Slf4j
public class SSOUtil {


    public static final String SPLITTER = "###";


    public static String generateStrongPasswordHash(String password)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        int iterations = 1000;
        char[] chars = password.toCharArray();
        byte[] salt = getSalt();

        PBEKeySpec spec = new PBEKeySpec(chars, salt, iterations, 64 * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        byte[] hash = skf.generateSecret(spec).getEncoded();
        return iterations + ":" + toHex(salt) + ":" + toHex(hash);
    }

    public static byte[] getSalt() throws NoSuchAlgorithmException {
        SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
        byte[] salt = new byte[16];
        sr.nextBytes(salt);
        return salt;
    }

    public static String toHex(byte[] array) throws NoSuchAlgorithmException {
        BigInteger bi = new BigInteger(1, array);
        String hex = bi.toString(16);

        int paddingLength = (array.length * 2) - hex.length();
        if (paddingLength > 0) {
            return String.format("%0" + paddingLength + "d", 0) + hex;
        } else {
            return hex;
        }
    }

    public static boolean validatePassword(String originalPassword, String storedPassword)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        String[] parts = storedPassword.split(":");
        int iterations = Integer.parseInt(parts[0]);

        byte[] salt = fromHex(parts[1]);
        byte[] hash = fromHex(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(originalPassword.toCharArray(),
                salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        int diff = hash.length ^ testHash.length;
        for (int i = 0; i < hash.length && i < testHash.length; i++) {
            diff |= hash[i] ^ testHash[i];
        }
        return diff == 0;
    }

    private static byte[] fromHex(String hex) throws NoSuchAlgorithmException {
        byte[] bytes = new byte[hex.length() / 2];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) Integer.parseInt(hex.substring(2 * i, 2 * i + 2), 16);
        }
        return bytes;
    }

    public static String getSignupVerificationUrl(String hostUrl, Integer signupId, String tokenVerificationCode) {
        if (hostUrl.endsWith("/")) {
            return hostUrl + signupId + "/" + tokenVerificationCode;
        }
        return hostUrl + "/" + signupId + "/" + tokenVerificationCode;
    }

    /**
     * Generate Unique 10 digit Alpha + Numeric values as Unique ID
     *
     * @return
     */
    public static String getUserUID() {
        Random random = new Random();
        String total_characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String randomString = "";
        for (int i = 0; i < 10; i++) {
            int index = random.nextInt(total_characters.length() - 1);
            randomString += total_characters.charAt(index);
        }
        return randomString.toUpperCase();
    }

    public static String generateUniqueID() {
        Random random = new Random();
        String total_characters = "0123456789";
        String randomString = "";
        for (int i = 0; i < 11; i++) {
            int index = random.nextInt(total_characters.length() - 1);
            randomString += total_characters.charAt(index);
        }
        return randomString.toUpperCase();
    }

    public static String encode(InviteForm inviteForm) {
        final String originalInput = inviteForm.getEmail()
                .concat(SPLITTER)
                .concat(inviteForm.getSelectedRoleId()
                        .concat(SPLITTER)
                        .concat(inviteForm.getSenderEmail())
                        .concat(SPLITTER).concat(inviteForm.getSenderName())
                        .concat(SPLITTER).concat(inviteForm.getSenderCompanyId().toString())
                        .concat(SPLITTER).concat(inviteForm.getSenderCompanyName())
                );
        String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
        return encodedString;
    }

    public static String decode(String encodedString) {
        byte[] decodedBytes = Base64.getDecoder().decode(encodedString);
        String decodedString = new String(decodedBytes);
        return decodedString;
    }
}
