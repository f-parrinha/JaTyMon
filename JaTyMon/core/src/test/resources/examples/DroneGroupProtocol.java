package examples;

import jatymon.annotations.Typestate;

@Typestate("DroneGroupProtocol")
public class DroneGroupProtocol {
  /* NOTE: This example tests if the TypestateProcessor can find the Drone subclass */

  public void add(final Drone drone) {
    /* Nothing to do here... */
  }

  public Drone take() {
    /* Nothing to do here... */
    return null;
  }

  public void next() {
    /* Nothing to do here... */
  }

  public void landAll() {
    /* Nothing to do here... */
  }

  public void putBack(final Drone drone) {
    /* Nothing to do here... */
  }

  public static final class Drone {
    /* Nothing to do here... */
  }
}
