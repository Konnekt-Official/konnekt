package konnekt.utils;

import javax.swing.*;
import java.awt.*;

public class AvatarUtil {

    public static JLabel avatar(int size) {
        ImageIcon icon = new ImageIcon(
                AvatarUtil.class.getResource("/konnekt/resources/images/default-user.png")
        );

        Image img = icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH);
        JLabel label = new JLabel(new ImageIcon(img));
        label.setPreferredSize(new Dimension(size, size));
        return label;
    }
}
