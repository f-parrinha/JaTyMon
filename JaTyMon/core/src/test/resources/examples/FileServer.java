package examples;

import jatymon.annotations.Typestate;
import java.net.*;

@Typestate("FileServer")
public class FileServer {
  public boolean start(final Socket socket) {
    /* Nothing to do here... */
    return true;
  }

  public boolean hasRequest() {
    /* Nothing to do here... */
    return true;
  }

  public String receiveFilename() {
    /* Nothing to do here... */
    return "";
  }

  public void sendEof() {
    /* Nothing to do here... */
  }

  public void sendByte(int val) {
    /* Nothing to do here... */
  }

  public void close() {
    /* Nothing to do here... */
  }
}
