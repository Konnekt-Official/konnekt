package konnekt.component;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GraphPanel extends JPanel {

    private Map<String, Integer> data;

    public GraphPanel(Map<String, Integer> data) {
        this.data = data;
        setPreferredSize(new Dimension(800, 300));
    }

    public void setData(Map<String, Integer> data) {
        this.data = data;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (data == null || data.isEmpty()) return;

        int width = getWidth();
        int height = getHeight();
        int barWidth = width / data.size();

        int max = data.values().stream().max(Integer::compare).orElse(1);

        int x = 0;
        for (var entry : data.entrySet()) {
            int barHeight = (int) ((entry.getValue() / (double) max) * (height - 50));

            g.setColor(new Color(66, 133, 244));
            g.fillRect(x + 10, height - barHeight - 30, barWidth - 20, barHeight);

            g.setColor(Color.BLACK);
            g.drawString(entry.getKey(), x + 10, height - 10);

            x += barWidth;
        }
    }
}
