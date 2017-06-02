package tech.zhouqian.expression.parser;

import java.util.Arrays;

public final class GroupParser extends AbstractParser {

  protected final char[] VALID_OPERATOR;

  public GroupParser(char[] validOperator) {
    super(1);
    VALID_OPERATOR = Arrays.copyOf(validOperator, validOperator.length);
  }

  public boolean isValidOperator(char ch) {
    for (char o : VALID_OPERATOR) {
      if (o == ch) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected boolean isValidNow() {
    for (int i = 0; i < mLength; ++i) {
      if (!isValidOperator(mBuffer[i])) {
        return false;
      }
    }
    return true;
  }
}
