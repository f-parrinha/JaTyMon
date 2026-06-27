package examples;

import jatymon.annotations.Typestate;
import examples.DroneGroupProtocol.Drone;

@Typestate("DroneNodeProtocol")
public class DroneNodeProtocol {
  /* NOTE: This example tests if the TypestateProcessor can find the Drone and DroneNode subclasses */

  public Drone take() {
    /* Nothing to do here... */
    return null;
  }

  public DroneNode getNext() {
    /* Nothing to do here... */
    return null;
  }

  public void putBack(final Drone drone) {
    /* Nothing to do here... */
  }

  public void setLast(final DroneNode droneNode) {
    /* Nothing to do here... */
  }

  public static final class DroneNode {
    /* Nothing to do here... */
  }
}
