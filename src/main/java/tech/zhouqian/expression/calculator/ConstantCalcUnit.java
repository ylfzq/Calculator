package tech.zhouqian.expression.calculator;

public final class ConstantCalcUnit extends CalcUnit {

  protected Number number;

  public ConstantCalcUnit(String name, Number num) {
    super(name, 0);
    this.number = num;
  }

  @Override
  public Number doCalc(Number[] vals) {
    return number;
  }

  @Override
  public String doLogAfterCalc(Number result, Number[] vals) {
    return name + "=" + number;
  }
}
