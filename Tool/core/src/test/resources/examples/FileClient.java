package examples;

import jatymon.annotations.Typestate;

@Typestate("FileClient")
public class FileClient {
  public boolean start() {
    /* Nothing to do here... */
    return true;
  }

  public void request(final String fileName) {
    /* Nothing to do here... */
  }

  public void close() {
    /* Nothing to do here... */
  }

  public boolean hasNextByte() {
    /* Nothing to do here... */
    return true;
  }

  public int nextByte() {
    /* Nothing to do here... */
    return 0;
  }
}
