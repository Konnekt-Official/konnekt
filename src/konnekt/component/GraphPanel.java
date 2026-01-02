package konnekt.component;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

public class GraphPanel extends JPanel {

    private Map<String, Integer> data;

    public GraphPanel(Map<String, Integer> data) {
        this.data = data;
        setPreferredSize(new Dimension(900, 300));
        setBackground(Color.WHITE);
    }

    public void setData(Map<String, Integer> data) {
        this.data = data;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (data == null || data.isEmpty()) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        int w = getWidth();
        int h = getHeight();

        int padding = 40;
        int labelPadding = 20;

        int graphWidth = w - padding * 2;
        int graphHeight = h - padding * 2;

        // ---------- AXES ----------
        g2.setColor(Color.DARK_GRAY);
        g2.drawLine(padding, h - padding, padding, padding); // Y axis
        g2.drawLine(padding, h - padding, w - padding, h - padding); // X axis

        int maxValue = data.values().stream().max(Integer::compare).orElse(1);

        int barCount = data.size();
        int barWidth = graphWidth / barCount - 10;

        int x = padding + 10;

        // ---------- BARS ----------
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            int value = entry.getValue();

            int barHeight = (int) ((value / (double) maxValue) * (graphHeight - 20));
            int y = h - padding - barHeight;

            // bar
            g2.setColor(new Color(66, 133, 244));
            g2.fillRect(x, y, barWidth, barHeight);

            // value on top
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Verdana", Font.PLAIN, 11));
            g2.drawString(String.valueOf(value), x + barWidth / 4, y - 5);

            // label
            String label = entry.getKey();
            g2.drawString(label, x, h - padding + labelPadding);

            x += barWidth + 10;
        }
    }
}
