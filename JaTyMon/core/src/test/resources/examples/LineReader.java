package examples;

import jatymon.annotations.Typestate;

@Typestate("LineReader")
public class LineReader {

  public String read() {
    return null;
  }

  public Status open(String fileName) {
    return Status.OK;
  }

  public void close() {
    /* Nothing to do here */
  }

  public boolean eof() {
    return true;
  }

  public enum Status {
    OK,
    ERROR
  }
}
