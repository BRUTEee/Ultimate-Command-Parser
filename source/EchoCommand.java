import java.util.StringJoiner;

// Implementation for 'echo' command
public class EchoCommand implements Command {
    @Override
    public String execute(String[] args) throws ParserException {
        if (args.length == 0) {
            throw new ParserException("echo requires at least one argument");
        }
        StringJoiner joiner = new StringJoiner(" ");
        for (String arg : args) {
            joiner.add(arg);
        }
        return joiner.toString();
    }
}