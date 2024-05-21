package cn.hutool.core.math;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import com.xiaopeng.commonfunc.Constant;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Stack;
/* loaded from: classes.dex */
public class Calculator {
    private final Stack<String> postfixStack = new Stack<>();
    private final Stack<Character> opStack = new Stack<>();
    private final int[] operatPriority = {0, 3, 2, 1, -1, 1, 0, 2};

    public static double conversion(String expression) {
        Calculator cal = new Calculator();
        return cal.calculate(transform(expression));
    }

    private static String transform(String expression) {
        char[] arr = StrUtil.removeSuffix(StrUtil.cleanBlank(expression), Constant.EQUALS_STRING).toCharArray();
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == '-') {
                if (i == 0) {
                    arr[i] = '~';
                } else {
                    char c = arr[i - 1];
                    if (c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == 'E' || c == 'e') {
                        arr[i] = '~';
                    }
                }
            }
        }
        if (arr[0] == '~' || (arr.length > 1 && arr[1] == '(')) {
            arr[0] = CharPool.DASHED;
            return "0" + new String(arr);
        }
        return new String(arr);
    }

    public double calculate(String expression) {
        Stack<String> resultStack = new Stack<>();
        prepare(expression);
        Collections.reverse(this.postfixStack);
        while (!this.postfixStack.isEmpty()) {
            String currentValue = this.postfixStack.pop();
            if (!isOperator(currentValue.charAt(0))) {
                resultStack.push(currentValue.replace("~", "-"));
            } else {
                String secondValue = resultStack.pop();
                String firstValue = resultStack.pop();
                BigDecimal tempResult = calculate(firstValue.replace("~", "-"), secondValue.replace("~", "-"), currentValue.charAt(0));
                resultStack.push(tempResult.toString());
            }
        }
        return Double.parseDouble(resultStack.pop());
    }

    private void prepare(String expression) {
        this.opStack.push(',');
        char[] arr = expression.toCharArray();
        int currentIndex = 0;
        int count = 0;
        for (int i = 0; i < arr.length; i++) {
            char currentOp = arr[i];
            if (isOperator(currentOp)) {
                if (count > 0) {
                    this.postfixStack.push(new String(arr, currentIndex, count));
                }
                char peekOp = this.opStack.peek().charValue();
                if (currentOp == ')') {
                    while (this.opStack.peek().charValue() != '(') {
                        this.postfixStack.push(String.valueOf(this.opStack.pop()));
                    }
                    this.opStack.pop();
                } else {
                    while (currentOp != '(' && peekOp != ',' && compare(currentOp, peekOp)) {
                        this.postfixStack.push(String.valueOf(this.opStack.pop()));
                        peekOp = this.opStack.peek().charValue();
                    }
                    this.opStack.push(Character.valueOf(currentOp));
                }
                count = 0;
                currentIndex = i + 1;
            } else {
                count++;
            }
        }
        if (count > 1 || (count == 1 && !isOperator(arr[currentIndex]))) {
            this.postfixStack.push(new String(arr, currentIndex, count));
        }
        while (this.opStack.peek().charValue() != ',') {
            this.postfixStack.push(String.valueOf(this.opStack.pop()));
        }
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '(' || c == ')' || c == '%';
    }

    public boolean compare(char cur, char peek) {
        if (cur == '%') {
            cur = '/';
        }
        if (peek == '%') {
            peek = '/';
        }
        int[] iArr = this.operatPriority;
        return iArr[peek + 65496] >= iArr[cur + 65496];
    }

    private BigDecimal calculate(String firstValue, String secondValue, char currentOp) {
        if (currentOp == '%') {
            BigDecimal result = NumberUtil.toBigDecimal(firstValue);
            return result.remainder(NumberUtil.toBigDecimal(secondValue));
        } else if (currentOp == '-') {
            BigDecimal result2 = NumberUtil.sub(firstValue, secondValue);
            return result2;
        } else if (currentOp == '/') {
            BigDecimal result3 = NumberUtil.div(firstValue, secondValue);
            return result3;
        } else if (currentOp == '*') {
            BigDecimal result4 = NumberUtil.mul(firstValue, secondValue);
            return result4;
        } else if (currentOp != '+') {
            throw new IllegalStateException("Unexpected value: " + currentOp);
        } else {
            BigDecimal result5 = NumberUtil.add(firstValue, secondValue);
            return result5;
        }
    }
}
