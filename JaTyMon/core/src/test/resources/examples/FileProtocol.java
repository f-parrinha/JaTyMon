package examples;

import jatymon.annotations.Typestate;
import examples.enums.FileStatus;

@Typestate("FileProtocol")
public class FileProtocol {
  public FileStatus open() {
    /* Nothing to do here... */
    return FileStatus.OK;
  }

  public String read() {
    /* Nothing to do here... */
    return "";
  }

  public void close() {
    /* Nothing to do here... */
  }
}
