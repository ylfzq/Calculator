package tech.zhouqian.expression.calculator;

import java.io.PrintStream;

/* package */ abstract class CalcUnit {

  public PrintStream calcStepStream = null;

  public final String name;
  public final int level;

  public CalcUnit(String name, int level) {
    this.name = name;
    this.level = level;
  }

  public final Number calc(Number... vals) {
    Number result = doCalc(vals);
    if (calcStepStream != null) {
      calcStepStream.println(doLogAfterCalc(result, vals));
    }
    return result;
  }

  public abstract Number doCalc(Number... vals);

  public abstract String doLogAfterCalc(Number result, Number... vals);

  @Override
  public String toString() {
    return name;
  }
}
