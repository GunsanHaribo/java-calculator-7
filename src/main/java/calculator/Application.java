package calculator;

import camp.nextstep.edu.missionutils.Console;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class Application {
    private static final String COMMA = ",";
    private static final String SEMICOLON = ";";
    private static final String COMMA_AND_SEMICOLON = COMMA + "|" + SEMICOLON;
    private static final String EMPTY = "";
    private static final String REX_ONLY_NUMBER = "\\d+";
    private static final String CUSTOM_DELIMITER_START = "//";
    private static final String CUSTOM_DELIMITER_END = "\\\\n";
    private static final String REX_CUSTOM_DELIMITER_FORMAT = ".*//(.+)\\\\n.*";
    private static final String ERROR_NO_EXPRESSION_CUSTOM_DELIMITER = "커스텀 구분자 이후로 수식이 없습니다";
    private static final String ERROR_CUSTOM_DELIMITER_OVER_TWO = "커스텀 구분자가 2개이상 있습니다";
    private static final String ERROR_CUSTOM_DELIMITER_NOT_START_WITH_FORMAT = "커스텀 구분자의 형식인 //로 시작하지 않습니다";
    private static final String ERROR_CUSTOM_DELIMITER_OPERANDS_CONTAIN_OTHER_CHAR = "피연산자에 커스텀 구분자가 아닌 문자가 있습니다";
    private static final String ERROR_CUSTOM_DELIMITER_CONTAIN_NUMBER = "구분자에 숫자가 들어있습니다";
    private static final String ERROR_NOT_CONTAIN_DELIMITER = "구분자에 숫자가 들어있습니다";
    private static final String ERROR_BASIC_DELIMITER_OPERAND_CONTAIN_OTHER_CHAR = "피연산자에 숫자가 아닌 다른 문자가 있습니다";
    private static final String ERROR_BASIC_DELIMITER_PARSING_PROBLEM = "파싱시 알 수 없는 에러가 있습니다";
    private static final String ERROR_BASIC_DELIMITER_CONTAIN_ZERO = "0을 포함합니다";
    private static final String ERROR_BASIC_DELIMITER_CONTAIN_MINUS = "음수를 포함합니다";
    private static final String ERROR_CUSTOM_DELIMITER_CONTAIN_ZERO = "커스텀 구분자부분의 피연산자에서 0을 포함합니다";
    private static final String ERROR_CUSTOM_DELIMITER_CONTAIN_MINUS = "커스텀 구분자부분의 피연산자에서 음수를 포함합니다";
    private static final String ERROR_BASIC_DELIMITER_ONLY_DELIMITER_SUPPORT = " 밖에 없습니다";


    public static void main(String[] args) {
        String input = Console.readLine();
        BigInteger result = add(input);
        System.out.println("결과 : " + result);
    }

    private static BigInteger add(String input) {
        boolean isNotContainingDelimiter = !input.contains(COMMA) && !input.contains(SEMICOLON);

        if (input.isEmpty()) {
            return BigInteger.valueOf(0);
        }
        if (input.matches(REX_CUSTOM_DELIMITER_FORMAT)) {
            List<String> customDelimiterInputs = Arrays.stream(input.split(CUSTOM_DELIMITER_END))
                    .filter(letter -> !letter.isBlank())
                    .toList();

            String expression = customDelimiterInputs.getLast();

            if (expression.contains(CUSTOM_DELIMITER_START)) {
                throw new IllegalArgumentException(ERROR_NO_EXPRESSION_CUSTOM_DELIMITER);
            }

            if (customDelimiterInputs.size() > 2) {
                throw new IllegalArgumentException(ERROR_CUSTOM_DELIMITER_OVER_TWO);
            }

            String firstToTwoLetter = customDelimiterInputs.getFirst().substring(0, 2);
            if (!firstToTwoLetter.equals(CUSTOM_DELIMITER_START)) {
                throw new IllegalArgumentException(ERROR_CUSTOM_DELIMITER_NOT_START_WITH_FORMAT);
            }

            String customDelimiter = customDelimiterInputs.getFirst().substring(2);
            try {
                new BigInteger(customDelimiter);
            } catch (NumberFormatException e) {
                List<BigInteger> operands;
                try {
                    operands = Arrays.stream(expression.split(Pattern.quote(customDelimiter)))
                            .map(String::trim)
                            .map(BigInteger::new)
                            .toList();
                } catch (NumberFormatException i) {
                    throw new IllegalArgumentException(ERROR_CUSTOM_DELIMITER_OPERANDS_CONTAIN_OTHER_CHAR);
                }

                validatePositive(operands, ERROR_CUSTOM_DELIMITER_CONTAIN_ZERO, ERROR_CUSTOM_DELIMITER_CONTAIN_MINUS);

                return operands.stream().reduce(BigInteger.valueOf(0), BigInteger::add);
            }
            throw new IllegalArgumentException(ERROR_CUSTOM_DELIMITER_CONTAIN_NUMBER);
        }

        if (isNotContainingDelimiter) {
            throw new IllegalArgumentException(ERROR_NOT_CONTAIN_DELIMITER);
        }

        String[] delimiters = {COMMA, SEMICOLON, COMMA_AND_SEMICOLON};
        for (String delimiter : delimiters) {
            validateOnlyDelimiter(input, delimiter);
        }

        List<String> stringInputs = Arrays.stream(input.split(COMMA_AND_SEMICOLON))
                .filter(letter -> !letter.equals(EMPTY))
                .map(String::trim)
                .toList();

        List<BigInteger> bigIntegerParsedInputs;
        try {
            bigIntegerParsedInputs = stringInputs.stream()
                    .map(BigInteger::new)
                    .toList();

        } catch (NumberFormatException e) {
            for (String stringInput : stringInputs) {
                boolean isNumeric = stringInput.matches(REX_ONLY_NUMBER);
                if (!isNumeric) {
                    throw new IllegalArgumentException(ERROR_BASIC_DELIMITER_OPERAND_CONTAIN_OTHER_CHAR);
                }
            }

            throw new IllegalArgumentException(ERROR_BASIC_DELIMITER_PARSING_PROBLEM);
        }

        validatePositive(bigIntegerParsedInputs, ERROR_BASIC_DELIMITER_CONTAIN_ZERO, ERROR_BASIC_DELIMITER_CONTAIN_MINUS);

        return bigIntegerParsedInputs.stream().reduce(BigInteger.valueOf(0), BigInteger::add);
    }

    private static void validatePositive(List<BigInteger> operands, String errorCustomDelimiterContainZero, String errorCustomDelimiterContainMinus) {
        for (BigInteger inputNumber : operands) {
            if (inputNumber.compareTo(BigInteger.ZERO) == 0) {
                throw new IllegalArgumentException(errorCustomDelimiterContainZero);
            }
            if (inputNumber.compareTo(BigInteger.ZERO) < 0) {
                throw new IllegalArgumentException(errorCustomDelimiterContainMinus);
            }
        }
    }

    private static void validateOnlyDelimiter(String input, String delimiter) {
        List<String> inputs = Arrays.stream(input.split(delimiter)).toList();
        if (inputs.isEmpty()) {
            throw new IllegalArgumentException(delimiter + ERROR_BASIC_DELIMITER_ONLY_DELIMITER_SUPPORT);
        }
    }

}
