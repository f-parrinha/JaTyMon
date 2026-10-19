package jatymon.babelprojects.abdquorum.protocols.quorum;

import jatymon.actions.ActionId;
import jatymon.actions.ActionType;
import jatymon.actions.ActionsCounter;
import jatymon.babelprojects.abdquorum.notifications.connection.ConnectionDownNotification;
import jatymon.babelprojects.abdquorum.notifications.connection.ConnectionUpNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.client.ClientReadMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.client.ClientWriteMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.readop.ReadAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.readop.ReadMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.readop.WriteBackAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.readop.WriteBackMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.writeop.ReadTagAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.writeop.ReadTagMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.writeop.WriteAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.writeop.WriteMessageNotification;
import jatymon.logging.JaTyMonLogger;
import jatymon.logging.logs.action.IllegalActionLog;
import jatymon.logging.logs.internal.InternalExceptionLog;
import jatymon.logging.logs.internal.MonitorStartLog;
import jatymon.logging.logs.ratio.RatioLogFactory;
import jatymon.math.JaTyMonMath;
import jatymon.math.intervals.Interval;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;

public class MonitoredABDProtocol extends ABDProtocol {
  public static final String TYPESTATE_NAME = "ABDQuorum";

  private final double confidenceLevel;

  private States currentState;

  private final ActionsCounter actionsCounter;

  private final JaTyMonLogger logger;

  private int conns;

  public MonitoredABDProtocol() {
    this.confidenceLevel = 0.95;
    this.currentState = States.Connect;
    this.actionsCounter = new ActionsCounter();
    this.logger = new JaTyMonLogger();
    this.conns = 0;
    logger.log(new MonitorStartLog(TYPESTATE_NAME, confidenceLevel));
  }

  public MonitoredABDProtocol(final JaTyMonLogger logger, final double confidenceLevel) {
    this.confidenceLevel = confidenceLevel;
    this.currentState = States.Connect;
    this.actionsCounter = new ActionsCounter();
    this.logger = logger;
    this.conns = 0;
    logger.log(new MonitorStartLog(TYPESTATE_NAME, confidenceLevel));
  }

  @Override
  public void uponWriteBackAck(WriteBackAckNotification arg0, short arg1) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponWriteBackAck(arg0, arg1);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponWriteBackAck", ActionType.Input));
        super.uponWriteBackAck(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponReadTagMessage(ReadTagMessageNotification arg0, short arg1) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponReadTagMessage(arg0, arg1);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponReadTagMessage", ActionType.Input));
        super.uponReadTagMessage(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponReadTagAck(ReadTagAckNotification arg0, short arg1) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponReadTagAck(arg0, arg1);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponReadTagAck", ActionType.Input));
        super.uponReadTagAck(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponClientReadMessage(ClientReadMessageNotification arg0, short arg1) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponClientReadMessage(arg0, arg1);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponClientReadMessage", ActionType.Input));
        super.uponClientReadMessage(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponReadMessage(ReadMessageNotification arg0, short arg1) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponReadMessage(arg0, arg1);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponReadMessage", ActionType.Input));
        super.uponReadMessage(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponReadAck(ReadAckNotification arg0, short arg1) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponReadAck(arg0, arg1);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponReadAck", ActionType.Input));
        super.uponReadAck(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponConnectionUp(ConnectionUpNotification arg0, short arg1) {
    final States oldState = currentState;
    try {
      switch (currentState) {
        case Connect:
          validateRatio(new ActionId("Connect", "uponConnectionUp", ActionType.Input), 1.0);
          connUp();
          if (hasAllConns()) {
            currentState = States.Main;
          }
          super.uponConnectionUp(arg0, arg1);
          break;
        case Main:
          connUp();
          if (hasAllConns()) {
            currentState = States.Main;
          }
          super.uponConnectionUp(arg0, arg1);
          break;
        default:
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponConnectionUp", ActionType.Input));
          super.uponConnectionUp(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponWriteBackMessage(WriteBackMessageNotification arg0, short arg1) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponWriteBackMessage(arg0, arg1);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponWriteBackMessage", ActionType.Input));
        super.uponWriteBackMessage(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponWriteAck(WriteAckNotification arg0, short arg1) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponWriteAck(arg0, arg1);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponWriteAck", ActionType.Input));
        super.uponWriteAck(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponWriteMessage(WriteMessageNotification arg0, short arg1) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponWriteMessage(arg0, arg1);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponWriteMessage", ActionType.Input));
        super.uponWriteMessage(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponConnectionDown(ConnectionDownNotification arg0, short arg1) {
    final States oldState = currentState;
    try {
      switch (currentState) {
        case Main:
          connDown();
          if (lostQuorum()) {
            currentState = States.Connect;
          }
          super.uponConnectionDown(arg0, arg1);
          break;
        case Connect:
          validateRatio(new ActionId("Connect", "uponConnectionDown", ActionType.Input), 0.0);
          connDown();
          currentState = States.Connect;
          super.uponConnectionDown(arg0, arg1);
          break;
        default:
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponConnectionDown", ActionType.Input));
          super.uponConnectionDown(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponClientWriteMessage(ClientWriteMessageNotification arg0, short arg1) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponClientWriteMessage(arg0, arg1);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponClientWriteMessage", ActionType.Input));
        super.uponClientWriteMessage(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  private void connDown() {
    conns = conns - 1;
  }

  private void connUp() {
    conns = conns + 1;
  }

  private boolean lostQuorum() {
    return conns <= peersSize - 1 / 2;
  }

  private boolean hasAllConns() {
    return conns >= peersSize;
  }

  private void validateRatio(final ActionId actionId, final double trueRatio) {
    actionsCounter.sample(actionId);

    // Confidence interval calculation
    final int stateActionsCount = actionsCounter.getStateActionsCount(actionId.stateName());
    final int actionCount = actionsCounter.getActionCount(actionId);
    final double ratio = (double) actionCount / stateActionsCount;
    final double standardError = JaTyMonMath.getStandardError(trueRatio, stateActionsCount);
    final Interval confidenceInterval = JaTyMonMath.getConfidenceInterval(confidenceLevel, standardError, trueRatio);

    // Verify ratio with confidence interval
    if (confidenceInterval.contains(ratio)) {
      logger.removeRatioLogs(actionId);
    } else {
      logger.log(RatioLogFactory.buildLog(TYPESTATE_NAME, actionId, ratio, confidenceInterval));
    }
  }

  private enum States {
    Connect,

    Main
  }
}
