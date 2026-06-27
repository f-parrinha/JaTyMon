package examples;

import jatymon.annotations.Typestate;

@Typestate("FileClient2")
public class FileClient2 {
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

  public String nextLine() {
    /* Nothing to do here... */
    return "";
  }
}
