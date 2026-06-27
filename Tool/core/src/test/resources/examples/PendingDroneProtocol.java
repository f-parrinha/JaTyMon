package examples;

import jatymon.annotations.Typestate;

@Typestate("PendingDroneProtocol")
public class PendingDroneProtocol {

  public Drone takeHoveringDrone() {
    return null;
  }

  public boolean completed() {
    return true;
  }

  public void setTask(Drone drone, DroneTask droneTask) {
    /* Nothing to do here... */
  }

  public void finishTask(Drone drone) {
    /* Nothing to do here... */
  }

  public void continueTask(Drone drone) {
    /* Nothing to do here... */
  }

  public DroneTask getTask() {
    return null;
  }

  public Drone takeFlyingDrone() {
    return null;
  }

  public static final class Drone {
    /* Nothing to do here... */
  }

  public static final class DroneTask {
    /* Nothing to do here... */
  }
}
