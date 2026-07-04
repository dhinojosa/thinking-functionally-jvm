package com.evolutionnext.sealed.functional;

public class Evaluator {
    static int evaluate(Expression expression) {
        return switch (expression) {
            case Constant c -> c.number();
            case Sum s -> evaluate(s.left()) + evaluate(s.right());
            case Subtract s -> evaluate(s.left()) - evaluate(s.right());
            case Multiply s -> evaluate(s.left()) * evaluate(s.right());
        };
    }

    static int evaluateRecordPatternMatch(Expression expression) {
        return switch (expression) {
            case Constant(var n) -> n;
            case Sum(var left, var right) -> evaluateRecordPatternMatch(left) + evaluateRecordPatternMatch(right);
            case Subtract(var left, var right) -> evaluateRecordPatternMatch(left) - evaluateRecordPatternMatch(right);
            case Multiply(var left, var right) -> evaluateRecordPatternMatch(left) * evaluateRecordPatternMatch(right);
        };
    }

    static String evaluateRecordPatternAsString(Expression expression) {
        return switch (expression) {
            case Constant(var n) -> String.format("%d", n);
            case Sum(var left, var right) -> String.format("(%s + %s)", evaluateRecordPatternAsString(left), evaluateRecordPatternAsString(right));
            case Subtract(var left, var right) -> String.format("(%s - %s)", evaluateRecordPatternAsString(left), evaluateRecordPatternAsString(right));
            case Multiply(var left, var right) -> String.format("(%s * %s)", evaluateRecordPatternAsString(left), evaluateRecordPatternAsString(right));
        };
    }
}
