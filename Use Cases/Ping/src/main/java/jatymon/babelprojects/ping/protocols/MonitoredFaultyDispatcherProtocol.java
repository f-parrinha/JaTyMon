package jatymon.babelprojects.ping.protocols;

import jatymon.actions.ActionId;
import jatymon.actions.ActionType;
import jatymon.actions.ActionsCounter;
import jatymon.babelprojects.ping.messages.EchoReplyMessage;
import jatymon.babelprojects.ping.messages.EchoRequestMessage;
import jatymon.logging.JaTyMonLogger;
import jatymon.logging.logs.action.IllegalActionLog;
import jatymon.logging.logs.internal.InternalExceptionLog;
import jatymon.logging.logs.internal.MonitorStartLog;
import jatymon.logging.logs.ratio.RatioLogFactory;
import jatymon.math.JaTyMonMath;
import jatymon.math.intervals.Interval;
import java.io.IOException;
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

public class MonitoredFaultyDispatcherProtocol extends FaultyDispatcherProtocol {
  private static final String TYPESTATE_NAME = "Dispatcher";

  private final double confidenceLevel;

  private States currentState;

  private final ActionsCounter actionsCounter;

  private final JaTyMonLogger logger;

  public MonitoredFaultyDispatcherProtocol() {
    this.confidenceLevel = 0.9;
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
  public void connectAll() {
    final States oldState = currentState;
    switch (currentState) {
      case Connect:
        try {
          currentState = States.Connect;
          super.connectAll();
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      default:
        try {
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "connectAll", ActionType.Output));
          super.connectAll();
        } catch (final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
    }
  }

  @Override
  public void uponOutConnectionFailed(OutConnectionFailed<ProtoMessage> arg0, int arg1) {
    final States oldState = currentState;
    switch (currentState) {
      case Main:
        try {
          currentState = States.Main;
          super.uponOutConnectionFailed(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      case Connect:
        try {
          currentState = States.Connect;
          super.uponOutConnectionFailed(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      default:
        try {
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponOutConnectionFailed", ActionType.Input));
          super.uponOutConnectionFailed(arg0, arg1);
        } catch (final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
    }
  }

  @Override
  public void uponInConnectionDown(InConnectionDown arg0, int arg1) {
    final States oldState = currentState;
    switch (currentState) {
      case Main:
        try {
          if (noConns()) {
            currentState = States.Connect;
          }
          super.uponInConnectionDown(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      default:
        try {
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponInConnectionDown", ActionType.Input));
          super.uponInConnectionDown(arg0, arg1);
        } catch (final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
    }
  }

  @Override
  public void init(Properties arg0) throws HandlerRegistrationException, IOException {
    final States oldState = currentState;
    switch (currentState) {
      case Init:
        try {
          currentState = States.CreateTCPChannel;
          super.init(arg0);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      default:
        try {
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "init", ActionType.Input));
          super.init(arg0);
        } catch (final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
    }
  }

  @Override
  public void uponEchoReplyMessage(EchoReplyMessage arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    switch (currentState) {
      case Main:
        try {
          currentState = States.Main;
          super.uponEchoReplyMessage(arg0, arg1, arg2, arg3);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      default:
        try {
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponEchoReplyMessage", ActionType.Input));
          super.uponEchoReplyMessage(arg0, arg1, arg2, arg3);
        } catch (final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
    }
  }

  @Override
  public void uponInConnectionUp(InConnectionUp arg0, int arg1) {
    final States oldState = currentState;
    switch (currentState) {
      case Connect:
        try {
          if (hasConns()) {
            currentState = States.Main;
          }
          super.uponInConnectionUp(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      case Main:
        try {
          currentState = States.Main;
          super.uponInConnectionUp(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      default:
        try {
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponInConnectionUp", ActionType.Input));
          super.uponInConnectionUp(arg0, arg1);
        } catch (final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
    }
  }

  @Override
  public void uponOutConnectionUp(OutConnectionUp arg0, int arg1) {
    final States oldState = currentState;
    switch (currentState) {
      case Connect:
        try {
          if (hasConns()) {
            currentState = States.Main;
          }
          super.uponOutConnectionUp(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      case Main:
        try {
          currentState = States.Main;
          super.uponOutConnectionUp(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      default:
        try {
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponOutConnectionUp", ActionType.Input));
          super.uponOutConnectionUp(arg0, arg1);
        } catch (final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
    }
  }

  @Override
  public void uponEchoRequestMessage(EchoRequestMessage arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    switch (currentState) {
      case Main:
        try {
          currentState = States.Main;
          super.uponEchoRequestMessage(arg0, arg1, arg2, arg3);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      default:
        try {
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponEchoRequestMessage", ActionType.Input));
          super.uponEchoRequestMessage(arg0, arg1, arg2, arg3);
        } catch (final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
    }
  }

  @Override
  public int createTcpChannel(String arg0, String arg1) {
    int superResult = 0;
    final States oldState = currentState;
    switch (currentState) {
      case CreateTCPChannel:
        try {
          currentState = States.Connect;
          superResult = super.createTcpChannel(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      default:
        try {
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "createTcpChannel", ActionType.Input));
          superResult = super.createTcpChannel(arg0, arg1);
        } catch (final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
    }
    return superResult;
  }

  @Override
  public void uponOutConnectionDown(OutConnectionDown arg0, int arg1) {
    final States oldState = currentState;
    switch (currentState) {
      case Main:
        try {
          if (noConns()) {
            currentState = States.Connect;
          }
          super.uponOutConnectionDown(arg0, arg1);
        } catch(final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
        break;
      default:
        try {
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponOutConnectionDown", ActionType.Input));
          super.uponOutConnectionDown(arg0, arg1);
        } catch (final Exception e) {
          currentState = oldState;
          logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
        }
    }
  }

  private boolean hasConns() {
    return conns > 0;
  }

  private boolean noConns() {
    return conns == 0;
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
    Main,

    Init,

    CreateTCPChannel,

    Connect
  }
}
