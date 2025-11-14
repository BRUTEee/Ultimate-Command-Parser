// Command interface for all commands (polymorphism)
public interface Command {
    String execute(String[] args) throws ParserException;
}