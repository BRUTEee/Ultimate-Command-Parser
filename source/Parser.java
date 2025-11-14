import java.util.HashMap;
import java.util.ArrayList;

// Parser class: Handles command registration and execution (uses HashMap for dynamic commands)
public class Parser {
    private HashMap<String, Command> commands = new HashMap<>();  // Generics: String key, Command value
    private ArrayList<String> history = new ArrayList<>();  // Command history (generics)

    public Parser() {
        // Register commands (easy to add more)
        commands.put("help", new HelpCommand());
        commands.put("date", new DateCommand());
        commands.put("time", new TimeCommand());
        commands.put("echo", new EchoCommand());
        commands.put("reverse", new ReverseCommand());
        commands.put("calc", new CalcCommand());
        commands.put("history", new HistoryCommand(history));
    }

    // Parse and execute input
    public String executeCommand(String input) {
        if (input.trim().isEmpty()) {
            return "";
        }
        String[] parts = input.split("\\s+");  // Split by whitespace
        String cmdName = parts[0].toLowerCase();
        String[] args = new String[parts.length - 1];
        System.arraycopy(parts, 1, args, 0, args.length);

        Command cmd = commands.get(cmdName);
        if (cmd == null) {
            return "Invalid command: " + cmdName + ". Type 'help' for list.";
        }
        try {
            return cmd.execute(args);
        } catch (ParserException e) {
            return "Error: " + e.getMessage();
        }
    }

    // Add to history
    public void addToHistory(String input) {
        if (!input.trim().isEmpty() && !input.trim().equalsIgnoreCase("history")) {
            history.add(input.trim());
        }
    }

    // Get history for navigation
    public ArrayList<String> getHistory() {
        return history;
    }

    // Check if command is 'exit'
    public boolean isExit(String input) {
        String trimmed = input.trim().toLowerCase();
        return trimmed.equals("exit") || trimmed.startsWith("exit ");
    }
}