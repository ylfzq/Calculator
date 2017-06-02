package tech.zhouqian.expression.parser;

public final class IdentifierParser extends AbstractParser {

  public IdentifierParser(int maxCharCount) {
    super(maxCharCount);
  }

  @Override
  protected boolean isValidNow() {
    for (int i = 0; i < mLength; ++i) {
      if (i == 0) {
        if (!Character.isJavaIdentifierStart(mBuffer[i])) {
          return false;
        }
      } else {
        if (!Character.isJavaIdentifierPart(mBuffer[i])) {
          return false;
        }
      }
    }
    return true;
  }
}
