import java.awt.*;

public class Planet {
    private double x, y;
    private double radius;  // Changed from final to allow updates
    private final double mass;
    private double vx, vy;
    private final Color color;

    public Planet(double x, double y, double radius, double mass, Color color) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.mass = mass;
        this.color = color;
        this.vx = 0;
        this.vy = 1.5; // Initial velocity for orbiting
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getMass() {
        return mass;
    }

    public void setPosition(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void setRadius(double radius) {
        this.radius = radius;
    }

    public void updateVelocity(double ax, double ay) {
        vx += ax;
        vy += ay;
    }

    public void updatePosition() {
        x += vx;
        y += vy;
    }

    public void draw(Graphics g) {
        g.setColor(color);
        g.fillOval((int) (x - radius), (int) (y - radius), (int) (2 * radius), (int) (2 * radius));
    }

    public boolean contains(Point p) {
        return p.distance(x, y) <= radius;
    }
}
