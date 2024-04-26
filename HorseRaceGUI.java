package Part2;
import javax.swing.*;
import java.awt.*;

public class HorseRaceGUI {
    private Race race;
    private JFrame frame;
    private JButton playButton, addHorseButton, removeHorseButton, addLaneButton, removeLaneButton;
    private JPanel horsePanel, lanePanel, buttonPanel;
    private JTextArea raceArea;

    public HorseRaceGUI() {
        race = new Race(100, raceArea);
        raceArea = new JTextArea(20, 50);
        frame = new JFrame("Horse Race Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(new JScrollPane(raceArea), BorderLayout.CENTER);


        // Horse panel
        horsePanel = new JPanel();
        horsePanel.setLayout(new BoxLayout(horsePanel, BoxLayout.Y_AXIS));

        // Lane panel
        lanePanel = new JPanel();
        lanePanel.setLayout(new BoxLayout(lanePanel, BoxLayout.Y_AXIS));

        // Add lane button
        buttonPanel = new JPanel();
        addLaneButton = new JButton("Add Lane");
        addLaneButton.addActionListener(e -> {
            // Add a new lane to the race
            // Update GUI to reflect the new lane
            JPanel lane = new JPanel();
            lane.add(new JLabel("Lane " + (lanePanel.getComponentCount() + 1)));
            lanePanel.add(lane);
            race.addHorse(null, -1);
            frame.pack();
        });
        buttonPanel.add(addLaneButton);
        
        // Remove lane button
        removeLaneButton = new JButton("Remove Lane");
        removeLaneButton.addActionListener(e -> {
            int laneNumber = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter the lane number to remove:"));
            race.removeHorse(laneNumber);
            if (laneNumber >= 1 && laneNumber <= lanePanel.getComponentCount()) {
                // Remove the lane from the GUI
                lanePanel.remove(laneNumber - 1);
                renumberLanes();
                frame.pack();
            } 
            else {
                JOptionPane.showMessageDialog(frame, "Invalid lane number. Please enter a valid lane number.");
            }
        });
        buttonPanel.add(removeLaneButton);
        // Add horse button
        addHorseButton = new JButton("Add Horse");
        addHorseButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "Enter the horse's name:");
            char symbol = JOptionPane.showInputDialog(frame, "Enter the horse's symbol:").charAt(0);
            double confidence = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter the horse's confidence:"));
            int laneNumber = Integer.parseInt(JOptionPane.showInputDialog(frame, "Enter the lane number:"));
            Horse horse = new Horse(symbol, name, confidence);
            // Add horse to the specified lane
            JPanel lane = (JPanel) lanePanel.getComponent(laneNumber - 1);
            lane.add(new JLabel(name));
            race.addHorse(horse, laneNumber);
            frame.pack();
        });
        buttonPanel.add(addHorseButton);

        // Remove horse button
        removeHorseButton = new JButton("Remove Horse");
        removeHorseButton.addActionListener(e -> {
            String laneNumber = JOptionPane.showInputDialog(frame, "Enter the lane number of the horse to remove:");
            race.removeHorse(Integer.parseInt(laneNumber));
            for (Component lane : lanePanel.getComponents()) {
                if (lane instanceof JPanel) {
                    for (Component component : ((JPanel) lane).getComponents()) {
                        if (component instanceof JLabel && ((JLabel) component).getText().equals(laneNumber)) {
                            ((JPanel) lane).remove(component);
                            frame.pack();
                            frame.repaint(); // Repaint the frame after removing a component
                            renumberLanes(); // Call the helper method to re-label the lanes
                            return;
                        }
                    }
                }
            }
        });
        
        buttonPanel.add(removeHorseButton);
    

        // Play button
        ImageIcon playIcon = new ImageIcon("./resources/play.png"); // Path to your image
        playButton = new JButton(playIcon);
        playButton.setPreferredSize(new Dimension(100, 40)); // Set the size of the button
        playButton.addActionListener(e -> {
            race.startRace();
            // Update GUI to reflect the current state of the race
        });
        buttonPanel.add(playButton);

        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.add(lanePanel, BorderLayout.WEST);

        frame.pack();
        frame.setVisible(true);
        }

        // Helper method to re-label the lanes after removing one
        private void renumberLanes() {
            for (int i = 0; i < lanePanel.getComponentCount(); i++) {
                JPanel lane = (JPanel) lanePanel.getComponent(i);
                JLabel laneLabel = (JLabel) lane.getComponent(0);
                laneLabel.setText("Lane " + (i + 1));
            }
        }
    public static void main(String[] args) {
        new HorseRaceGUI();
    }
}
