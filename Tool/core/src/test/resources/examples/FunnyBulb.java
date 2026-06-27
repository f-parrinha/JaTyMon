package examples;

import jatymon.annotations.Typestate;

@Typestate("FunnyBulb")
public class FunnyBulb {
  /* NOTE: This example also tests if the TypestateProcessor can resolve Mode enum as nested class */

  public boolean connect() {
    /* Nothing to do here... */
    return true;
  }

  public void disconnect() {
    /* Nothing to do here... */
  }

  public void setBrightness(int val) {
    /* Nothing to do here... */
  }

  public Mode switchMode() {
    /* Nothing to do here... */
    return Mode.RND;
  }

  public void setColor(final String color) {
    /* Nothing to do here... */
  }

  public void randomColor() {
    /* Nothing to do here... */
  }

  public enum Mode {
    RND,
    STD
  }
}
