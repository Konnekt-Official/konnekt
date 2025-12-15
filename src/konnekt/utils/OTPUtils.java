package konnekt.utils;

import java.security.SecureRandom;

public class OTPUtils {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int OTP_LENGTH = 6;
    private static final SecureRandom random = new SecureRandom();

    public static String generateOTP() {
        StringBuilder otp = new StringBuilder(OTP_LENGTH);
        for (int i = 0; i < OTP_LENGTH; i++) {
            int index = random.nextInt(CHARACTERS.length());
            otp.append(CHARACTERS.charAt(index));
        }
        return otp.toString();
    }
}
