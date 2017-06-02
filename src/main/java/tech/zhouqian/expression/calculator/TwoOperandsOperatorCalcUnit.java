package tech.zhouqian.expression.calculator;

public abstract class TwoOperandsOperatorCalcUnit extends OperatorCalcUnit {

  public TwoOperandsOperatorCalcUnit(String op, int level) {
    super(op, level);
  }

  @Override
  public Number doCalc(Number... vals) {
    return doCalc(vals[0], vals[1]);
  }

  @Override
  public String doLogAfterCalc(Number result, Number... vals) {
    return vals[0] + name + vals[1] + "=" + result;
  }

  public abstract Number doCalc(Number val1, Number val2);
}
