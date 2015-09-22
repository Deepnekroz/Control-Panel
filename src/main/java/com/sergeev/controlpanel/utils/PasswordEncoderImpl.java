package com.sergeev.controlpanel.utils;

import org.slf4j.Logger;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */
public class PasswordEncoderImpl implements PasswordEncoder {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(PasswordEncoderImpl.class);

    private volatile static Random random;

    //Double checked locking
    private Random getRandom() {
        if (random == null) {
            synchronized (Utils.class) {
                if (random == null) {
                    random = new Random();
                }
            }
        }
        return random;
    }

    private String getPasswordHash(CharSequence rawPassword) throws Exception {
        LOG.debug("Given password: " + rawPassword);
        byte[] salt = new byte[16];
        getRandom().nextBytes(salt);
        KeySpec spec = new PBEKeySpec(rawPassword.toString().toCharArray(), salt, 65536, 128);
        SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = f.generateSecret(spec).getEncoded();
        Base64.Encoder enc = Base64.getEncoder();
        //System.out.printf("salt: %s%n", enc.encodeToString(salt));
        //System.out.printf("hash: %s%n", enc.encodeToString(hash));

        LOG.debug("Produced hash: " + enc.encodeToString(hash));

        return enc.encodeToString(hash);


    }

    @Override
    public String encode(CharSequence rawPassword) {
        String encoded=null;
        try{
            encoded = getPasswordHash(rawPassword);
        }catch (Exception e){
            LOG.error(e.toString());
        }
        return encoded;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encode(rawPassword).equals(encodedPassword);
    }
}
