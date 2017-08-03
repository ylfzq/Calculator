package tech.zhouqian.expression.calculator;

import tech.zhouqian.expression.parser.AbstractParser;
import tech.zhouqian.expression.parser.ExpressionTokenizer;
import tech.zhouqian.expression.parser.GroupParser;
import tech.zhouqian.expression.parser.IdentifierParser;
import tech.zhouqian.expression.parser.NumberParser;
import tech.zhouqian.expression.parser.OperatorParser;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * @author zhouqian
 * @since 20151103
 */
public final class Calculator {

  public static void main(String[] args) {
    for (String exp : args) {
      System.out.println(Calculator.getDefault().calcExpression(exp));
    }
  }


  private static final CalcUnit LEFT_BRACKET = new PlaceholderCalcUnit("(", -2);
  private static final CalcUnit COMMA = new PlaceholderCalcUnit(",", -1);

  public static Calculator getDefault() {
    return new Calculator(CalcUnitsImpl.DEFAULT);
  }

  private final HashMap<String, CalcUnit> mMap;
  // Operators or Functions or LEFT_BRACKET
  private final LinkedList<CalcUnit> mOpStack;
  // Number or LEFT_BRACKET or COMMA
  private final LinkedList<Object> mNumStack;

  public ExpressionTokenizer mTokenizer;
  private PrintStream mStackPrintStream;

  public Calculator(CalcUnit[] calcs) {
    mMap = new HashMap<>();
    mOpStack = new LinkedList<>();
    mNumStack = new LinkedList<>();

    addCalcUnit(calcs);
  }

  public Calculator addCalcUnit(CalcUnit[] calcs) {
    if (calcs != null) {
      for (CalcUnit calc : calcs) {
        mMap.put(calc.name, calc);
      }
    }
    mTokenizer = null;
    return this;
  }

  protected Calculator initTokenizer() {
    List<String> operatorList = new ArrayList<>();
    for (CalcUnit calc : mMap.values()) {
      if (calc instanceof OperatorCalcUnit) {
        operatorList.add(calc.name);
      }
    }
    mTokenizer = new ExpressionTokenizer(
        new OperatorParser(operatorList),
        new NumberParser(16),
        new IdentifierParser(32),
        new GroupParser(",()".toCharArray()));
    return this;
  }

  public Calculator setCalcStepPrintStream(PrintStream ps) {
    for (CalcUnit calc : mMap.values()) {
      calc.calcStepStream = ps;
    }
    return this;
  }

  public Calculator setStackPrintStream(PrintStream ps) {
    mStackPrintStream = ps;
    return this;
  }

  public Number calcExpression(String expression) {
    if (mTokenizer == null) {
      initTokenizer();
    }

    mOpStack.clear();
    mNumStack.clear();
    mTokenizer.setExpression(expression);
    AbstractParser parser;
    while ((parser = mTokenizer.next()) != null) {
      handleParser(parser);
      if (mStackPrintStream != null) {
        mStackPrintStream.println(mOpStack.toString());
        mStackPrintStream.println(mNumStack.toString());
        mStackPrintStream.println();
      }
    }
    if (mTokenizer.getPosition() < expression.length()) {
      throw new RuntimeException(String.format(
          "Unknown char(%c) at position(%d) in expression",
          expression.charAt(mTokenizer.getPosition()),
          mTokenizer.getPosition()));
    }
    calcStacksUtilLowerThanLevel(Integer.MIN_VALUE);
    if (mNumStack.size() == 0) {
      return null;
    } else if (mNumStack.size() > 1) {
      throw new RuntimeException("number stack has more than one value: "
          + mNumStack.toString());
    }

    return ((Number) mNumStack.pop());
  }

  protected void calcStacksUtilLowerThanLevel(int level) {
    CalcUnit opCalc = null;
    while (!mOpStack.isEmpty()) {
      opCalc = mOpStack.pop();
      if (opCalc.level < level) {
        mOpStack.push(opCalc);
        break;
      }

      if (opCalc instanceof SingleOperandOperatorCalcUnit) {
        throw new RuntimeException("internal error, cannot be single-operand operator here");
      } else if (opCalc instanceof TwoOperandsOperatorCalcUnit) {
        Number num1 = (Number) mNumStack.pop();
        Number num2 = (Number) mNumStack.pop();
        handleNumber(opCalc.calc(num2, num1));
      } else if (opCalc == LEFT_BRACKET) {
        if (mOpStack.peek() instanceof FunctionCalcUnit) {
          LinkedList<Number> list = new LinkedList<>();
          while (mNumStack.peek() instanceof Number) {
            list.add((Number) mNumStack.pop());
          }
          if (mNumStack.peek() == LEFT_BRACKET) {
            mNumStack.pop();
          }
          Collections.reverse(list);

          FunctionCalcUnit funCalc = (FunctionCalcUnit) mOpStack.pop();
          Number result = funCalc.calc(list.toArray(new Number[list.size()]));
          handleNumber(result);
        } else if (level == LEFT_BRACKET.level
            && mOpStack.peek() instanceof SingleOperandOperatorCalcUnit) {
          Number num = (Number) mNumStack.pop();
          if (num == null) {
            throw new RuntimeException(opCalc.name
                + "requires a operand after it.");
          }
          Number result = mOpStack.pop().calc(num);
          handleNumber(result);
        }
        break;
      } else {
        throw new RuntimeException("Not supported OperatorCalc: "
            + opCalc.name);
      }
    }
  }

  // FIXME cannot judge single-operand operator's position is correct or wrong
  protected void handleParser(AbstractParser parser) {
    final String str = parser.toString();
    if (parser instanceof OperatorParser) {
      OperatorCalcUnit opCalc = (OperatorCalcUnit) mMap.get(str);
      if (opCalc == null) {
        throw new RuntimeException("not supported operator: "
            + parser.toString());
      }
      handleOperatorCalc(opCalc);
    } else if (parser instanceof NumberParser) {
      double val = Double.parseDouble(str);
      handleNumber(val);
    } else if (parser instanceof IdentifierParser) {
      CalcUnit calc = mMap.get(str);
      if (calc instanceof ConstantCalcUnit) {
        handleNumber(calc.calc());
      } else if (calc instanceof FunctionCalcUnit) {
        mOpStack.push(calc);
        mNumStack.push(LEFT_BRACKET);
      } else {
        throw new RuntimeException("Unknown identifier: " + str);
      }
    } else if (parser instanceof GroupParser) {
      if (",".equals(str)) {
        calcStacksUtilLowerThanLevel(COMMA.level);
      } else if ("(".equals(str)) {
        mOpStack.push(LEFT_BRACKET);
      } else if (")".equals(str)) {
        calcStacksUtilLowerThanLevel(LEFT_BRACKET.level);
      }
    }
  }

  protected void handleOperatorCalc(OperatorCalcUnit opCalc) {
    if (opCalc instanceof SingleOperandOperatorCalcUnit) {
      if (((SingleOperandOperatorCalcUnit) opCalc).isLeftOperand) {
        Number num = (Number) mNumStack.pop();
        if (num == null) {
          throw new RuntimeException(opCalc.name
              + "requires a operand before it.");
        }
        Number result = opCalc.calc(num);
        handleNumber(result);
      } else {
        mOpStack.push(opCalc);
      }
    } else if (opCalc instanceof TwoOperandsOperatorCalcUnit) {
      CalcUnit calc = mOpStack.peek();
      if (calc == null) {
        mOpStack.push(opCalc);
      } else if (calc instanceof SingleOperandOperatorCalcUnit) {
        throw new RuntimeException("internal error, cannot be single-operand operator here");
      } else if (calc instanceof TwoOperandsOperatorCalcUnit) {
        if (((TwoOperandsOperatorCalcUnit) opCalc).level <= ((TwoOperandsOperatorCalcUnit) calc).level) {
          calcStacksUtilLowerThanLevel(((TwoOperandsOperatorCalcUnit) opCalc).level);
        }
        mOpStack.push(opCalc);
      } else if (calc == LEFT_BRACKET) {
        mOpStack.push(opCalc);
      } else {
        throw new RuntimeException(String.format(
            "syntax error, operator(%s) cannot exist here after identifier(%s)",
            opCalc.name, calc.name));
      }
    } else {
      throw new RuntimeException("Not supported OperatorCalc: "
          + opCalc.name);
    }
  }

  protected void handleNumber(Number val) {
    CalcUnit calc = mOpStack.peek();
    if (calc == null) {
      mNumStack.push(val);
    } else if (calc instanceof OperatorCalcUnit) {
      if (calc instanceof SingleOperandOperatorCalcUnit) {
        if (((SingleOperandOperatorCalcUnit) calc).isLeftOperand) {
          throw new RuntimeException("internal error, cannot be left-operand operator here");
        } else {
          mOpStack.pop();
          handleNumber(calc.calc(val));
        }
      } else if (calc instanceof TwoOperandsOperatorCalcUnit) {
        mNumStack.push(val);
      } else if (calc == LEFT_BRACKET) {
        mNumStack.push(val);
      } else {
        throw new RuntimeException("Not supported OperatorCalc: "
            + calc.name);
      }
    } else {
      mNumStack.push(val);
    }
  }
}
