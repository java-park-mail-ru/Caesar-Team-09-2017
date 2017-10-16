package utils;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.security.SecureRandom;

public class TestUtils {

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public static String getRandomString(SecureRandom random, int length){
        final String lettersAndDigits = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%^&*";
        final StringBuilder stringBuilder = new StringBuilder(length);
        for(int i = 0; i < length; ++i){
            stringBuilder.append(lettersAndDigits.toCharArray()[random.nextInt(lettersAndDigits.length())]);
        }
        return stringBuilder.toString();
    }

    public static void createJsonResponse(@NotNull JSONObject json, @Nullable String username, @Nullable String email, @Nullable String password) throws JSONException {
        if (username == null) {
            json.put("username", getRandomString(SECURE_RANDOM, 10));
        } else {
            json.put("username", username);
        }

        if (email == null) {
            json.put("email", getRandomString(SECURE_RANDOM, 10) + "@mail.ru");
        } else {
            json.put("email", email);
        }

        if (password == null) {
            json.put("password", getRandomString(SECURE_RANDOM, 10));
        } else {
            json.put("password", password);
        }

    }

}
