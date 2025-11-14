import java.util.Date;

// Implementation for 'date' command
public class DateCommand implements Command {
    @Override
    public String execute(String[] args) throws ParserException {
        if (args.length > 0) {
            throw new ParserException("date takes no arguments");
        }
        return new Date().toString();  // Current date and time
    }
}