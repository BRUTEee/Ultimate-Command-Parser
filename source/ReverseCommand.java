import java.util.StringJoiner;

// Implementation for 'reverse' command
public class ReverseCommand implements Command {
    @Override
    public String execute(String[] args) throws ParserException {
        if (args.length == 0) {
            throw new ParserException("reverse requires at least one argument");
        }
        StringJoiner joiner = new StringJoiner(" ");
        for (String arg : args) {
            joiner.add(new StringBuilder(arg).reverse().toString());
        }
        return joiner.toString();
    }
}