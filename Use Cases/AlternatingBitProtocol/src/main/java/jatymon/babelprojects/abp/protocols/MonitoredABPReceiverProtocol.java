package jatymon.babelprojects.abp.protocols;

import jatymon.actions.ActionId;
import jatymon.actions.ActionType;
import jatymon.actions.ActionsCounter;
import jatymon.babelprojects.abp.notifications.connection.ConnectionDownNotification;
import jatymon.babelprojects.abp.notifications.connection.ConnectionUpNotification;
import jatymon.babelprojects.abp.notifications.messages.ReceivedBitMessageNotification;
import jatymon.logging.JaTyMonLogger;
import jatymon.logging.logs.action.IllegalActionLog;
import jatymon.logging.logs.internal.InternalExceptionLog;
import jatymon.logging.logs.internal.MonitorStartLog;
import jatymon.logging.logs.ratio.RatioLogFactory;
import jatymon.math.JaTyMonMath;
import jatymon.math.intervals.Interval;
import java.lang.Override;
import java.lang.String;
import pt.unl.fct.di.novasys.network.data.Host;

public class MonitoredABPReceiverProtocol extends ABPReceiverProtocol {
  private static final String TYPESTATE_NAME = "ABPReceiver";

  private final double confidenceLevel;

  private States currentState;

  private final ActionsCounter actionsCounter;

  private final JaTyMonLogger logger;

  public MonitoredABPReceiverProtocol() {
    this.confidenceLevel = 0.9;
    this.currentState = States.Start;
    this.actionsCounter = new ActionsCounter();
    this.logger = new JaTyMonLogger();
    logger.log(new MonitorStartLog(TYPESTATE_NAME, confidenceLevel));
  }

  public MonitoredABPReceiverProtocol(final JaTyMonLogger logger, final double confidenceLevel) {
    this.confidenceLevel = confidenceLevel;
    this.currentState = States.Start;
    this.actionsCounter = new ActionsCounter();
    this.logger = logger;
    logger.log(new MonitorStartLog(TYPESTATE_NAME, confidenceLevel));
  }

  @Override
  public void receiveBitMessage(ReceivedBitMessageNotification arg0, short arg1) {
    final States oldState = currentState;
    switch (currentState) {
      case R0:
        validateRatio(new ActionId("R0", "receiveBitMessage", ActionType.Input), 1.0);
        try {
          currentState = States.R1;
          super.receiveBitMessage(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      case R1:
        validateRatio(new ActionId("R1", "receiveBitMessage", ActionType.Input), 0.5);
        try {
          currentState = States.R1;
          super.receiveBitMessage(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      default:
        try {
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "receiveBitMessage", ActionType.Input));
          super.receiveBitMessage(arg0, arg1);
        } catch (final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
    }
  }

  @Override
  public void sendAckMessage(Host arg0, byte arg1) {
    final States oldState = currentState;
    switch (currentState) {
      case R1:
        validateRatio(new ActionId("R1", "sendAckMessage", ActionType.Output), 0.5);
        try {
          currentState = States.R1;
          super.sendAckMessage(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      default:
        try {
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "sendAckMessage", ActionType.Output));
          super.sendAckMessage(arg0, arg1);
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
          currentState = States.R0;
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
      case R0:
        validateRatio(new ActionId("R0", "uponConnectionDown", ActionType.Input), 0.0);
        try {
          currentState = States.Start;
          super.uponConnectionDown(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      case R1:
        validateRatio(new ActionId("R1", "uponConnectionDown", ActionType.Input), 0.0);
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
    R0,

    R1,

    Start
  }
}
