package tech.zhouqian.expression.calculator;

public final class CalcUnitsImpl {

  public static final int CALC_LEVEL_PLUS_MINUS = 1;
  public static final int CALC_LEVEL_MULTIPLY_DIVID = 2;

  public static final CalcUnit PI = new ConstantCalcUnit("PI", Math.PI);

  public static final CalcUnit OPERATOR_FACTORIAL = new SingleOperandOperatorCalcUnit("!", true) {

    @Override
    public Number doCalc(Number val) {
      double mul = 1;
      int a = val.intValue();
      for (int i = 2; i <= a; ++i) {
        mul *= i;
      }
      return mul;
    }
  };

  public static final CalcUnit OPERATOR_SQRT = new SingleOperandOperatorCalcUnit("~", false) {

    @Override
    public Number doCalc(Number val) {
      return Math.sqrt(val.doubleValue());
    }
  };

  public static final CalcUnit OPERATOR_PLUS_PLUS = new SingleOperandOperatorCalcUnit("++", true) {

    @Override
    public Number doCalc(Number val) {
      if (val instanceof Integer || val instanceof Long) {
        return val.longValue() + 1;
      } else if (val instanceof Float || val instanceof Double) {
        return val.doubleValue() + 1;
      } else {
        return val.doubleValue() + 1;
      }
    }
  };

  public static final CalcUnit OPERATOR_PLUS = new TwoOperandsOperatorCalcUnit("+", CALC_LEVEL_PLUS_MINUS) {

    @Override
    public Number doCalc(Number val1, Number val2) {
      return val1.doubleValue() + val2.doubleValue();
    }
  };

  public static final CalcUnit OPERATOR_MINUS = new TwoOperandsOperatorCalcUnit("-", CALC_LEVEL_PLUS_MINUS) {

    @Override
    public Number doCalc(Number val1, Number val2) {
      return val1.doubleValue() - val2.doubleValue();
    }
  };

  public static final CalcUnit OPERATOR_MULTIPLY = new TwoOperandsOperatorCalcUnit("*", CALC_LEVEL_MULTIPLY_DIVID) {

    @Override
    public Number doCalc(Number val1, Number val2) {
      return val1.doubleValue() * val2.doubleValue();
    }
  };

  public static final CalcUnit OPERATOR_DIVID = new TwoOperandsOperatorCalcUnit("/", CALC_LEVEL_MULTIPLY_DIVID) {

    @Override
    public Number doCalc(Number val1, Number val2) {
      return val1.doubleValue() / val2.doubleValue();
    }
  };

  public static final CalcUnit FUNCTION_MAX = new FunctionCalcUnit("max") {

    @Override
    public Number doCalc(Number[] vals) {
      Number maxNumber = vals[0];
      if (vals.length == 1) {
        return maxNumber;
      }

      for (int i = 1; i < vals.length; ++i) {
        if (vals[i].doubleValue() > maxNumber.doubleValue()) {
          maxNumber = vals[i];
        }
      }
      return maxNumber;
    }
  };

  public static final CalcUnit FUNCTION_MIN = new FunctionCalcUnit("min") {

    @Override
    public Number doCalc(Number[] vals) {
      Number minNumber = vals[0];
      if (vals.length == 1) {
        return minNumber;
      }

      for (int i = 1; i < vals.length; ++i) {
        if (vals[i].doubleValue() < minNumber.doubleValue()) {
          minNumber = vals[i];
        }
      }
      return minNumber;
    }
  };

  public static final CalcUnit[] DEFAULT = new CalcUnit[]{OPERATOR_PLUS,
      OPERATOR_MINUS, OPERATOR_MULTIPLY, OPERATOR_DIVID,
      OPERATOR_FACTORIAL, OPERATOR_SQRT, OPERATOR_PLUS_PLUS, PI,
      FUNCTION_MAX, FUNCTION_MIN};
}
