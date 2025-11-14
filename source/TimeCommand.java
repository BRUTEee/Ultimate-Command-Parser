import java.util.Date;
import java.text.SimpleDateFormat;

// Implementation for 'time' command
public class TimeCommand implements Command {
    @Override
    public String execute(String[] args) throws ParserException {
        if (args.length > 0) {
            throw new ParserException("time takes no arguments");
        }
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        return sdf.format(new Date());  // Current time
    }
}