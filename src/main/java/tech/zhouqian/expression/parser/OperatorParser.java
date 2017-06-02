package tech.zhouqian.expression.parser;

import java.util.List;

public final class OperatorParser extends AbstractParser {

  protected final String[] VALID_OPERATOR;

  public OperatorParser(List<String> validOperator) {
    super(5);
    VALID_OPERATOR = validOperator.toArray(new String[validOperator.size()]);
  }

  protected boolean isValidOperator(String str) {
    for (String o : VALID_OPERATOR) {
      if (o.startsWith(str)) {
        return true;
      }
    }
    return false;
  }

  @Override
  protected boolean isValidNow() {
    for (int i = 0; i < mLength; ++i) {
      if (!isValidOperator(toString())) {
        return false;
      }
    }
    return true;
  }
}
