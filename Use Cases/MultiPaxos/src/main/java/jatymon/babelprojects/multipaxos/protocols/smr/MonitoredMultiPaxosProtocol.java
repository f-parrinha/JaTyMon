package jatymon.babelprojects.multipaxos.protocols.smr;

import jatymon.actions.ActionId;
import jatymon.actions.ActionType;
import jatymon.actions.ActionsCounter;
import jatymon.babelprojects.multipaxos.notifications.connection.ConnectionDownNotification;
import jatymon.babelprojects.multipaxos.notifications.connection.ConnectionUpNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.client.ClientReadMessageNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.client.ClientWriteMessageNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.replica.AcceptMessageNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.replica.AcceptOkMessageNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.replica.PrepareMessageNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.replica.PrepareOkMessageNotification;
import jatymon.babelprojects.multipaxos.timers.MultiPaxosRetryTimer;
import jatymon.logging.JaTyMonLogger;
import jatymon.logging.logs.action.IllegalActionLog;
import jatymon.logging.logs.internal.InternalExceptionLog;
import jatymon.logging.logs.internal.MonitorStartLog;
import jatymon.logging.logs.ratio.RatioLogFactory;
import jatymon.math.JaTyMonMath;
import jatymon.math.intervals.Interval;
import java.io.IOException;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.util.Properties;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;

public class MonitoredMultiPaxosProtocol extends MultiPaxosProtocol {
  public static final String TYPESTATE_NAME = "MultiPaxos";

  private final double confidenceLevel;

  private States currentState;

  private final ActionsCounter actionsCounter;

  private final JaTyMonLogger logger;

  private int prepareOkCount;

  private int conns;

  public MonitoredMultiPaxosProtocol() {
    this.confidenceLevel = 0.95;
    this.currentState = States.Init;
    this.actionsCounter = new ActionsCounter();
    this.logger = new JaTyMonLogger();
    this.prepareOkCount = 0;
    this.conns = 0;
    logger.log(new MonitorStartLog(TYPESTATE_NAME, confidenceLevel));
  }

  public MonitoredMultiPaxosProtocol(final JaTyMonLogger logger, final double confidenceLevel) {
    this.confidenceLevel = confidenceLevel;
    this.currentState = States.Init;
    this.actionsCounter = new ActionsCounter();
    this.logger = logger;
    this.prepareOkCount = 0;
    this.conns = 0;
    logger.log(new MonitorStartLog(TYPESTATE_NAME, confidenceLevel));
  }

  @Override
  public boolean uponAcceptMessage(AcceptMessageNotification arg0, short arg1) {
    boolean superResult = false;
    final States oldState = currentState;
    try {
      switch (currentState) {
        case NonLeader:
          superResult = super.uponAcceptMessage(arg0, arg1);
          if (superResult) {
            currentState = States.Leader;
          } else {
            currentState = States.NonLeader;
          }
          break;
        case Leader:
          superResult = super.uponAcceptMessage(arg0, arg1);
          if (superResult) {
            currentState = States.Leader;
          } else {
            currentState = States.NonLeader;
          }
          break;
        default:
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponAcceptMessage", ActionType.Input));
          superResult = super.uponAcceptMessage(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    return superResult;
  }

  @Override
  public void uponAcceptOkMessage(AcceptOkMessageNotification arg0, short arg1) {
    final States oldState = currentState;
    try {
      if (currentState == States.Leader) {
        currentState = States.Leader;
        super.uponAcceptOkMessage(arg0, arg1);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponAcceptOkMessage", ActionType.Input));
        super.uponAcceptOkMessage(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponPrepareMessage(PrepareMessageNotification arg0, short arg1) {
    final States oldState = currentState;
    try {
      switch (currentState) {
        case NonLeader:
          currentState = States.NonLeader;
          super.uponPrepareMessage(arg0, arg1);
          break;
        case Leader:
          currentState = States.NonLeader;
          super.uponPrepareMessage(arg0, arg1);
          break;
        default:
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponPrepareMessage", ActionType.Input));
          super.uponPrepareMessage(arg0, arg1);
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
      switch (currentState) {
        case NonLeader:
          currentState = States.NonLeader;
          super.uponClientReadMessage(arg0, arg1);
          break;
        case Leader:
          currentState = States.Leader;
          super.uponClientReadMessage(arg0, arg1);
          break;
        default:
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponClientReadMessage", ActionType.Input));
          super.uponClientReadMessage(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void init(Properties arg0) throws HandlerRegistrationException, IOException {
    final States oldState = currentState;
    try {
      if (currentState == States.Init) {
        currentState = States.Connect;
        super.init(arg0);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "init", ActionType.Input));
        super.init(arg0);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponPaxosRetryTimer(MultiPaxosRetryTimer arg0, long arg1) {
    final States oldState = currentState;
    try {
      switch (currentState) {
        case Leader:
          currentState = States.NonLeader;
          super.uponPaxosRetryTimer(arg0, arg1);
          break;
        case NonLeader:
          currentState = States.NonLeader;
          super.uponPaxosRetryTimer(arg0, arg1);
          break;
        default:
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponPaxosRetryTimer", ActionType.Input));
          super.uponPaxosRetryTimer(arg0, arg1);
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
        case NonLeader:
          incConns();
          if (hasConns()) {
            currentState = States.NonLeader;
          }
          super.uponConnectionUp(arg0, arg1);
          break;
        case Connect:
          incConns();
          if (hasConns()) {
            currentState = States.NonLeader;
          }
          super.uponConnectionUp(arg0, arg1);
          break;
        case Leader:
          incConns();
          if (hasConns()) {
            currentState = States.Leader;
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
  public void uponConnectionDown(ConnectionDownNotification arg0, short arg1) {
    final States oldState = currentState;
    try {
      switch (currentState) {
        case Leader:
          decConns();
          if (lostQuorum()) {
            currentState = States.Connect;
          }
          super.uponConnectionDown(arg0, arg1);
          break;
        case NonLeader:
          decConns();
          if (lostQuorum()) {
            currentState = States.Connect;
          }
          super.uponConnectionDown(arg0, arg1);
          break;
        case Connect:
          decConns();
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
  public void uponPrepareOkMessage(PrepareOkMessageNotification arg0, short arg1) {
    final States oldState = currentState;
    try {
      if (currentState == States.NonLeader) {
        incPrepareOkCount();
        if (hasPrepareOkQuorum()) {
          currentState = States.Leader;
          resetPrepareOkCount();
        }
        super.uponPrepareOkMessage(arg0, arg1);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponPrepareOkMessage", ActionType.Input));
        super.uponPrepareOkMessage(arg0, arg1);
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
      switch (currentState) {
        case NonLeader:
          currentState = States.NonLeader;
          super.uponClientWriteMessage(arg0, arg1);
          break;
        case Leader:
          currentState = States.Leader;
          super.uponClientWriteMessage(arg0, arg1);
          break;
        default:
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponClientWriteMessage", ActionType.Input));
          super.uponClientWriteMessage(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  private void resetPrepareOkCount() {
    prepareOkCount = 0;
  }

  private void incConns() {
    conns = conns + 1;
  }

  private void decConns() {
    conns = conns - 1;
  }

  private void incPrepareOkCount() {
    prepareOkCount = prepareOkCount + 1;
  }

  private boolean hasPrepareOkQuorum() {
    return prepareOkCount > peersSize / 2;
  }

  private boolean hasConns() {
    return conns >= peersSize;
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

    Leader,

    Init,

    NonLeader
  }
}
