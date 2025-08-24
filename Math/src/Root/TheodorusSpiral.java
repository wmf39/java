package Root;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class TheodorusSpiral extends JPanel {
    private static final long serialVersionUID = 1L;
	private final ArrayList<Double> xPoints = new ArrayList<>();
    private final ArrayList<Double> yPoints = new ArrayList<>();
    private final int numTriangles = 30;
    private final int scale = 20; // Zoomfaktor

    public TheodorusSpiral() {
        computeSpiral();
    }

    private void computeSpiral() {
        double x = 0;
        double y = 0;
        double angle = 0;

        xPoints.add(x);
        yPoints.add(y);

        for (int n = 1; n <= numTriangles; n++) {
            double length = Math.sqrt(n);
            x += Math.cos(angle) * length;
            y += Math.sin(angle) * length;

            xPoints.add(x);
            yPoints.add(y);

            // Update angle based on arccos(1 / sqrt(n + 1))
            angle += Math.acos(1 / Math.sqrt(n + 1));
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Set center of canvas
        int w = getWidth() / 2;
        int h = getHeight() / 2;

        g2.setColor(Color.BLUE);

        for (int i = 0; i < xPoints.size() - 1; i++) {
            int x1 = (int) (w + xPoints.get(i) * scale);
            int y1 = (int) (h - yPoints.get(i) * scale);
            int x2 = (int) (w + xPoints.get(i + 1) * scale);
            int y2 = (int) (h - yPoints.get(i + 1) * scale);

            g2.drawLine(x1, y1, x2, y2);
            g2.fillOval(x1 - 2, y1 - 2, 4, 4);
        }
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Theodorus-Spirale");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 800);
        frame.setContentPane(new TheodorusSpiral());
        frame.setVisible(true);
    }
}
