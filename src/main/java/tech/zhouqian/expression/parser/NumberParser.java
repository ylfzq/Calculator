package tech.zhouqian.expression.parser;

public final class NumberParser extends AbstractParser {
  public NumberParser(int maxCharCount) {
    super(maxCharCount);
  }

  @Override
  protected boolean isValidNow() {
    if (mLength == 1 && mBuffer[0] == '-') {
      return true;
    }
    try {
      Double.parseDouble(toString());
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }
}
