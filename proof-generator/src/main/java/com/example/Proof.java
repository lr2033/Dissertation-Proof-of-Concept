package com.example;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import javax.swing.*;
import com.google.genai.Client;
import com.google.genai.ResponseStream;
import com.google.genai.types.*;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import java.util.List;



public class Proof extends JFrame {
    // Instance variables
    private String input;   // User input

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
                String output = generateProof(input);
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

    //Function to generate output
    private String generateProof(String input) {
    // Load context from file
    String context;
    try (BufferedReader br = new BufferedReader(new FileReader("Context.txt"))) {
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        context = sb.toString();
    } catch (Exception e) {
        e.printStackTrace();
        return "Error reading context file.";
    }

    // Combine context and input
    String prompt = context + " " + input;

    // GenAI setup
    try {
        String apiKey = System.getenv("GOOGLE_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            return "API key not set.";
        }

        Client client = Client.builder().apiKey(apiKey).build();
        String model = "gemini-2.5-flash-image";

        List<Content> contents = ImmutableList.of(
            Content.builder()
                .role("user")
                .parts(ImmutableList.of(Part.fromText(prompt)))
                .build()
        );

        GenerateContentConfig config = GenerateContentConfig.builder()
            .responseModalities(ImmutableList.of("TEXT"))
            .build();

        ResponseStream<GenerateContentResponse> responseStream = client.models.generateContentStream(model, contents, config);

        StringBuilder result = new StringBuilder();
        for (GenerateContentResponse res : responseStream) {
            List<Part> parts = res.candidates().get().get(0).content().get().parts().get();
            for (Part part : parts) {
                if (part.text() != null) {
                    result.append(part.text());
                }
            }
        }

        responseStream.close();
        return result.toString();

    } catch (Exception e) {
        e.printStackTrace();
        return "Error generating proof.";
    }
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
