
import java.awt.*;
import javax.swing.*;

// display blocks in isometric 3D
class Viewer extends JComponent {
    private final int WIDTH;
    private final int HEIGHT;
    private Puzzle p;
    private int originX;
    private int originY;

    public Viewer(int width, int height) {
        this.WIDTH = width;
        this.HEIGHT = height;

        // creating object of JFrame(Window popup)
        JFrame window = new JFrame("Puzzle Viewer");

        // setting closing operation
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // setting size of the pop window
        int xpos = 0;
        int ypos = 0;
        window.setBounds(xpos, ypos, width, height+50); // +50 to allow for the title bar

        // setting canvas for drawing
        window.getContentPane().add(this);

        // set visibility
        window.setVisible(true);

    }

    public void render(Puzzle p, int originX, int originY) {
        this.p = p;
        this.originX = originX;
        this.originY = originY;
        repaint();
    }

    public int scaleX(double x) {
        return (int)(x/300*WIDTH);
    }

    public int scaleY(double y) {
        return (int)(HEIGHT - 5 - (y/180*HEIGHT));
    }

    public void paint(Graphics g) {
        if (p !=null) {
            p.paint(g, originX, originY);
        }
    }
}
