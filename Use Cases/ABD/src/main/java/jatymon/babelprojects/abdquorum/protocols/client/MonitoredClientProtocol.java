package jatymon.babelprojects.abdquorum.protocols.client;

import jatymon.actions.ActionId;
import jatymon.actions.ActionType;
import jatymon.actions.ActionsCounter;
import jatymon.babelprojects.abdquorum.notifications.messages.client.ClientReadAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.client.ClientWriteAckNotification;
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

public class MonitoredClientProtocol extends ClientProtocol {
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
  public void sendReadMessage(boolean arg0) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        validateRatio(new ActionId("Main", "sendReadMessage", ActionType.Input), 0.25);
        currentState = States.Main;
        super.sendReadMessage(arg0);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "sendReadMessage", ActionType.Input));
        super.sendReadMessage(arg0);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void sendWriteMessage(boolean arg0) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        validateRatio(new ActionId("Main", "sendWriteMessage", ActionType.Input), 0.25);
        currentState = States.Main;
        super.sendWriteMessage(arg0);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "sendWriteMessage", ActionType.Input));
        super.sendWriteMessage(arg0);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void stopClient() {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.end;
        super.stopClient();
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "stopClient", ActionType.Input));
        super.stopClient();
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponClientReadAck(ClientReadAckNotification arg0, short arg1) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        validateRatio(new ActionId("Main", "uponClientReadAck", ActionType.Input), 0.25);
        currentState = States.Main;
        super.uponClientReadAck(arg0, arg1);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponClientReadAck", ActionType.Input));
        super.uponClientReadAck(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponClientWriteAck(ClientWriteAckNotification arg0, short arg1) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        validateRatio(new ActionId("Main", "uponClientWriteAck", ActionType.Input), 0.25);
        currentState = States.Main;
        super.uponClientWriteAck(arg0, arg1);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponClientWriteAck", ActionType.Input));
        super.uponClientWriteAck(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void startClient(ClientProtocol.Op arg0) {
    final States oldState = currentState;
    try {
      if (currentState == States.Init) {
        currentState = States.Main;
        super.startClient(arg0);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "startClient", ActionType.Input));
        super.startClient(arg0);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
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
    Init,

    Main,

    end
  }
}
