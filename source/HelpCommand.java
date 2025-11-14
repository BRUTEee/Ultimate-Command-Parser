import java.util.StringJoiner;

// Implementation for 'help' command (updated to include history)
public class HelpCommand implements Command {
    @Override
    public String execute(String[] args) throws ParserException {
        if (args.length > 0) {
            throw new ParserException("help takes no arguments");
        }
        StringJoiner joiner = new StringJoiner("\n");
        joiner.add("Available commands:");
        joiner.add("help - list all commands");
        joiner.add("date - show current date & time");
        joiner.add("time - show current time");
        joiner.add("echo <text> - print text");
        joiner.add("reverse <text> - print reversed text");
        joiner.add("calc <expression> - calculate simple arithmetic (e.g., 5 + 3)");
        joiner.add("history - show past commands");
        joiner.add("exit - close the program");
        return joiner.toString();
    }
}