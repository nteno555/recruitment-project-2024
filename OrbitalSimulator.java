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
        planets.add(new Planet(300, 300, 30, 5e24, Color.BLUE));  // Starter planet (Blue)
        // Add more planets here

        // Setup the radius slider
        radiusSlider = new JSlider(JSlider.HORIZONTAL, 10, 100, 30); // Min 10, Max 100, Initial 30
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

        Planet p1 = planets.get(0);  // Central planet (Blue)
        Planet p2 = planets.get(1);  // This is the planet you will add

        double G = 6.67430e-11; // Gravitational constant

        // Calculate distance
        double dx = p2.getX() - p1.getX();
        double dy = p2.getY() - p1.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        // Calculate force
        double force = G * (p1.getMass() * p2.getMass()) / (distance * distance);

        // Calculate acceleration
        double ax = force / p2.getMass() * (dx / distance);
        double ay = force / p2.getMass() * (dy / distance);

        // Update velocity and position
        p2.updateVelocity(ax, ay);
        p2.updatePosition();
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Planet Sim");
        OrbitalSimulator simulator = new OrbitalSimulator();

        frame.setLayout(new BorderLayout());
        frame.add(simulator, BorderLayout.CENTER);
        frame.add(simulator.radiusSlider, BorderLayout.SOUTH);  // Add the slider at the bottom

        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
