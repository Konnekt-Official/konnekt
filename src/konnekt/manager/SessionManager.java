package konnekt.manager;

import konnekt.model.dao.UserDao;
import konnekt.model.pojo.UserPojo;

import java.util.prefs.Preferences;

public class SessionManager {

    private static final Preferences prefs = Preferences.userRoot().node("konnekt");
    private static final String KEY_LOGGED_IN_USER_EMAIL = "loggedInUserEmail";
    
    private static UserPojo currentUser = null;

    // Call this after login
    public static void login(String email) {
        prefs.put(KEY_LOGGED_IN_USER_EMAIL, email);
        loadCurrentUser();
    }

    // Load user from email
    private static void loadCurrentUser() {
        String email = prefs.get(KEY_LOGGED_IN_USER_EMAIL, null);
        if (email != null) {
            UserDao userDao = new UserDao();
            currentUser = userDao.getUserByEmail(email);
        }
    }

    // Get logged-in user's email
    public static String getLoggedInUserEmail() {
        return prefs.get(KEY_LOGGED_IN_USER_EMAIL, null);
    }

    // Get logged-in user's ID
    public static int getCurrentUserId() {
        if (currentUser == null) {
            loadCurrentUser();
        }
        return currentUser != null ? currentUser.getId() : -1;
    }

    public static boolean isLoggedIn() {
        return getLoggedInUserEmail() != null;
    }

    public static void logout() {
        prefs.remove(KEY_LOGGED_IN_USER_EMAIL);
        currentUser = null;
    }
}
