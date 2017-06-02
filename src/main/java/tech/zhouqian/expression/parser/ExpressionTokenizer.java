package tech.zhouqian.expression.parser;

public final class ExpressionTokenizer {

  private String mExpression;
  private int mPosition;
  private final AbstractParser[] mParsers;

  public ExpressionTokenizer(AbstractParser... parsers) {
    mParsers = parsers;
  }

  public ExpressionTokenizer setExpression(String expression) {
    mExpression = expression;
    mPosition = 0;
    for (AbstractParser parser : mParsers) {
      parser.reset();
    }
    return this;
  }

  public AbstractParser next() {
    final int length = mExpression.length();
    if (mPosition >= length) {
      return null;
    }

    for (AbstractParser parser : mParsers) {
      parser.reset();
    }

    for (; mPosition < length; ++mPosition) {
      final char ch = mExpression.charAt(mPosition);
      if (ch == ' ') {
        continue;
      }

      boolean enough = true;
      for (AbstractParser parser : mParsers) {
        if (parser.feed(ch)) {
          enough = false;
        }
      }
      if (enough) {
        break;
      }
    }

    AbstractParser bestParser = null;
    int maxLength = 0;
    for (AbstractParser parser : mParsers) {
      final int len = parser.toString().length();
      if (len > maxLength) {
        bestParser = parser;
        maxLength = len;
      }
    }
    return bestParser;
  }

  public int getPosition() {
    return mPosition;
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    AbstractParser parser;
    while ((parser = next()) != null) {
      sb.append(parser.toString()).append("\n");
    }
    return sb.toString();
  }
}
