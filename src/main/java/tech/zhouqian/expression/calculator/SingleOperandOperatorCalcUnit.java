package tech.zhouqian.expression.calculator;

public abstract class SingleOperandOperatorCalcUnit extends OperatorCalcUnit {

  public final boolean isLeftOperand;

  public SingleOperandOperatorCalcUnit(String op, boolean isLeftOperand) {
    super(op, 0);
    this.isLeftOperand = isLeftOperand;
  }

  @Override
  public Number doCalc(Number... vals) {
    return doCalc(vals[0]);
  }

  @Override
  public String doLogAfterCalc(Number result, Number... vals) {
    if (isLeftOperand) {
      return vals[0] + name + "=" + result;
    } else {
      return name + vals[0] + "=" + result;
    }
  }

  public abstract Number doCalc(Number val);
}
