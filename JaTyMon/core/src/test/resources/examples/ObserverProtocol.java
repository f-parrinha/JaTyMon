package examples;

import jatymon.annotations.Typestate;

@Typestate("ObserverProtocol")
public class ObserverProtocol {

  public void notify(double notification) {
    /* Nothing to do here... */
  }

  public void ack() {
    /* Nothing to do here... */
  }
}
