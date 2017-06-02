package tech.zhouqian.expression.parser;

public abstract class AbstractParser {

  protected final char[] mBuffer;
  protected int mLength;
  protected boolean mIsValid;

  public AbstractParser(int maxCharCount) {
    mBuffer = new char[maxCharCount + 1];
    mLength = 0;
    mIsValid = true;
  }

  protected void reset() {
    mLength = 0;
    mIsValid = true;
  }

  protected boolean feed(char ch) {
    if (mIsValid) {
      mBuffer[mLength] = ch;
      ++mLength;
      if (mLength < mBuffer.length && isValidNow()) {
        return true;
      } else {
        --mLength;
        mIsValid = false;
        return false;
      }
    } else {
      return false;
    }
  }

  protected abstract boolean isValidNow();

  @Override
  public String toString() {
    return new String(mBuffer, 0, mLength);
  }
}
