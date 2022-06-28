package tdd.args;

import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

public class Args {
    public static <T> T parse(Class<T> optionsClass, String... args) {
        try {
            List<String> arguments = Arrays.asList(args);
            Constructor<?> constructor = optionsClass.getDeclaredConstructors()[0];

            Object[] values = Arrays.stream(constructor.getParameters()).map(item -> parseOptions(arguments, item)).toArray();

            return (T) constructor.newInstance(values);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Object parseOptions(List<String> arguments, Parameter parameters) {
        Option option = parameters.getAnnotation(Option.class);
        Object value = null;

        if (parameters.getType() == boolean.class) {
            value = arguments.contains("-" + option.value());
        }
        if (parameters.getType() == int.class) {
            int index = arguments.indexOf("-" + option.value());
            value = Integer.parseInt(arguments.get(index + 1));
        }
        if (parameters.getType() == String.class) {
            int index = arguments.indexOf("-" + option.value());
            value = arguments.get(index + 1);
        }
        return value;
    }
}
