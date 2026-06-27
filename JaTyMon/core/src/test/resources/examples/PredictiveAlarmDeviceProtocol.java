package examples;

import jatymon.annotations.Typestate;

@Typestate("PredictiveAlarmDeviceProtocol")
public class PredictiveAlarmDeviceProtocol {
  public boolean isTrainingNeeded() {
    return true;
  }

  public boolean predictiveThresholdCheck() {
    return true;
  }

  public boolean thresholdCheck() {
    return true;
  }

  public boolean dataValidation() {
    return true;
  }

  public boolean modelEvaluation() {
    return true;
  }

  public void modelTuning(String tuning) {
    /* Nothing to do here... */
  }

  public void notify(double val) {
    /* Nothing to do here... */
  }

  public void connect() {
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

  public void setInferenceTimeStep(String timeStep) {
    /* Nothing to do here... */
  }

  public void setThreshold(double threshold) {
    /* Nothing to do here... */
  }
}
