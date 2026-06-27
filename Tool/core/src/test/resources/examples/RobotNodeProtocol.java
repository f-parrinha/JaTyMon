package examples;

import jatymon.annotations.Typestate;

@Typestate("RobotNodeProtocol")
public class RobotNodeProtocol {

  public void setLast(RobotNode robotNode) {
    /* Nothing to do here... */
  }

  public RobotNode getNext() {
    return null;
  }

  public void putBack(Robot robot) {
    /* Nothing to do here... */
  }

  public Robot take() {
    return null;
  }

  public static final class RobotNode {
    /* Nothing to do here... */
  }
}
