package com.ndr.socialasteroids.security.encoding;

import java.security.Key;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import com.ndr.socialasteroids.infra.error.exception.EncrypterException;

public class Encrypter
{
    private static final String ALGO = "AES";

    private static byte[] secret = new byte[]
        {49, 113, -25, -75, -110, -96, -113, 47, -123, -83, -40, 46, -68, 100, 91, -59};
    
    public static String encrypt(String data)
    {
        String encodedData = "";
        try
        {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(ALGO);

            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encodedValue = cipher.doFinal(data.getBytes());

            encodedData = Base64.getEncoder().encodeToString(encodedValue);
        } 
        catch (Exception ex)
        {
            throw new EncrypterException("Failed to encrypt necessary data");
        }

        return encodedData;
    }

    public static String decrypt(String encryptedData)
    {
        String plainData;
        try
        {
            Key key = generateKey();
            Cipher cipher = Cipher.getInstance(ALGO);

            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] byteEncryptedData = Base64.getDecoder().decode(encryptedData);
            byte[] byteDecryptedData = cipher.doFinal(byteEncryptedData);

            plainData = new String(byteDecryptedData);
        }
        catch(Exception ex)
        {
            throw new EncrypterException("Failed to decrypt necessary data");
        }

        return plainData;
    }

    private static Key generateKey()
    {
        SecretKeySpec key = new SecretKeySpec(secret, ALGO);
        return key;
    }
}
