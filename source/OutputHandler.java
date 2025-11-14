import java.awt.EventQueue;
import javax.swing.JTextArea;

// OutputHandler: Encapsulates output to console and GUI (uses EventQueue for thread-safety)
public class OutputHandler {
    private JTextArea outputArea;

    public OutputHandler(JTextArea outputArea) {
        this.outputArea = outputArea;
    }

    public void output(String message) {
        if (!message.isEmpty()) {
            System.out.println(message);  // Console output
            // GUI update (thread-safe)
            EventQueue.invokeLater(() -> {
                outputArea.append(message + "\n\n");  // Added extra newline for better readability
                outputArea.setCaretPosition(outputArea.getDocument().getLength());  // Auto-scroll to bottom
            });
        }
    }
}