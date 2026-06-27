package jatymon.babelprojects.multipaxos.protocols.client;

import jatymon.actions.ActionId;
import jatymon.actions.ActionType;
import jatymon.actions.ActionsCounter;
import jatymon.babelprojects.multipaxos.notifications.messages.client.ClientReadAckNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.client.ClientWriteAckNotification;
import jatymon.babelprojects.multipaxos.session.Operation;
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

public class MonitoredClientProtocol extends ClientProtocol implements Monitor {
  public static final String TYPESTATE_NAME = "ProbabilisticClient";

  private final double confidenceLevel;

  private States currentState;

  private final ActionsCounter actionsCounter;

  private final JaTyMonLogger logger;

  public MonitoredClientProtocol() {
    this.confidenceLevel = 0.95;
    this.currentState = States.Init;
    this.actionsCounter = new ActionsCounter();
    this.logger = new JaTyMonLogger();
    logger.log(new MonitorStartLog(TYPESTATE_NAME, confidenceLevel));
  }

  public MonitoredClientProtocol(final JaTyMonLogger logger, final double confidenceLevel) {
    this.confidenceLevel = confidenceLevel;
    this.currentState = States.Init;
    this.actionsCounter = new ActionsCounter();
    this.logger = logger;
    logger.log(new MonitorStartLog(TYPESTATE_NAME, confidenceLevel));
  }

  @Override
  public void sendWriteMessage(boolean arg0) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        validateRatio(new ActionId("Main", "sendWriteMessage", ActionType.Input), 0.25);
        currentState = States.Main;
        super.sendWriteMessage(arg0);
      } else {
        logIllegalActions = true;
        super.sendWriteMessage(arg0);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "sendWriteMessage", ActionType.Input));
    }
  }

  @Override
  public void sendReadMessage(boolean arg0) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        validateRatio(new ActionId("Main", "sendReadMessage", ActionType.Input), 0.25);
        currentState = States.Main;
        super.sendReadMessage(arg0);
      } else {
        logIllegalActions = true;
        super.sendReadMessage(arg0);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "sendReadMessage", ActionType.Input));
    }
  }

  @Override
  public void stopClient() {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        currentState = States.end;
        super.stopClient();
      } else {
        logIllegalActions = true;
        super.stopClient();
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "stopClient", ActionType.Input));
    }
  }

  @Override
  public void uponClientReadAck(ClientReadAckNotification arg0, short arg1) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        validateRatio(new ActionId("Main", "uponClientReadAck", ActionType.Input), 0.25);
        currentState = States.Main;
        super.uponClientReadAck(arg0, arg1);
      } else {
        logIllegalActions = true;
        super.uponClientReadAck(arg0, arg1);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponClientReadAck", ActionType.Input));
    }
  }

  @Override
  public void startClient(Operation.Type arg0) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Init) {
        currentState = States.Main;
        super.startClient(arg0);
      } else {
        logIllegalActions = true;
        super.startClient(arg0);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "startClient", ActionType.Input));
    }
  }

  @Override
  public void uponClientWriteAck(ClientWriteAckNotification arg0, short arg1) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        validateRatio(new ActionId("Main", "uponClientWriteAck", ActionType.Input), 0.25);
        currentState = States.Main;
        super.uponClientWriteAck(arg0, arg1);
      } else {
        logIllegalActions = true;
        super.uponClientWriteAck(arg0, arg1);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponClientWriteAck", ActionType.Input));
    }
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
    end,

    Main,

    Init
  }
}
