package examples;

import jatymon.annotations.Typestate;

@Typestate("MultiTaskRobotProtocol")
public class MultiTaskRobotProtocol {

  public void turnOn() {
    /* Nothing to do here... */
  }

  public void turnOff() {
    /* Nothing to do here... */
  }
  public void executeTask(String taskName) {
    /* Nothing to do here... */
  }
  public void unplugArm() {
    /* Nothing to do here... */
  }

  public void plugArm(MechanicalArm arm) {
    /* Nothing to do here... */
  }

  public static final class MechanicalArm {
    /* Nothing to do here... */
  }
}
