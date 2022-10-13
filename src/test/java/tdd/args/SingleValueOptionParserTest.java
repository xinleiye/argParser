package tdd.args;

import org.junit.jupiter.api.Test;
//import org.junit.jupiter;

import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static tdd.args.BooleanOptionParserTest.option;
import static java.util.Arrays.asList;

public class SingleValueOptionParserTest {
    @Test
    public void should_not_accept_extra_argument_for_single_value_option() {
        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
            new SingleValueOptionParser<>(0, Integer::parseInt).parse(asList("-p", "8080", "8081"), option("p"));
        });

        assertEquals("p", e.getOption());
    }

//    @ParameterizedTest
//    @ValueSource(string = {"-p -l", "-p"})
//    public void should_not_accept_insufficient_argument_for_single_value_option(String arguments) {
//        InsufficientArgumentsException e = assertThrows(InsufficientArgumentsException.class, () -> {
//            new SingleValueOptionParser<>(Integer::parseInt).parse(asList(arguments.split(" ")), option("p"));
//        });
//
//        assertEquals("p", e.getOption());
//    }

    @Test
    public void should_set_default_value_to_zero_for_int_option() {
        Function<String, Object> whatever = (it) -> null;
        Object defaultValue = new Object();
        assertSame(defaultValue, new SingleValueOptionParser<>(defaultValue, whatever).parse(asList(), option("p")));
    }

    @Test
    public void should_parse_value_if_flag_present() {
        Object parsed = new Object();
        Function<String, Object> parse = (it) -> parsed;
        Object whatever = new Object();
        assertSame(parsed, new SingleValueOptionParser<>(whatever, parse).parse(asList("-p", "8080"), option("p")));
    }
//    @Test
//    public void should_not_accept_extra_argument_for_string_single_value_option() {
//        TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
//            new SingleValueOptionParser<>("", String::valueOf).parse(asList("-d", "/user/logs", "/usr/vars"), option("d"));
//        });
//
//        assertEquals("d", e.getOption());
//    }
}