package Part2;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class HorseRaceGUI {
    private JFrame frame;
    private JButton playButton;
    private JPanel horsePanel;
    private JPanel lanePanel;
    private JButton addHorseButton;
    private JButton addLaneButton;
    private Race race;

    public HorseRaceGUI() {
        race = new Race(10); // Initialize Race object with a length of 10

        frame = new JFrame("Horse Race Simulator");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Play button
        playButton = new JButton("Play");
        playButton.addActionListener(e -> {
            race.startRace();
            // Update GUI to reflect the current state of the race
        });
        frame.add(playButton, BorderLayout.CENTER);

        // Horse panel
        horsePanel = new JPanel();
        horsePanel.add(new JLabel("Horses:"));
        addHorseButton = new JButton("Add Horse");
        addHorseButton.addActionListener(e -> {
            String name = JOptionPane.showInputDialog(frame, "Enter the horse's name:");
            char symbol = JOptionPane.showInputDialog(frame, "Enter the horse's symbol:").charAt(0);
            double confidence = Double.parseDouble(JOptionPane.showInputDialog(frame, "Enter the horse's confidence:"));
            Horse horse = new Horse(symbol, name, confidence);
            race.addHorse(horse);
            horsePanel.add(new JLabel(name));
            frame.pack();
        });
        horsePanel.add(addHorseButton);
        frame.add(horsePanel, BorderLayout.SOUTH);

        // Lane panel
        lanePanel = new JPanel();
        lanePanel.setLayout(new BoxLayout(lanePanel, BoxLayout.Y_AXIS));
        addLaneButton = new JButton("Add Lane");
        addLaneButton.addActionListener(e -> {
            // Add a new lane to the race
            // Update GUI to reflect the new lane
        });
        lanePanel.add(addLaneButton);
        frame.add(lanePanel, BorderLayout.WEST);

        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new HorseRaceGUI();
    }
}
