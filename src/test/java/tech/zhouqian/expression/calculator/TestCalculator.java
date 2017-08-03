package tech.zhouqian.expression.calculator;

import org.junit.Test;

/**
 * @author zhouqian
 * @since 20170510
 */
public class TestCalculator {

  @Test
  public void test() {
    System.out.println(
        Calculator.getDefault()
            .addCalcUnit(new CalcUnit[]{
                new FunctionCalcUnit("sqrt") {

                  @Override
                  public Number doCalc(Number[] vals) {
                    if (vals.length > 1) {
                      throw new IllegalArgumentException("sqrt() can only receive 1 param");
                    }

                    return Math.sqrt(vals[0].doubleValue());
                  }
                },
                new TwoOperandsOperatorCalcUnit("**", CalcUnitsImpl.CALC_LEVEL_MULTIPLY_DIVID + 1) {
                  @Override public Number doCalc(Number val1, Number val2) {
                    return Math.pow(val1.doubleValue(), val2.doubleValue());
                  }
                }
            })
            .calcExpression("2**3+min(1, 2, 3)")
    );
  }

  @Test
  public void test2() {
    System.out.println(Calculator.getDefault().calcExpression("min(1,-2)"));
  }
}
