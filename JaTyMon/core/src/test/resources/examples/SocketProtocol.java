package examples;

import jatymon.annotations.Typestate;

@Typestate("SocketProtocol")
public class SocketProtocol {

  public boolean connect() {
    return true;
  }

  public boolean canSend(String val) {
    return true;
  }

  public boolean canReceive() {
    return true;
  }

  public void send(String val) {
    /* Nothing to do here... */
  }

  public void close() {
    /* Nothing to do here... */
  }

  public String receive() {
    return null;
  }
}
