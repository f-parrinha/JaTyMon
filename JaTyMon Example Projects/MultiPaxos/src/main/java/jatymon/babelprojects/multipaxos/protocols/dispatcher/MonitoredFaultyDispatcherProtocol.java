package jatymon.babelprojects.multipaxos.protocols.dispatcher;

import jatymon.actions.ActionId;
import jatymon.actions.ActionType;
import jatymon.actions.ActionsCounter;
import jatymon.babelprojects.multipaxos.messages.client.ClientReadAck;
import jatymon.babelprojects.multipaxos.messages.client.ClientReadMessage;
import jatymon.babelprojects.multipaxos.messages.client.ClientWriteAck;
import jatymon.babelprojects.multipaxos.messages.client.ClientWriteMessage;
import jatymon.babelprojects.multipaxos.messages.replica.AcceptMessage;
import jatymon.babelprojects.multipaxos.messages.replica.AcceptOkMessage;
import jatymon.babelprojects.multipaxos.messages.replica.PrepareMessage;
import jatymon.babelprojects.multipaxos.messages.replica.PrepareOkMessage;
import jatymon.exceptions.discarding.MonitorDiscardException;
import jatymon.logging.JaTyMonLogger;
import jatymon.logging.logs.action.IllegalActionLog;
import jatymon.logging.logs.internal.InternalExceptionLog;
import jatymon.logging.logs.internal.MonitorStartLog;
import jatymon.logging.logs.ratio.RatioLogFactory;
import jatymon.math.JaTyMonMath;
import jatymon.math.intervals.Interval;
import jatymon.runtime.Monitor;
import java.io.IOException;
import java.lang.Exception;
import java.lang.Override;
import java.lang.String;
import java.util.Properties;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.channel.tcp.events.InConnectionDown;
import pt.unl.fct.di.novasys.channel.tcp.events.InConnectionUp;
import pt.unl.fct.di.novasys.channel.tcp.events.OutConnectionDown;
import pt.unl.fct.di.novasys.channel.tcp.events.OutConnectionFailed;
import pt.unl.fct.di.novasys.channel.tcp.events.OutConnectionUp;
import pt.unl.fct.di.novasys.network.data.Host;

public class MonitoredFaultyDispatcherProtocol extends FaultyDispatcherProtocol implements Monitor {
  public static final String TYPESTATE_NAME = "Dispatcher";

  private final double confidenceLevel;

  private States currentState;

  private final ActionsCounter actionsCounter;

  private final JaTyMonLogger logger;

  public MonitoredFaultyDispatcherProtocol() {
    this.confidenceLevel = 0.95;
    this.currentState = States.Init;
    this.actionsCounter = new ActionsCounter();
    this.logger = new JaTyMonLogger();
    logger.log(new MonitorStartLog(TYPESTATE_NAME, confidenceLevel));
  }

  public MonitoredFaultyDispatcherProtocol(final JaTyMonLogger logger,
      final double confidenceLevel) {
    this.confidenceLevel = confidenceLevel;
    this.currentState = States.Init;
    this.actionsCounter = new ActionsCounter();
    this.logger = logger;
    logger.log(new MonitorStartLog(TYPESTATE_NAME, confidenceLevel));
  }

  @Override
  public void uponPrepareMessage(PrepareMessage arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponPrepareMessage(arg0, arg1, arg2, arg3);
      } else {
        logIllegalActions = true;
        super.uponPrepareMessage(arg0, arg1, arg2, arg3);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponPrepareMessage", ActionType.Input));
    }
  }

  @Override
  public void uponClientWriteAck(ClientWriteAck arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponClientWriteAck(arg0, arg1, arg2, arg3);
      } else {
        logIllegalActions = true;
        super.uponClientWriteAck(arg0, arg1, arg2, arg3);
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

  @Override
  public void uponPrepareOkMessage(PrepareOkMessage arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponPrepareOkMessage(arg0, arg1, arg2, arg3);
      } else {
        logIllegalActions = true;
        super.uponPrepareOkMessage(arg0, arg1, arg2, arg3);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponPrepareOkMessage", ActionType.Input));
    }
  }

  @Override
  public void uponOutConnectionFailed(OutConnectionFailed<ProtoMessage> arg0, int arg1) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      switch (currentState) {
        case Connect:
          currentState = States.Connect;
          super.uponOutConnectionFailed(arg0, arg1);
          break;
        case Main:
          currentState = States.Main;
          super.uponOutConnectionFailed(arg0, arg1);
          break;
        default:
          logIllegalActions = true;
          super.uponOutConnectionFailed(arg0, arg1);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponOutConnectionFailed", ActionType.Input));
    }
  }

  @Override
  public void uponInConnectionDown(InConnectionDown arg0, int arg1) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        if (noConns()) {
          currentState = States.Connect;
        }
        super.uponInConnectionDown(arg0, arg1);
      } else {
        logIllegalActions = true;
        super.uponInConnectionDown(arg0, arg1);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponInConnectionDown", ActionType.Input));
    }
  }

  @Override
  public void uponInConnectionUp(InConnectionUp arg0, int arg1) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      switch (currentState) {
        case Main:
          currentState = States.Main;
          super.uponInConnectionUp(arg0, arg1);
          break;
        case Connect:
          if (hasConns()) {
            currentState = States.Main;
          }
          super.uponInConnectionUp(arg0, arg1);
          break;
        default:
          logIllegalActions = true;
          super.uponInConnectionUp(arg0, arg1);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponInConnectionUp", ActionType.Input));
    }
  }

  @Override
  public void uponAcceptOkMessage(AcceptOkMessage arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponAcceptOkMessage(arg0, arg1, arg2, arg3);
      } else {
        logIllegalActions = true;
        super.uponAcceptOkMessage(arg0, arg1, arg2, arg3);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponAcceptOkMessage", ActionType.Input));
    }
  }

  @Override
  public void uponClientReadMessage(ClientReadMessage arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponClientReadMessage(arg0, arg1, arg2, arg3);
      } else {
        logIllegalActions = true;
        super.uponClientReadMessage(arg0, arg1, arg2, arg3);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponClientReadMessage", ActionType.Input));
    }
  }

  @Override
  public void connectAll() {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Connect) {
        currentState = States.Connect;
        super.connectAll();
      } else {
        logIllegalActions = true;
        super.connectAll();
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "connectAll", ActionType.Output));
    }
  }

  @Override
  public void init(Properties arg0) throws HandlerRegistrationException, IOException {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Init) {
        currentState = States.CreateTCPChannel;
        super.init(arg0);
      } else {
        logIllegalActions = true;
        super.init(arg0);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "init", ActionType.Input));
    }
  }

  @Override
  public void uponOutConnectionUp(OutConnectionUp arg0, int arg1) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      switch (currentState) {
        case Connect:
          if (hasConns()) {
            currentState = States.Main;
          }
          super.uponOutConnectionUp(arg0, arg1);
          break;
        case Main:
          currentState = States.Main;
          super.uponOutConnectionUp(arg0, arg1);
          break;
        default:
          logIllegalActions = true;
          super.uponOutConnectionUp(arg0, arg1);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponOutConnectionUp", ActionType.Input));
    }
  }

  @Override
  public void uponClientWriteMessage(ClientWriteMessage arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponClientWriteMessage(arg0, arg1, arg2, arg3);
      } else {
        logIllegalActions = true;
        super.uponClientWriteMessage(arg0, arg1, arg2, arg3);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponClientWriteMessage", ActionType.Input));
    }
  }

  @Override
  public int createTcpChannel(String arg0, String arg1) {
    int superResult = 0;
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.CreateTCPChannel) {
        currentState = States.Connect;
        superResult = super.createTcpChannel(arg0, arg1);
      } else {
        logIllegalActions = true;
        superResult = super.createTcpChannel(arg0, arg1);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "createTcpChannel", ActionType.Input));
    }
    return superResult;
  }

  @Override
  public void uponOutConnectionDown(OutConnectionDown arg0, int arg1) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        if (noConns()) {
          currentState = States.Connect;
        }
        super.uponOutConnectionDown(arg0, arg1);
      } else {
        logIllegalActions = true;
        super.uponOutConnectionDown(arg0, arg1);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponOutConnectionDown", ActionType.Input));
    }
  }

  @Override
  public void uponClientReadAck(ClientReadAck arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponClientReadAck(arg0, arg1, arg2, arg3);
      } else {
        logIllegalActions = true;
        super.uponClientReadAck(arg0, arg1, arg2, arg3);
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
  public void uponAcceptMessage(AcceptMessage arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    boolean logIllegalActions = false;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponAcceptMessage(arg0, arg1, arg2, arg3);
      } else {
        logIllegalActions = true;
        super.uponAcceptMessage(arg0, arg1, arg2, arg3);
      }
    } catch(final MonitorDiscardException e) {
      logIllegalActions = false;
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    if (logIllegalActions) {
      logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponAcceptMessage", ActionType.Input));
    }
  }

  private boolean noConns() {
    return conns == 0;
  }

  private boolean hasConns() {
    return conns > 0;
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

    Connect,

    CreateTCPChannel
  }
}
