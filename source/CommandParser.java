import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Scanner;

// Main class: Sets up Swing GUI and CLI thread (hybrid)
public class CommandParser {
    private static Parser parser = new Parser();
    private static JTextArea outputArea = new JTextArea();
    private static OutputHandler outputHandler = new OutputHandler(outputArea);
    private static JFrame frame;
    private static JTextField inputField;
    private static ArrayList<String> history;
    private static int historyIndex = -1;  // For navigating history

    public static void main(String[] args) {
        // Set up GUI on Event Dispatch Thread
        SwingUtilities.invokeLater(() -> setupGUI());

        // Start CLI thread (multithreading)
        Thread cliThread = new Thread(() -> runCLI());
        cliThread.start();
    }

    // Set up Swing GUI (uses BorderLayout, FlowLayout)
    private static void setupGUI() {
        frame = new JFrame("Ultimate Command Parser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // Remove system look and feel to ensure custom colors apply consistently
        // try {
        //     UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }

        // North: Input panel with label for better UX
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        inputPanel.setBackground(new Color(200, 220, 255));  // Soft blue for input area
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Padding
        JLabel inputLabel = new JLabel("Enter Command:");
        inputLabel.setFont(new Font("SansSerif", Font.BOLD, 16));  // Larger, bold font
        inputLabel.setForeground(Color.BLACK);  // Ensure visible
        inputField = new JTextField(50);
        inputField.setFont(new Font("Monospaced", Font.PLAIN, 16));  // Larger font for input
        inputField.setBackground(Color.WHITE);
        inputField.setForeground(Color.BLACK);
        inputField.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));  // Subtle border
        JButton executeButton = new JButton("Execute");
        executeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        executeButton.setBackground(new Color(100, 180, 100));  // Green for execute
        executeButton.setForeground(Color.WHITE);
        executeButton.setPreferredSize(new Dimension(100, 30));  // Larger button
        executeButton.setBorder(BorderFactory.createRaisedBevelBorder());  // 3D effect
        inputPanel.add(inputLabel);
        inputPanel.add(inputField);
        inputPanel.add(executeButton);
        frame.add(inputPanel, BorderLayout.NORTH);

        // Center: Output area (read-only) with colors and larger font
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Monospaced", Font.PLAIN, 18));  // Increased font size for better vibe
        outputArea.setBackground(new Color(30, 30, 30));  // Dark background for console-like feel
        outputArea.setForeground(Color.WHITE);  // White for visibility
        outputArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Padding
        JScrollPane scrollPane = new JScrollPane(outputArea);  // Add scrollbars
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(100, 100, 100)));
        frame.add(scrollPane, BorderLayout.CENTER);

        // South: Buttons for common commands with colors
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(200, 220, 255));  // Matching soft blue
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));  // Padding
        JButton helpButton = new JButton("Help");
        helpButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        helpButton.setBackground(new Color(70, 130, 180));  // Steel blue
        helpButton.setForeground(Color.WHITE);
        helpButton.setPreferredSize(new Dimension(100, 30));  // Larger button
        helpButton.setBorder(BorderFactory.createRaisedBevelBorder());
        
        JButton dateButton = new JButton("Date");
        dateButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        dateButton.setBackground(new Color(70, 130, 180));
        dateButton.setForeground(Color.WHITE);
        dateButton.setPreferredSize(new Dimension(100, 30));  // Larger button
        dateButton.setBorder(BorderFactory.createRaisedBevelBorder());
        
        JButton timeButton = new JButton("Time");
        timeButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        timeButton.setBackground(new Color(70, 130, 180));
        timeButton.setForeground(Color.WHITE);
        timeButton.setPreferredSize(new Dimension(100, 30));  // Larger button
        timeButton.setBorder(BorderFactory.createRaisedBevelBorder());
        
        JButton historyButton = new JButton("History");  // New button for history
        historyButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        historyButton.setBackground(new Color(70, 130, 180));
        historyButton.setForeground(Color.WHITE);
        historyButton.setPreferredSize(new Dimension(100, 30));  // Larger button
        historyButton.setBorder(BorderFactory.createRaisedBevelBorder());
        
        JButton exitButton = new JButton("Exit");
        exitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        exitButton.setBackground(new Color(200, 50, 50));  // Red for exit
        exitButton.setForeground(Color.WHITE);
        exitButton.setPreferredSize(new Dimension(100, 30));  // Larger button
        exitButton.setBorder(BorderFactory.createRaisedBevelBorder());
        
        buttonPanel.add(helpButton);
        buttonPanel.add(dateButton);
        buttonPanel.add(timeButton);
        buttonPanel.add(historyButton);
        buttonPanel.add(exitButton);
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Event listeners
        executeButton.addActionListener(e -> handleGUIInput(inputField.getText()));
        helpButton.addActionListener(e -> handleGUIInput("help"));
        dateButton.addActionListener(e -> handleGUIInput("date"));
        timeButton.addActionListener(e -> handleGUIInput("time"));
        historyButton.addActionListener(e -> handleGUIInput("history"));
        exitButton.addActionListener(e -> {
            outputHandler.output("Exiting...");
            System.exit(0);
        });

        // Command history navigation with up/down arrows
        history = parser.getHistory();
        inputField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleGUIInput(inputField.getText());
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    navigateHistory(true);  // Up: previous
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    navigateHistory(false);  // Down: next
                }
            }
        });

        // Close window handling (already handled by defaultCloseOperation, but add message)
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                outputHandler.output("Exiting via window close...");
            }
        });

        // Maximize fix: Set after window is opened
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
            }
        });

        frame.pack();
        frame.setVisible(true);
    }

    // Handle GUI input: Add to history, check for exit, execute, clear field
    private static void handleGUIInput(String input) {
        if (parser.isExit(input)) {
            outputHandler.output("Exiting...");
            System.exit(0);
            return;
        }
        if (!input.trim().isEmpty()) {
            parser.addToHistory(input);
            historyIndex = -1;  // Reset index after new command
        }
        String result = parser.executeCommand(input);
        outputHandler.output(result);
        inputField.setText("");
        inputField.requestFocus();  // Refocus on input
    }

    // Navigate history with arrows
    private static void navigateHistory(boolean up) {
        if (history.isEmpty()) return;
        if (up) {
            if (historyIndex < history.size() - 1) {
                historyIndex++;
            }
        } else {
            if (historyIndex > 0) {
                historyIndex--;
            } else {
                historyIndex = -1;
                inputField.setText("");
                return;
            }
        }
        if (historyIndex >= 0 && historyIndex < history.size()) {
            inputField.setText(history.get(history.size() - 1 - historyIndex));
        }
    }

    // CLI loop (runs in separate thread)
    private static void runCLI() {
        try (Scanner scanner = new Scanner(System.in)) {
            outputHandler.output("Welcome to Ultimate Command Parser!\nEnter commands (type 'exit' to quit):");
            while (true) {
                System.out.print("> ");  // Prompt in console
                String input = scanner.nextLine();
                if (parser.isExit(input)) {
                    outputHandler.output("Exiting...");
                    System.exit(0);
                }
                if (!input.trim().isEmpty()) {
                    parser.addToHistory(input);
                }
                String result = parser.executeCommand(input);
                outputHandler.output(result);
            }
        }
    }
}