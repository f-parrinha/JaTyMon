package jatymon.babelprojects.abdquorum.protocols.dispatcher;

import jatymon.actions.ActionId;
import jatymon.actions.ActionType;
import jatymon.actions.ActionsCounter;
import jatymon.babelprojects.abdquorum.messages.client.ClientReadAck;
import jatymon.babelprojects.abdquorum.messages.client.ClientReadMessage;
import jatymon.babelprojects.abdquorum.messages.client.ClientWriteAck;
import jatymon.babelprojects.abdquorum.messages.client.ClientWriteMessage;
import jatymon.babelprojects.abdquorum.messages.replica.readop.ReadAck;
import jatymon.babelprojects.abdquorum.messages.replica.readop.ReadMessage;
import jatymon.babelprojects.abdquorum.messages.replica.readop.WriteBackAck;
import jatymon.babelprojects.abdquorum.messages.replica.readop.WriteBackMessage;
import jatymon.babelprojects.abdquorum.messages.replica.writeop.ReadTagAck;
import jatymon.babelprojects.abdquorum.messages.replica.writeop.ReadTagMessage;
import jatymon.babelprojects.abdquorum.messages.replica.writeop.WriteAck;
import jatymon.babelprojects.abdquorum.messages.replica.writeop.WriteMessage;
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
import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.channel.tcp.events.InConnectionDown;
import pt.unl.fct.di.novasys.channel.tcp.events.InConnectionUp;
import pt.unl.fct.di.novasys.channel.tcp.events.OutConnectionDown;
import pt.unl.fct.di.novasys.channel.tcp.events.OutConnectionFailed;
import pt.unl.fct.di.novasys.channel.tcp.events.OutConnectionUp;
import pt.unl.fct.di.novasys.network.data.Host;

public class MonitoredFaultyDispatcherProtocol extends FaultyDispatcherProtocol {
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
  public void uponClientWriteAck(ClientWriteAck arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponClientWriteAck(arg0, arg1, arg2, arg3);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponClientWriteAck", ActionType.Input));
        super.uponClientWriteAck(arg0, arg1, arg2, arg3);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponReadMessage(ReadMessage arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponReadMessage(arg0, arg1, arg2, arg3);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponReadMessage", ActionType.Input));
        super.uponReadMessage(arg0, arg1, arg2, arg3);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponOutConnectionFailed(OutConnectionFailed<ProtoMessage> arg0, int arg1) {
    final States oldState = currentState;
    try {
      switch (currentState) {
        case Main:
          currentState = States.Main;
          super.uponOutConnectionFailed(arg0, arg1);
          break;
        case Connect:
          currentState = States.Connect;
          super.uponOutConnectionFailed(arg0, arg1);
          break;
        default:
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponOutConnectionFailed", ActionType.Input));
          super.uponOutConnectionFailed(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponInConnectionDown(InConnectionDown arg0, int arg1) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        if (noConns()) {
          currentState = States.Connect;
        }
        super.uponInConnectionDown(arg0, arg1);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponInConnectionDown", ActionType.Input));
        super.uponInConnectionDown(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponInConnectionUp(InConnectionUp arg0, int arg1) {
    final States oldState = currentState;
    try {
      switch (currentState) {
        case Connect:
          if (hasConns()) {
            currentState = States.Main;
          }
          super.uponInConnectionUp(arg0, arg1);
          break;
        case Main:
          currentState = States.Main;
          super.uponInConnectionUp(arg0, arg1);
          break;
        default:
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponInConnectionUp", ActionType.Input));
          super.uponInConnectionUp(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponReadAck(ReadAck arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponReadAck(arg0, arg1, arg2, arg3);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponReadAck", ActionType.Input));
        super.uponReadAck(arg0, arg1, arg2, arg3);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponReadTagMessage(ReadTagMessage arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponReadTagMessage(arg0, arg1, arg2, arg3);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponReadTagMessage", ActionType.Input));
        super.uponReadTagMessage(arg0, arg1, arg2, arg3);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponWriteBackMessage(WriteBackMessage arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponWriteBackMessage(arg0, arg1, arg2, arg3);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponWriteBackMessage", ActionType.Input));
        super.uponWriteBackMessage(arg0, arg1, arg2, arg3);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponReadTagAck(ReadTagAck arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponReadTagAck(arg0, arg1, arg2, arg3);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponReadTagAck", ActionType.Input));
        super.uponReadTagAck(arg0, arg1, arg2, arg3);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void connectAll() {
    final States oldState = currentState;
    try {
      if (currentState == States.Connect) {
        currentState = States.Connect;
        super.connectAll();
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "connectAll", ActionType.Output));
        super.connectAll();
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponClientReadMessage(ClientReadMessage arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponClientReadMessage(arg0, arg1, arg2, arg3);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponClientReadMessage", ActionType.Input));
        super.uponClientReadMessage(arg0, arg1, arg2, arg3);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponWriteAck(WriteAck arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponWriteAck(arg0, arg1, arg2, arg3);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponWriteAck", ActionType.Input));
        super.uponWriteAck(arg0, arg1, arg2, arg3);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponWriteBackAck(WriteBackAck arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponWriteBackAck(arg0, arg1, arg2, arg3);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponWriteBackAck", ActionType.Input));
        super.uponWriteBackAck(arg0, arg1, arg2, arg3);
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
        currentState = States.CreateTCPChannel;
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
  public void uponOutConnectionUp(OutConnectionUp arg0, int arg1) {
    final States oldState = currentState;
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
          logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponOutConnectionUp", ActionType.Input));
          super.uponOutConnectionUp(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponWriteMessage(WriteMessage arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponWriteMessage(arg0, arg1, arg2, arg3);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponWriteMessage", ActionType.Input));
        super.uponWriteMessage(arg0, arg1, arg2, arg3);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponClientWriteMessage(ClientWriteMessage arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponClientWriteMessage(arg0, arg1, arg2, arg3);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponClientWriteMessage", ActionType.Input));
        super.uponClientWriteMessage(arg0, arg1, arg2, arg3);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public int createTcpChannel(String arg0, String arg1) {
    int superResult = 0;
    final States oldState = currentState;
    try {
      if (currentState == States.CreateTCPChannel) {
        currentState = States.Connect;
        superResult = super.createTcpChannel(arg0, arg1);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "createTcpChannel", ActionType.Input));
        superResult = super.createTcpChannel(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
    return superResult;
  }

  @Override
  public void uponOutConnectionDown(OutConnectionDown arg0, int arg1) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        if (noConns()) {
          currentState = States.Connect;
        }
        super.uponOutConnectionDown(arg0, arg1);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponOutConnectionDown", ActionType.Input));
        super.uponOutConnectionDown(arg0, arg1);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
    }
  }

  @Override
  public void uponClientReadAck(ClientReadAck arg0, Host arg1, short arg2, int arg3) {
    final States oldState = currentState;
    try {
      if (currentState == States.Main) {
        currentState = States.Main;
        super.uponClientReadAck(arg0, arg1, arg2, arg3);
      } else {
        logger.log(new IllegalActionLog(TYPESTATE_NAME, currentState.toString(), "uponClientReadAck", ActionType.Input));
        super.uponClientReadAck(arg0, arg1, arg2, arg3);
      }
    } catch(final Exception e) {
      currentState = oldState;
      logger.log(new InternalExceptionLog(TYPESTATE_NAME, e.getMessage()));
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
    Connect,

    Main,

    Init,

    CreateTCPChannel
  }
}
