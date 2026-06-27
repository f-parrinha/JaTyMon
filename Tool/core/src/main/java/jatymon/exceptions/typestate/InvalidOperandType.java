package jatymon.exceptions.typestate;

public class InvalidOperandType extends RuntimeException {
    public static final String MESSAGE = "Invalid operand type was given. Operand must either be an ID or a NUMBER";
    public InvalidOperandType() {
        super(MESSAGE);
    }
}
