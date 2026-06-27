package examples;

import jatymon.annotations.Typestate;

@Typestate("DroneProtocol")
public class DroneProtocol {
  public void takeOff() {
    /* Nothing to do here... */
  }

  public void shutDown() {
    /* Nothing to do here... */
  }

  public void land() {
    /* Nothing to do here... */
  }

  public void setDestination(double x, double y) {
    /* Nothing to do here... */
  }

  public void takePicture() {
    /* Nothing to do here... */
  }

  public boolean hasArrived() {
    /* Nothing to do here... */
    return true;
  }
}
