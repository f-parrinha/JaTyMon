package jatymon.babelprojects.abdquorum.protocols.quorum;

import jatymon.actions.ActionId;
import jatymon.actions.ActionType;
import jatymon.actions.ActionsCounter;
import jatymon.babelprojects.abdquorum.notifications.connection.ConnectionDownNotification;
import jatymon.babelprojects.abdquorum.notifications.connection.ConnectionUpNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.readop.ReadMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.readop.WriteBackMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.writeop.ReadTagMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.writeop.WriteMessageNotification;
import jatymon.exceptions.discarding.MonitorDiscardException;
import jatymon.logging.JaTyMonLogger;
import jatymon.logging.logs.action.IllegalActionLog;
import jatymon.logging.logs.internal.InternalExceptionLog;
import jatymon.logging.logs.internal.MonitorStartLog;
import jatymon.logging.logs.ratio.RatioLogFactory;
import jatymon.math.JaTyMonMath;
import jatymon.math.intervals.Interval;
import jatymon.runtime.Monitor;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;

public class MonitoredABDReceiverProtocol extends ABDReceiverProtocol implements Monitor {
  public static final String TYPESTATE_NAME = "ABDReceiver";

  private final double confidenceLevel;

  private States currentState;

  private final ActionsCounter actionsCounter;

  private final JaTyMonLogger logger;

  private int conns;

  public MonitoredABDReceiverProtocol() {
    this.confidenceLevel = 0.95;
    this.currentState = States.Connect;
    this.actionsCounter = new ActionsCounter();
    this.logger = new JaTyMonLogger();
    this.conns = 0;
    logger.log(new MonitorStartLog(TYPESTATE_NAME, confidenceLevel));
  }

  public MonitoredABDReceiverProtocol(final JaTyMonLogger logger, final double confidenceLevel) {
    this.confidenceLevel = confidenceLevel;
    this.currentState = States.Connect;
    this.actionsCounter = new ActionsCounter();
    this.logger = logger;
    this.conns = 0;
    logger.log(new MonitorStartLog(TYPESTATE_NAME, confidenceLevel));
  }

  @Override
  public void uponReadTagMessage(ReadTagMessageNotification arg0, short arg1) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponReadTagMessage(arg0, arg1);
      } else {
        logIllegalActions = true;
        super.uponReadTagMessage(arg0, arg1);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponReadTagMessage", ActionType.Input));
    }
  }

  @Override
  public void uponReadMessage(ReadMessageNotification arg0, short arg1) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponReadMessage(arg0, arg1);
      } else {
        logIllegalActions = true;
        super.uponReadMessage(arg0, arg1);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponReadMessage", ActionType.Input));
    }
  }

  @Override
  public void uponConnectionUp(ConnectionUpNotification arg0, short arg1) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Connect) {
        validateRatio(new ActionId("Connect", "uponConnectionUp", ActionType.Input), 1.0);
        connUp();
        if (hasAllConns()) {
          currentState = States.Main;
        }
        super.uponConnectionUp(arg0, arg1);
      } else {
        logIllegalActions = true;
        super.uponConnectionUp(arg0, arg1);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponConnectionUp", ActionType.Input));
    }
  }

  @Override
  public void uponWriteBackMessage(WriteBackMessageNotification arg0, short arg1) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponWriteBackMessage(arg0, arg1);
      } else {
        logIllegalActions = true;
        super.uponWriteBackMessage(arg0, arg1);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponWriteBackMessage", ActionType.Input));
    }
  }

  @Override
  public void uponWriteMessage(WriteMessageNotification arg0, short arg1) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponWriteMessage(arg0, arg1);
      } else {
        logIllegalActions = true;
        super.uponWriteMessage(arg0, arg1);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponWriteMessage", ActionType.Input));
    }
  }

  @Override
  public void uponConnectionDown(ConnectionDownNotification arg0, short arg1) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      switch (currentState) {
        case Connect:
          connDown();
          currentState = States.Connect;
          super.uponConnectionDown(arg0, arg1);
          break;
        case Main:
          connDown();
          if (lostQuorum()) {
            currentState = States.Connect;
          }
          super.uponConnectionDown(arg0, arg1);
          break;
        default:
          logIllegalActions = true;
          super.uponConnectionDown(arg0, arg1);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponConnectionDown", ActionType.Input));
    }
  }

  private void connUp() {
    conns = conns + 1;
  }

  private void connDown() {
    conns = conns - 1;
  }

  private boolean hasAllConns() {
    return conns == peersSize;
  }

  private boolean lostQuorum() {
    return conns <= peersSize - 1 / 2;
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
