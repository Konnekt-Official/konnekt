package konnekt.manager;

import java.util.prefs.Preferences;

public class SessionManager {

    private static final Preferences prefs = Preferences.userRoot().node("konnekt");
    private static final String KEY_LOGGED_IN_USER = "loggedInUser";
    
    public static void login(String email) {
        prefs.put(KEY_LOGGED_IN_USER, email);
    }

    public static String getLoggedInUser() {
        return prefs.get(KEY_LOGGED_IN_USER, null);
    }

    public static boolean isLoggedIn() {
        return getLoggedInUser() != null;
    }

    public static void logout() {
        prefs.remove(KEY_LOGGED_IN_USER);
    }
}
