package examples;

import jatymon.annotations.Typestate;

@Typestate("SmartDeviceProtocol")
public class SmartDeviceProtocol {

  public double forecast(String val) {
    return 0;
  }

  public boolean dataValidation() {
    return true;
  }

  public boolean isTrainingNeeded() {
    return true;
  }

  public void notify(double val) {
    /* Nothing to do here... */
  }

  public void ack() {
    /* Nothing to do here... */
  }

  public void disconnect() {
    /* Nothing to do here... */
  }

  public void alert() {
    /* Nothing to do here... */
  }

  public void pruneData() {
    /* Nothing to do here... */
  }

  public void train() {
    /* Nothing to do here... */
  }

  public boolean modelEvaluation() {
    return true;
  }

  public void modelTuning(String tuning) {
    /* Nothing to do here... */
  }
}
