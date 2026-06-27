package jatymon.babelprojects.abp.protocols;

import jatymon.actions.ActionId;
import jatymon.actions.ActionType;
import jatymon.actions.ActionsCounter;
import jatymon.babelprojects.abp.notifications.connection.ConnectionDownNotification;
import jatymon.babelprojects.abp.notifications.connection.ConnectionUpNotification;
import jatymon.babelprojects.abp.notifications.messages.ReceivedAckMessageNotification;
import jatymon.babelprojects.abp.timers.SendBitTimer;
import jatymon.logging.JaTyMonLogger;
import jatymon.logging.logs.action.IllegalActionLog;
import jatymon.logging.logs.internal.InternalExceptionLog;
import jatymon.logging.logs.internal.MonitorStartLog;
import jatymon.logging.logs.ratio.RatioLogFactory;
import jatymon.math.JaTyMonMath;
import jatymon.math.intervals.Interval;
import java.lang.Override;
import java.lang.String;

public class MonitoredABPSenderProtocol extends ABPSenderProtocol {
  private static final String TYPESTATE_NAME = "ABPSender";

  private final double confidenceLevel;

  private States currentState;

  private final ActionsCounter actionsCounter;

  private final JaTyMonLogger logger;

  public MonitoredABPSenderProtocol() {
    this.confidenceLevel = 0.9;
    this.currentState = States.Start;
    this.actionsCounter = new ActionsCounter();
    this.logger = new JaTyMonLogger();
    logger.log(new MonitorStartLog(TYPESTATE_NAME, confidenceLevel));
  }

  public MonitoredABPSenderProtocol(final JaTyMonLogger logger, final double confidenceLevel) {
    this.confidenceLevel = confidenceLevel;
    this.currentState = States.Start;
    this.actionsCounter = new ActionsCounter();
    this.logger = logger;
    logger.log(new MonitorStartLog(TYPESTATE_NAME, confidenceLevel));
  }

  @Override
  public void receiveAckMessage(ReceivedAckMessageNotification arg0, short arg1) {
    final States oldState = currentState;
    switch (currentState) {
      case S1:
        validateRatio(new ActionId("S1", "receiveAckMessage", ActionType.Input), 0.5);
        try {
          currentState = States.S1;
          super.receiveAckMessage(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      default:
        try {
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "receiveAckMessage", ActionType.Input));
          super.receiveAckMessage(arg0, arg1);
        } catch (final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
    }
  }

  @Override
  public void uponConnectionUp(ConnectionUpNotification arg0, short arg1) {
    final States oldState = currentState;
    switch (currentState) {
      case Start:
        validateRatio(new ActionId("Start", "uponConnectionUp", ActionType.Input), 1.0);
        try {
          currentState = States.S0;
          super.uponConnectionUp(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      default:
        try {
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponConnectionUp", ActionType.Input));
          super.uponConnectionUp(arg0, arg1);
        } catch (final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
    }
  }

  @Override
  public void uponConnectionDown(ConnectionDownNotification arg0, short arg1) {
    final States oldState = currentState;
    switch (currentState) {
      case S1:
        try {
          currentState = States.Start;
          super.uponConnectionDown(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      case Start:
        validateRatio(new ActionId("Start", "uponConnectionDown", ActionType.Input), 0.0);
        try {
          currentState = States.Start;
          super.uponConnectionDown(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      case S0:
        validateRatio(new ActionId("S0", "uponConnectionDown", ActionType.Input), 0.0);
        try {
          currentState = States.Start;
          super.uponConnectionDown(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      default:
        try {
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponConnectionDown", ActionType.Input));
          super.uponConnectionDown(arg0, arg1);
        } catch (final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
    }
  }

  @Override
  public void uponSendBitTimer(SendBitTimer arg0, long arg1) {
    final States oldState = currentState;
    switch (currentState) {
      case S1:
        validateRatio(new ActionId("S1", "uponSendBitTimer", ActionType.Output), 0.5);
        try {
          currentState = States.S1;
          super.uponSendBitTimer(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      case S0:
        validateRatio(new ActionId("S0", "uponSendBitTimer", ActionType.Output), 1.0);
        try {
          currentState = States.S1;
          super.uponSendBitTimer(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      default:
        try {
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponSendBitTimer", ActionType.Output));
          super.uponSendBitTimer(arg0, arg1);
        } catch (final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
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
    S0,

    S1,

    Start
  }
}
