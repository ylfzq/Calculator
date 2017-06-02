package tech.zhouqian.expression.calculator;

/* package */ final class PlaceholderCalcUnit extends CalcUnit {

  public PlaceholderCalcUnit(String name, int level) {
    super(name, level);
  }

  @Override
  public Number doCalc(Number... vals) {
    return null;
  }

  @Override
  public String doLogAfterCalc(Number result, Number... vals) {
    return null;
  }
}
