package tech.zhouqian.expression.calculator;

public abstract class FunctionCalcUnit extends CalcUnit {

  public FunctionCalcUnit(String name) {
    super(name, 0);
  }

  @Override
  public String doLogAfterCalc(Number result, Number[] vals) {
    StringBuilder sb = new StringBuilder(name);
    for (int i = 0; i < vals.length; ++i) {
      if (i == 0) {
        sb.append("(");
      }
      sb.append(vals[i]);
      if (i != vals.length - 1) {
        sb.append(",");
      } else {
        sb.append(")=");
      }
    }
    sb.append(result);
    return sb.toString();
  }
}
