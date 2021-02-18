package sample;


import static org.jparsec.Scanners.isChar;

import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;

import org.jparsec.OperatorTable;
import org.jparsec.Parser;
import org.jparsec.Scanners;

public class EvalString{


    public static Double eval(String source) {
        return innerWorking(source);
    }
    public static Double innerWorking(String source){
        return parser().parse(source);
    }

    static final Parser<Double> NUMBER = Scanners.DECIMAL.map(Double::parseDouble);

    static final BinaryOperator<Double> PLUS = Double::sum;

    static final BinaryOperator<Double> MINUS = (a, b) -> a - b;

    static final BinaryOperator<Double> MUL = (a, b) -> a * b;

    static final BinaryOperator<Double> DIV = (a, b) -> a / b;

    static final BinaryOperator<Double> MOD = (a, b) -> a % b;

    static final UnaryOperator<Double> NEG = a -> -a;

    private static <T> Parser<T> op(char ch, T value) {
        return isChar(ch).retn(value);
    }

    static Parser<Double> parser() {
        Parser.Reference<Double> ref = Parser.newReference();
        Parser<Double> term = ref.lazy().between(isChar('('), isChar(')')).or(NUMBER);
        Parser<Double> parser = new OperatorTable<Double>()
                .prefix(op('-', NEG), 100)
                .infixl(op('+', PLUS), 10)
                .infixl(op('-', MINUS), 10)
                .infixl(op('*', MUL), 20)
                .infixl(op('/', DIV), 20)
                .infixl(op('%', MOD), 20)
                .build(term);
        ref.set(parser);
        return parser;
    }
}