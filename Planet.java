import java.awt.*;

public class Planet {
    private double x, y;
    private double radius;
    private final double mass;
    private double vx, vy;
    private final Color color;

    public Planet(double x, double y, double radius, double mass, Color color) {
        // TODO: Code the constructor
    }

    // TODO: Code getters for X, Y, mass, radius, vx, vy
    // TODO: Code setters for position, radius, velocity

    public void updateVelocity(double ax, double ay) {
    }

    public void updatePosition() {
        this.x += vx;
        this.y += vy;
        if (this.x < 10) {
            this.x = 10;
        }
        if (this.x > 790) {
            this.x = 790;
        }
        if (this.y < 10) {
            this.y = 10;
        }
        if (this.y > 700) {
            this.y = 700;
        }
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int) (x - radius), (int) (y - radius), (int) (2 * radius), (int) (2 * radius));
    }

    public boolean contains(Point p) {
        return p.distance(x, y) <= radius;
    }
}