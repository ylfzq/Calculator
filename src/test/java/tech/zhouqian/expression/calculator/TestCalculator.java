package tech.zhouqian.expression.calculator;

import org.junit.Test;

/**
 * @author zhouqian
 * @since 20170510
 */
public class TestCalculator {

  @Test
  public void test() {
    System.out.println(Calculator.getDefault()
        .setCalcStepPrintStream(System.out)
        .calcExpression("1+2*(3+4*6)/min(5, 2, PI)"));
  }
}
