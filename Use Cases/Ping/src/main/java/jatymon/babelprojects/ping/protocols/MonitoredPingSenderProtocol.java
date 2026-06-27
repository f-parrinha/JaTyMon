package jatymon.babelprojects.ping.protocols;

import jatymon.actions.ActionId;
import jatymon.actions.ActionType;
import jatymon.actions.ActionsCounter;
import jatymon.babelprojects.ping.notifications.connection.ConnectionDownNotification;
import jatymon.babelprojects.ping.notifications.connection.ConnectionUpNotification;
import jatymon.babelprojects.ping.notifications.messages.ReceivedEchoReply;
import jatymon.logging.JaTyMonLogger;
import jatymon.logging.logs.action.IllegalActionLog;
import jatymon.logging.logs.internal.InternalExceptionLog;
import jatymon.logging.logs.internal.MonitorStartLog;
import jatymon.logging.logs.ratio.RatioLogFactory;
import jatymon.math.JaTyMonMath;
import jatymon.math.intervals.Interval;
import java.lang.Override;
import java.lang.String;

public class MonitoredPingSenderProtocol extends PingSenderProtocol {
  private static final String TYPESTATE_NAME = "PingSender";

  private final double confidenceLevel;

  private States currentState;

  private final ActionsCounter actionsCounter;

  private final JaTyMonLogger logger;

  private int conns;

  public MonitoredPingSenderProtocol() {
    this.confidenceLevel = 0.9;
    this.currentState = States.State0;
    this.actionsCounter = new ActionsCounter();
    this.logger = new JaTyMonLogger();
    this.conns = 0;
    logger.log(new MonitorStartLog(TYPESTATE_NAME, confidenceLevel));
  }

  public MonitoredPingSenderProtocol(final JaTyMonLogger logger, final double confidenceLevel) {
    this.confidenceLevel = confidenceLevel;
    this.currentState = States.State0;
    this.actionsCounter = new ActionsCounter();
    this.logger = logger;
    this.conns = 0;
    logger.log(new MonitorStartLog(TYPESTATE_NAME, confidenceLevel));
  }

  @Override
  public void broadcastEchoRequest() {
    final States oldState = currentState;
    switch (currentState) {
      case State1:
        validateRatio(new ActionId("State1", "broadcastEchoRequest", ActionType.Output), 0.5);
        try {
          currentState = States.State1;
          super.broadcastEchoRequest();
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      default:
        try {
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "broadcastEchoRequest", ActionType.Output));
          super.broadcastEchoRequest();
        } catch (final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
    }
  }

  @Override
  public void uponReceiveEchoReply(ReceivedEchoReply arg0, short arg1) {
    final States oldState = currentState;
    switch (currentState) {
      case State1:
        validateRatio(new ActionId("State1", "uponReceiveEchoReply", ActionType.Input), 0.5);
        try {
          currentState = States.State1;
          super.uponReceiveEchoReply(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      default:
        try {
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponReceiveEchoReply", ActionType.Input));
          super.uponReceiveEchoReply(arg0, arg1);
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
      case State0:
        try {
          connUp();
          if (hasAllConns()) {
            currentState = States.State1;
          }
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
      case State0:
        try {
          connDown();
          currentState = States.State0;
          super.uponConnectionDown(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      case State1:
        validateRatio(new ActionId("State1", "uponConnectionDown", ActionType.Input), 0.0);
        try {
          connDown();
          currentState = States.State0;
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

  private void connDown() {
    conns = conns - 1;
  }

  private void connUp() {
    conns = conns + 1;
  }

  private boolean hasAllConns() {
    return conns == peersSize - 1;
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
    State1,

    State0
  }
}
