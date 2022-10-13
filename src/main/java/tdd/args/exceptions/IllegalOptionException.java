package tdd.args.exceptions;

public class IllegalOptionException extends RuntimeException {
    private String option;

    public IllegalOptionException(String option) {
        this.option = option;
    }

    public String getOption() {
        return option;
    }
}
