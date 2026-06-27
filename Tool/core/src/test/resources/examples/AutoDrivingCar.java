package examples;

import jatymon.annotations.Typestate;
import examples.enums.Mode;

@Typestate("AutoDrivingCar")
public class AutoDrivingCar {
  public boolean turnOn() {
    /* Nothing to do here... */
    return true;
  }

  public void turnOff() {
    /* Nothing to do here... */
  }

  public void setSpeed(int speed) {
    /* Nothing to do here... */
  }

  public Mode switchMode() {
    /* Nothing to do here... */
    return Mode.MANUAL_DRIVE;
  }

  public void autoPark() {
    /* Nothing to do here... */
  }
}