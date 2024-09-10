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

        planets.add(new Planet(400, 400, 50, 5e9, Color.BLUE));
        planets.add(new Planet(600, 400, 10, 1e8, Color.RED));

        planets.get(1).setVelocity(0, 0.9);

        // Setup the radius slider
        radiusSlider = new JSlider(JSlider.HORIZONTAL, 10, 100, 50); // Min 10, Max 100, Initial 50
        radiusSlider.addChangeListener(e -> {
            int newRadius = radiusSlider.getValue();
            planets.get(0).setRadius(newRadius);  // Adjust the radius of the blue planet
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

        Planet p1 = planets.get(0);
        Planet p2 = planets.get(1);

        double G = 6.67430e-5; // Gravitational constant

        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        double minDistance = p1.getRadius() + p2.getRadius();
        if (distance < minDistance) {
            distance = minDistance;
        }

        double force = G * (p1.getMass() * p2.getMass()) / (distance * distance);

        double ax = force / p2.getMass() * (dx / distance);
        double ay = force / p2.getMass() * (dy / distance);

        p2.updateVelocity(ax, ay);
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
