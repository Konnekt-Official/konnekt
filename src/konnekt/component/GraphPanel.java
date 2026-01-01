package konnekt.component;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GraphPanel extends JPanel {

    private Map<String, Integer> data;

    public GraphPanel(Map<String, Integer> data) {
        this.data = data;
        setPreferredSize(new Dimension(900, 300));
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
        int barWidth = Math.max(30, width / data.size() - 10);

        int max = data.values().stream().max(Integer::compare).orElse(1);

        int x = 20;
        for (var entry : data.entrySet()) {
            int barHeight = (int) ((entry.getValue() / (double) max) * (height - 50));

            g.setColor(new Color(66, 133, 244));
            g.fillRect(x, height - barHeight - 30, barWidth, barHeight);

            g.setColor(Color.BLACK);
            String label = entry.getKey(); // date
            g.drawString(label, x, height - 10);

            x += barWidth + 10;
        }
    }
}
