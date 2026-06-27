package examples;

import jatymon.annotations.Typestate;

@Typestate("SUV")
public class SUV {

  public boolean turnOn() {
    return true;
  }

  public void turnOff() {
    /* Nothing to do here... */
  }

  public void setSpeed(int val) {
    /* Nothing to do here... */
  }

  public void setFourWheels(boolean onOff) {
    /* Nothing to do here... */
  }

  public void setEcoDrive(boolean onOff) {
    /* Nothing to do here... */
  }

  public Mode switchMode() {
    return Mode.SPORT;
  }

  public enum Mode {
    SPORT,
    COMFORT
  }
}
