import jatymon.babelprojects.multipaxos.ConfigKeys;
import jatymon.babelprojects.multipaxos.messages.client.ClientReadAck;
import jatymon.babelprojects.multipaxos.messages.client.ClientReadMessage;
import jatymon.babelprojects.multipaxos.messages.client.ClientWriteAck;
import jatymon.babelprojects.multipaxos.messages.client.ClientWriteMessage;
import jatymon.babelprojects.multipaxos.messages.replica.AcceptMessage;
import jatymon.babelprojects.multipaxos.messages.replica.AcceptOkMessage;
import jatymon.babelprojects.multipaxos.messages.replica.PrepareMessage;
import jatymon.babelprojects.multipaxos.messages.replica.PrepareOkMessage;
import jatymon.babelprojects.multipaxos.notifications.connection.ConnectionDownNotification;
import jatymon.babelprojects.multipaxos.notifications.connection.ConnectionUpNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.client.ClientReadAckNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.client.ClientReadMessageNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.client.ClientWriteAckNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.client.ClientWriteMessageNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.replica.AcceptOkMessageNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.replica.PrepareOkMessageNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.replica.AcceptMessageNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.replica.PrepareMessageNotification;
import jatymon.math.JaTyMonMath;
import pt.unl.fct.di.novasys.babel.core.Babel;
import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.channel.tcp.events.*;
import pt.unl.fct.di.novasys.network.data.Host;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Properties;

public class MonitorTestsUtils {
    public static final InetAddress TEST_ADDRESS = InetAddress.getLoopbackAddress();
    public static final int TEST_PORT = 8080;
    public static final Host TEST_HOST = new Host(InetAddress.getLoopbackAddress(), TEST_PORT);
    public static final String BABEL_INSTANCE_FIELD_NAME = "system";
    public static final String CURRENT_STATE_FIELD_NAME = "currentState";
    public static final Properties TEST_PROPS =  new Properties();
    static {
        TEST_PROPS.setProperty(ConfigKeys.PEERS_CONFIG, "9000");
        TEST_PROPS.setProperty(ConfigKeys.CLIENT_START_DELAY, "0");
        TEST_PROPS.setProperty(ConfigKeys.CLIENTS_PORTS_CONFIG, "9000");
    }


    // Messages
    public static final ClientReadMessage TEST_CLIENT_READ_MESSAGE = new ClientReadMessage("clientID", TEST_HOST);
    public static final ClientReadAck TEST_CLIENT_READ_ACK = new ClientReadAck("clientID", TEST_HOST, new byte[0]);
    public static final ClientWriteMessage TEST_CLIENT_WRITE_MESSAGE = new ClientWriteMessage("clientID", TEST_HOST, new byte[0]);
    public static final ClientWriteAck TEST_CLIENT_WRITE_ACK = new ClientWriteAck("clientID", TEST_HOST);
    public static final PrepareMessage TEST_PREPARE_MESSAGE = new PrepareMessage("op1", 0, 0);
    public static final AcceptMessage TEST_ACCEPT_MESSAGE = new AcceptMessage("op2", 0, 0, new byte[0], false);
    public static final PrepareOkMessage TEST_PREPARE_OK_MESSAGE = new PrepareOkMessage("op1", 0, 0, new byte[0]);
    public static final AcceptOkMessage TEST_ACCEPT_OK_MESSAGE = new AcceptOkMessage("op2", 0, new byte[0]);

    // Events
    public static final InConnectionUp TEST_IN_CONNECTION_UP = new InConnectionUp(TEST_HOST);
    public static final InConnectionDown TEST_IN_CONNECTION_DOWN = new InConnectionDown(TEST_HOST, new Throwable());
    public static final OutConnectionUp TEST_OUT_CONNECTION_UP = new OutConnectionUp(TEST_HOST);
    public static final OutConnectionDown TEST_OUT_CONNECTION_DOWN = new OutConnectionDown(TEST_HOST, new Throwable());
    public static final OutConnectionFailed<ProtoMessage> TEST_OUT_CONNECTION_FAILED = new OutConnectionFailed<>(TEST_HOST, new LinkedList<>(), new Throwable());

    // Notifications
    public static final ClientWriteMessageNotification TEST_CLIENT_WRITE_MESSAGE_NOTIFICATION = new ClientWriteMessageNotification(TEST_CLIENT_WRITE_MESSAGE, TEST_HOST);
    public static final ClientWriteAckNotification TEST_CLIENT_WRITE_ACK_NOTIFICATION = new ClientWriteAckNotification(TEST_CLIENT_WRITE_ACK, TEST_HOST);
    public static final ClientReadMessageNotification TEST_CLIENT_READ_MESSAGE_NOTIFICATION = new ClientReadMessageNotification(TEST_CLIENT_READ_MESSAGE, TEST_HOST);
    public static final ClientReadAckNotification TEST_CLIENT_READ_ACK_NOTIFICATION = new ClientReadAckNotification(TEST_CLIENT_READ_ACK, TEST_HOST);
    public static final PrepareMessageNotification TEST_PREPARE_MESSAGE_NOTIFICATION = new PrepareMessageNotification(TEST_PREPARE_MESSAGE, TEST_HOST);
    public static final AcceptMessageNotification TEST_ACCEPT_MESSAGE_NOTIFICATION = new AcceptMessageNotification(TEST_ACCEPT_MESSAGE, TEST_HOST);
    public static final PrepareOkMessageNotification TEST_PREPARE_OK_MESSAGE_NOTIFICATION = new PrepareOkMessageNotification(TEST_PREPARE_OK_MESSAGE, TEST_HOST);
    public static final AcceptOkMessageNotification TEST_ACCEPT_OK_MESSAGE_NOTIFICATION = new AcceptOkMessageNotification(TEST_ACCEPT_OK_MESSAGE, TEST_HOST);
    public static final ConnectionUpNotification TEST_CONNECTION_UP_NOTIFICATION = new ConnectionUpNotification(TEST_HOST, 1, 1, 0);
    public static final ConnectionDownNotification TEST_CONNECTION_DOWN_NOTIFICATION = new ConnectionDownNotification(TEST_HOST, 1, 0);


    /**
     * Calculates the number of times an action must be executed to fall out of the correct confidence interval. The
     *  method takes into account the total number of previous executions that occurred in the state where the action is,
     *  and the number of times it was executed until now
     *  <p>
     *      E = SE * z(l) <br>
     *      SE = sqrt(p(1-p)/n) <br>
     *      Emax = p + SE * z(l) => <br>
     *      Emax = p + sqrt(p(1-p)/n)
     *  <p/>
     *  <p>
     *      The question is: given "m" perfect total action executions and "d" perfect executions for a single action,
     *      how many "n" executions of that action until it goes above Emax? <br>
     *      &emsp;(d+n)/(m+n) = p + z(l) * sqrt(p(1-p)/(m+n))
     *  </p>
     *  <p>
     *      Let N = m+n => n = N-m <br>
     *      (d+n)/(m+n) = p + z(l) * sqrt(p(1-p)/(m+n)) = <br>
     *      (d+n)/N = p + z(l) * sqrt(p(1-p)/N) = <br>
     *      N*(d+n)/N = N * p + N * z(l) * sqrt(p(1-p)/N) = <br>
     *      d+n = N * p + sqrt(N²) * z(l) * sqrt(p(1-p)/N) = <br>
     *      d+N-m = pN + z(l) * sqrt(N² * p(1-p)/N) = <br>
     *      d+N-m = pN + z(l) * sqrt(Np(1-p)) = <br>
     *      d-m+N(1-p) = z(l) * sqrt(Np(1-p)) = <br>
     *      d-m+N(1-p) = z(l) * sqrt(p(1-p)) * sqrt(N) = <br>
     *  </p>
     *  <p>
     *      Let x = sqrt(N), q = 1-p, then <br>
     *      d-m + q*x² = z(l) * sqrt(p*q) * x <br>
     *      q*x² - z(l)*sqrt(p*q)*x + (d-m) = 0 <br>
     *  </p>
     *  <p>
     *      Solving the quadratic (taking the positive root): <br>
     *      x = (z(l)*sqrt(p*q) + sqrt(z(l)²*p*q - 4*q*(d-m))) / (2*q) <br>
     *      Since N = x² => n = N - m <br>
     *  </p>
     * @param confidenceLevel confidence level
     * @param trueRatio true ratio
     * @param totalStateExecutions total number of action executions until now in the state where the action is
     * @param totalActionExecutions number of executions for particular action in a state until now
     * @return number of execution an action must take to deviate to erroneous behaviour
     */
    public static int getMinActionExecutionsForRatioLog(double confidenceLevel, double trueRatio, int totalStateExecutions, int totalActionExecutions) {
        final double z = JaTyMonMath.zscore(confidenceLevel);
        final double q = 1 - trueRatio;
        final double pq = trueRatio * q;

        // This "a" is just to make it easier to read the quadratic formula
        final double a = q;
        final double b = -z * Math.sqrt(pq);
        final double c = totalActionExecutions - totalStateExecutions;

        // Quadratic formula
        final double sqrtN = (-b + Math.sqrt(b * b - 4 * a * c)) / (2 * a);
        final int minN = (int) Math.ceil(sqrtN * sqrtN);
        return minN - totalStateExecutions;
    }

    /**
     * Resets internal field storing the Babel instance. Uses reflection, and it is a hack to make sure the Babel instance
     *  is reset in each testing class
     */
    public static void resetBabelInstance() throws NoSuchFieldException, IllegalAccessException {
        final Field instance = Babel.class.getDeclaredField(MonitorTestsUtils.BABEL_INSTANCE_FIELD_NAME);
        instance.setAccessible(true);
        instance.set(null, null);
    }

    /**
     * Sets the currentState variable to a different value in the receiver protocol
     * @param target target instance
     * @param stateName state name
     */
    public static void changeCurrentStateVar(final Object target, final String stateName) throws NoSuchFieldException, IllegalAccessException {
        final Field stateField = target.getClass().getDeclaredField(CURRENT_STATE_FIELD_NAME);
        stateField.setAccessible(true);

        final Object desiredState = Arrays.stream(stateField.getType().getEnumConstants())
                .filter(e -> ((Enum<?>) e).name().equals(stateName))
                .findFirst()
                .orElseThrow();
        stateField.set(target, desiredState);
    }

    /**
     * Sets a variable given by varName to the result given by res
     * @param target target instance
     * @param varName variable name
     * @param res result to set
     */
    public static void changeVar(final Object target, final String varName, final String res) throws NoSuchFieldException, IllegalAccessException {
        final Field stateField = target.getClass().getDeclaredField(varName);
        stateField.setAccessible(true);

        final Object desiredState = Arrays.stream(stateField.getType().getEnumConstants())
                .filter(e -> ((Enum<?>) e).name().equals(res))
                .findFirst()
                .orElseThrow();
        stateField.set(target, desiredState);
    }
}
