
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
        //display a test block
        p.paint(g, originX, originY);

        // draw and display some test cubes
        /*
        Cube cube1 = new Cube(100, 100, 0, 0, 0);
        Cube cube2 = new Cube(100, 100, 1, 0, 0);
        Cube cube3 = new Cube(100, 100, 0, 1, 0);
        Cube cube4 = new Cube(100, 100, 0, 0, 1);
        cube4.paint(g);
        cube3.paint(g);
        cube1.paint(g);
        cube2.paint(g);

         */
    }
}
