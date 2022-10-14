package tdd.args;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import tdd.args.exceptions.TooManyArgumentsException;
import tdd.args.exceptions.InsufficientArgumentsException;

import java.lang.annotation.Annotation;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;
import static tdd.args.OptionParsersTest.BooleanOptionParserTest.option;
import static java.util.Arrays.asList;

public class OptionParsersTest {
    @Nested
    class UnaryOptionParser {
        @Test
        public void should_not_accept_extra_argument_for_single_value_option() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                OptionParsers.unary(0, Integer::parseInt).parse(asList("-p", "8080", "8081"), option("p"));
            });

            assertEquals("p", e.getOption());
        }

        @ParameterizedTest
        @ValueSource(strings = {"-p -l", "-p"})
        public void should_not_accept_insufficient_argument_for_single_value_option(String arguments) {
            InsufficientArgumentsException e = assertThrows(InsufficientArgumentsException.class, () -> {
                OptionParsers.unary(0, Integer::parseInt).parse(asList(arguments.split(" ")), option("p"));
            });

            assertEquals("p", e.getOption());
        }

        @Test
        public void should_set_default_value_to_zero_for_int_option() {
            Function<String, Object> whatever = (it) -> null;
            Object defaultValue = new Object();
            assertSame(defaultValue, OptionParsers.unary(defaultValue, whatever).parse(asList(), option("p")));
        }

        @Test
        public void should_parse_value_if_flag_present() {
            Object parsed = new Object();
            Function<String, Object> parse = (it) -> parsed;
            Object whatever = new Object();
            assertSame(parsed, OptionParsers.unary(whatever, parse).parse(asList("-p", "8080"), option("p")));
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

    @Nested
    class BooleanOptionParserTest {
        @Test
        public void should_not_accept_extra_argument_for_boolean_option() {
            TooManyArgumentsException e = assertThrows(TooManyArgumentsException.class, () -> {
                OptionParsers.bool().parse(asList("-l", "t"), option("l"));
            });
        }

        @Test
        public void should_set_default_value_to_true_if_option_present() {
            assertTrue(OptionParsers.bool().parse(asList("-l"), option("l")));
        }

        @Test
        public void should_set_default_value_to_false_if_option_not_present() {
            assertFalse(OptionParsers.bool().parse(asList(), option("l")));
        }

        static Option option(String value) {
            return new Option() {
                @Override
                public Class<? extends Annotation> annotationType() {
                    return Option.class;
                }

                @Override
                public String value() {
                    return value;
                }
            };
        }
    }
}