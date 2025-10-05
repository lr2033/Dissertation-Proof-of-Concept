import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Proof extends JFrame {
    // Instance variables
    private String input;   // User input
    private String output;  // Generated proof

    // GUI Variables
    private JPanel panel;
    private JButton submitButton;
    private JLabel inputLabel;
    private JTextArea inputArea;
    private JLabel outputLabel;
    private JTextArea outputArea;

    // Constructor
    public Proof() {
        // Set up the frame
        panel = new JPanel();
        submitButton = new JButton("Submit");
        inputLabel = new JLabel("Input:");
        inputArea = new JTextArea(10, 30);
        outputLabel = new JLabel("Output:");
        outputArea = new JTextArea(10, 30);
        outputArea.setEditable(false);

        // Set up the panel layout
        panel.setLayout(new GridLayout(5, 1));
        panel.add(inputLabel);
        panel.add(new JScrollPane(inputArea));
        panel.add(submitButton);
        panel.add(outputLabel);
        panel.add(new JScrollPane(outputArea));

        // Button action
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                input = inputArea.getText();
                output = "Generated proof for input: " + input;
                outputArea.setText(output);
            }
        });

        // Add panel to frame
        this.add(panel);

        // Frame settings
        setTitle("Proof Generator");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    // Main method
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Proof();
            }
        });
    }
}
