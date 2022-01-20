import javax.swing.*;
import java.awt.*;

// represent a single isometric cube
public class Cube {
    private int size;
    private int angle; // degrees
    private double angleRadians;
    private Polygon side1;
    private Polygon side2;
    private Polygon top;

    public Cube(int originX, int originY, int offsetX, int offsetY, int offsetZ) {
        // create a cube within a box that has the top left corner at originX, originY
        // the offset X,Y,Z represent the number of cube-lengths in each axis to offset the block position
        size = 30;
        angle = 20;
        angleRadians = Math.toRadians(angle);

        Point vertex1 = new Point(
                (int)(originX +
                (offsetX * size * Math.cos(angleRadians)) +
                (offsetZ * size * Math.cos(angleRadians))),
                (int)(originY +
                        (offsetX * size * Math.sin(angleRadians)) +
                        (offsetY * size) -
                        (offsetZ * size * Math.sin(angleRadians)))
                );
        Point vertex2 = new Point((int)(vertex1.getX() + Math.cos(angleRadians) * size),
                (int)(vertex1.getY() + Math.sin(angleRadians) * size));
        Point vertex3 = new Point((int)vertex2.getX(),
                (int)(vertex2.getY() + size));
        Point vertex4 = new Point((int)vertex1.getX(),
                (int)(vertex3.getY() - Math.sin(angleRadians) * size));
        Point vertex5 = new Point((int)(vertex1.getX() + Math.cos(angleRadians) * size),
                (int)(vertex1.getY() - Math.sin(angleRadians) * size));
        Point vertex6 = new Point((int)(vertex5.getX() + Math.cos(angleRadians) * size),
                (int)(vertex5.getY() + Math.sin(angleRadians) * size));
        Point vertex7 = new Point((int)vertex6.getX(),
                (int)vertex6.getY() + size);
        side1 = new Polygon(
            new int[] {
                (int)vertex1.getX(),
                (int)vertex2.getX(),
                (int)vertex3.getX(),
                (int)vertex4.getX(),
            },
            new int[] {
                (int)vertex1.getY(),
                (int)vertex2.getY(),
                (int)vertex3.getY(),
                (int)vertex4.getY(),
            },4);
        side2 = new Polygon(
            new int[] {
                (int)vertex2.getX(),
                (int)vertex6.getX(),
                (int)vertex7.getX(),
                (int)vertex3.getX(),
            },
            new int[] {
                (int)vertex2.getY(),
                (int)vertex6.getY(),
                (int)vertex7.getY(),
                (int)vertex3.getY(),
            },4);
        top = new Polygon(
            new int[] {
                (int)vertex1.getX(),
                (int)vertex5.getX(),
                (int)vertex6.getX(),
                (int)vertex2.getX(),
            },
            new int[] {
                (int)vertex1.getY(),
                (int)vertex5.getY(),
                (int)vertex6.getY(),
                (int)vertex2.getY(),
            },4);
    }

    public void paint(Graphics g, Color baseColor, boolean solid) {
        Color highlight = baseColor;
        Color shadow = baseColor;
        // draw the cube on a supplied graphic context
        if (solid) {
            g.setColor(shadow);
            g.fillPolygon(side1);
            g.setColor(highlight);
            g.fillPolygon(top);
            g.setColor(baseColor);
            g.fillPolygon(side2);
        }
        g.setColor(Color.black);
        g.drawPolygon(side1);
        g.drawPolygon(top);
        g.drawPolygon(side2);
    }
}

