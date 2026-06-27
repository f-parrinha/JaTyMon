package examples;

import jatymon.annotations.Typestate;

@Typestate("AlarmDeviceProtocol")
public class AlarmDeviceProtocol {
  public void connect() {
    /* Nothing to do here... */
  }

  public void notify(double val) {
    /* Nothing to do here... */
  }

  public void setThreshold(double val) {
    /* Nothing to do here... */
  }

  public void disconnect() {
    /* Nothing to do here... */
  }

  public boolean thresholdCheck() {
    /* Nothing to do here... */
    return true;
  }

  public void alert() {
    /* Nothing to do here... */
  }
}