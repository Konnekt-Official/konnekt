package konnekt;

import konnekt.manager.SessionManager;
import konnekt.view.LoginView;
import konnekt.view.NavigatorView;

public class Konnekt {
    
    public static void main(String[] args) {
        /*if (Session.isLoggedIn()) {
            new FeedView().setVisible(true);
            return;
        }*/
        
        new LoginView().setVisible(true);
    }
}
