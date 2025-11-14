import java.util.ArrayList;
import java.util.StringJoiner;

// Implementation for 'history' command
public class HistoryCommand implements Command {
    private ArrayList<String> history;

    public HistoryCommand(ArrayList<String> history) {
        this.history = history;
    }

    @Override
    public String execute(String[] args) throws ParserException {
        if (args.length > 0) {
            throw new ParserException("history takes no arguments");
        }
        if (history.isEmpty()) {
            return "No command history yet.";
        }
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("Command History:");
        for (int i = 0; i < history.size(); i++) {
            joiner.add((i + 1) + ": " + history.get(i));
        }
        return joiner.toString();
    }
}