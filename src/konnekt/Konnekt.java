package konnekt;

import konnekt.manager.SessionManager;
import konnekt.view.LoginView;
import konnekt.view.NavigatorView;

public class Konnekt {
    
    public static void main(String[] args) {
        SessionManager.logout();
        if (SessionManager.isLoggedIn()) {
            new NavigatorView().setVisible(true);
            return;
        }
        
        new LoginView().setVisible(true);
    }
}
