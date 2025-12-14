package konnekt;

import konnekt.view.RegisterView;

public class Konnekt {
    
    public static void main(String[] args) {
        /*
        The code below creating the object to call setVisible method is redundent
        because view class already does this in there static main method
        */ 
        new RegisterView().setVisible(true);
    }
}
