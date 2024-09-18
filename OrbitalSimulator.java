import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class OrbitalSimulator extends JPanel implements ActionListener {

    private final Timer timer;
    private final List<Planet> planets;
    private Planet selectedPlanet;
    private JSlider radiusSlider;

    public OrbitalSimulator() {
        planets = new ArrayList<>();

        planets.add(new Planet(400, 400, 50, 8e8, Color.BLUE));
        planets.add(new Planet(600, 400, 10, 1e3, Color.RED));

        planets.get(1).setVelocity(0, 0.9);

        radiusSlider = new JSlider(JSlider.HORIZONTAL, 10, 100, 50);
        radiusSlider.addChangeListener(e -> {
            int newRadius = radiusSlider.getValue();
            planets.get(0).setRadius(newRadius);
            repaint();
        });

        timer = new Timer(16, this);
        timer.start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (Planet planet : planets) {
                    if (planet.contains(e.getPoint())) {
                        selectedPlanet = planet;
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                selectedPlanet = null;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (selectedPlanet != null) {
                    selectedPlanet.setPosition(e.getX(), e.getY());
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (Planet planet : planets) {
            planet.draw(g);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        applyGravity();
        repaint();
    }

    private void applyGravity() {
        if (planets.size() < 2) return;

        Planet p1 = planets.get(0);  // Central planet (Blue)
        Planet p2 = planets.get(1);  // Orbiting planet (Red)

        double G = 6.67430e-5; // Gravitational constant

        double dx = p1.getX() - p2.getX();
        double dy = p1.getY() - p2.getY();
        double distance = Math.sqrt(dx * dx + dy * dy); // Distance formula

        double minDistance = p1.getRadius() + p2.getRadius(); // Minimum distance so they don't overlap
        if (distance < minDistance) {
            distance = minDistance;
        }

        double force = (G * p1.getMass() * p2.getMass()) / (distance * distance);

        double directionX = dx / distance;
        double directionY = dy / distance;

        double ax = force / p2.getMass() * directionX;
        double ay = force / p2.getMass() * directionY;

        p2.setVelocity(p2.getVx() + ax, p2.getVy() + ay);

        p2.updatePosition();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Orbital Simulator");
        OrbitalSimulator simulator = new OrbitalSimulator();

        frame.setLayout(new BorderLayout());
        frame.add(simulator, BorderLayout.CENTER);
        frame.add(simulator.radiusSlider, BorderLayout.SOUTH);  // Add the slider at the bottom

        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
