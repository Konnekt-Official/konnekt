package konnekt.view;

import java.awt.Dimension;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public abstract class BaseFrame extends JFrame {

    public BaseFrame() {
        setDefaultSettings();
        setDefaultIcon(); // sets multi-resolution icons
    }

    private void setDefaultSettings() {
        setPreferredSize(new Dimension(900, 600));
        pack();
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(700, 450));
        setResizable(true);
    }

    protected void setDefaultIcon() {
        try {
            ImageIcon icon16 = new ImageIcon(getClass().getResource("/konnekt/resources/images/icons/icon16.png"));
            ImageIcon icon32 = new ImageIcon(getClass().getResource("/konnekt/resources/images/icons/icon32.png"));
            ImageIcon icon64 = new ImageIcon(getClass().getResource("/konnekt/resources/images/icons/icon64.png"));
            ImageIcon icon128 = new ImageIcon(getClass().getResource("/konnekt/resources/images/icons/icon128.png"));
            ImageIcon icon256 = new ImageIcon(getClass().getResource("/konnekt/resources/images/icons/icon256.png"));

            setIconImages(java.util.List.of(
                    icon16.getImage(),
                    icon32.getImage(),
                    icon64.getImage(),
                    icon128.getImage(),
                    icon256.getImage()
            ));
        } catch (Exception e) {
            System.err.println("Icon(s) not found!");
        }
    }

    // Set single custom icon using path
    protected void setAppIcon(String path) {
        Image icon = new ImageIcon(getClass().getResource(path)).getImage();
        setIconImage(icon);
    }
}