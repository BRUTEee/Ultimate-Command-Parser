// Implementation for 'calc' command (simple arithmetic)
public class CalcCommand implements Command {
    @Override
    public String execute(String[] args) throws ParserException {
        if (args.length != 3) {
            throw new ParserException("calc requires exactly 3 parts: num1 op num2 (e.g., 5 + 3)");
        }
        try {
            double num1 = Double.parseDouble(args[0]);
            String op = args[1];
            double num2 = Double.parseDouble(args[2]);
            double result;
            switch (op) {
                case "+": result = num1 + num2; break;
                case "-": result = num1 - num2; break;
                case "*": result = num1 * num2; break;
                case "/": 
                    if (num2 == 0) throw new ParserException("Division by zero");
                    result = num1 / num2; 
                    break;
                default: throw new ParserException("Invalid operator: " + op);
            }
            return String.valueOf(result);
        } catch (NumberFormatException e) {
            throw new ParserException("Invalid numbers in expression");
        }
    }
}