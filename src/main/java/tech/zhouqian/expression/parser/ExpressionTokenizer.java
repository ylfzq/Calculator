package tech.zhouqian.expression.parser;

public final class ExpressionTokenizer {

  private String mExpression;
  private int mPosition;
  private final AbstractParser[] mParsers;
  private AbstractParser mLastOne;

  public ExpressionTokenizer(AbstractParser... parsers) {
    mParsers = parsers;
  }

  public ExpressionTokenizer setExpression(String expression) {
    mExpression = expression;
    mPosition = 0;
    for (AbstractParser parser : mParsers) {
      parser.reset();
    }
    mLastOne = null;
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
        if (mLastOne instanceof NumberParser && parser instanceof NumberParser) {
          // It is impossible that a number is following another number
          continue;
        }

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
      final int len = parser.getLength();
      if (len > maxLength) {
        bestParser = parser;
        maxLength = len;
      }
    }

    mLastOne = bestParser;
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
