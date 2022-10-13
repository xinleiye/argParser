package tdd.args;

import tdd.args.exceptions.IllegalValueException;
import tdd.args.exceptions.InsufficientArgumentsException;
import tdd.args.exceptions.TooManyArgumentsException;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;

class SingleValueOptionParser<T> implements OptionParser<T> {
    Function<String, T> valueParser;
    T defaultValue;

    public SingleValueOptionParser(T defaultValue, Function<String, T> valueParser) {
        this.defaultValue = defaultValue;
        this.valueParser = valueParser;
    }

    public static <T> SingleValueOptionParser<T> createSingleValueOptionPaser(Function<String, T> valueParser, T defaultValue) {
        return new SingleValueOptionParser<T>(defaultValue, valueParser);
    }

    @Override
    public T parse(List<String> arguments, Option option) {
        return values(arguments, option, 1).map(it -> parseValue(option, it.get(0))).orElse(defaultValue);
    }

    static Optional<List<String>> values(List<String> arguments, Option option, int expectedSize) {
        Optional<List<String>> argumentList;
        int index = arguments.indexOf("-" + option.value());
        if (index == -1) {
            return Optional.empty();
        }
        List<String> values = values(arguments, index);

        if (values.size() < expectedSize) {
            throw new InsufficientArgumentsException(option.value());
        }
        if (values.size() > expectedSize) {
            throw new TooManyArgumentsException(option.value());
        }

        return Optional.of(values);
    }

    private T parseValue(Option option, String value) throws IllegalValueException {
        try {
            return valueParser.apply(value);
        } catch (Exception e) {
            throw new IllegalValueException(option.value(), value);
        }
    }

    static List<String> values(List<String> arguments, int index) {
        return arguments.subList(index + 1, IntStream.range(index + 1, arguments.size())
                .filter(it -> arguments.get(it).startsWith("-"))
                .findFirst().orElse(arguments.size()));
    }
}
