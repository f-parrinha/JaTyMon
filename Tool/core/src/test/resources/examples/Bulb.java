package examples;

import jatymon.annotations.Typestate;

@Typestate("Bulb")
public class Bulb {
  public void setBrightness(int val) {
    /* Nothing to do here... */
  }

  public boolean connect() {
    /* Nothing to do here... */
    return false;
  }

  public void disconnect() {
    /* Nothing to do here... */
  }
}