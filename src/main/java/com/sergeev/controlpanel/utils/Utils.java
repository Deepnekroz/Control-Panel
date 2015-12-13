package com.sergeev.controlpanel.utils;

import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by dmitry-sergeev on 22.09.15.
 */
public class Utils {

    private static final Logger LOG = org.slf4j.LoggerFactory.getLogger(Utils.class);

    public static ResponseEntity status(int status){
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("status", HttpStatus.valueOf(status).name());
        return new ResponseEntity<>(jsonObject.toString(), HttpStatus.valueOf(status));
    }
/*
    public static KeyPair readKeypair(final InputStream is, final char[] password) throws IOException {
        PasswordFinder passwordFinder = password != null ? new StaticPasswordFinder(password) : null;

        KeyPair kp = null;
        try {
            // read the stream as a PEM encoded
            try {

                final PemReader pem = new PemReader(new InputStreamReader(is), passwordFinder);
                try {
                    // Skip over entries in the file which are not KeyPairs
                    do {
                        final Object o = pem.readObject();

                        if (o == null)
                            break; // at end of file
                        else if (o instanceof KeyPair)
                            kp = (KeyPair) o;
                    } while (kp == null);
                }
                finally {
                    pem.close();
                }
            }
            catch (EncryptionException e) {
                throw new IOException("Error reading PEM stream: " + e.getMessage(), e);
            }
        }
        finally {
            is.close();
        }

        // Cast the return to a KeyPair (or, if there is no [valid] return, throw an exception)
        if (kp != null)
            return kp;
        else
            throw new IOException("Stream " + is + " did not contain a PKCS8 KeyPair");
    }
*/
}
