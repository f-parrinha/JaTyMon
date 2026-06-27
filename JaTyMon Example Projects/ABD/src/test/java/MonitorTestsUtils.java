import jatymon.babelprojects.abdquorum.ConfigKeys;
import jatymon.babelprojects.abdquorum.data.Database;
import jatymon.babelprojects.abdquorum.data.Tag;
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
import jatymon.babelprojects.abdquorum.notifications.connection.ConnectionDownNotification;
import jatymon.babelprojects.abdquorum.notifications.connection.ConnectionUpNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.client.ClientReadAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.client.ClientReadMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.client.ClientWriteAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.client.ClientWriteMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.readop.ReadAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.readop.ReadMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.readop.WriteBackAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.readop.WriteBackMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.writeop.ReadTagAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.writeop.ReadTagMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.writeop.WriteAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.writeop.WriteMessageNotification;
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
    public static final ClientReadMessage TEST_CLIENT_READ_MESSAGE = new ClientReadMessage("client");
    public static final ClientReadAck TEST_CLIENT_READ_ACK = new ClientReadAck("client", new byte[0]);
    public static final ClientWriteMessage TEST_CLIENT_WRITE_MESSAGE = new ClientWriteMessage("client", new byte[0]);
    public static final ClientWriteAck TEST_CLIENT_WRITE_ACK = new ClientWriteAck("client");
    public static final ReadMessage TEST_READ_MESSAGE = new ReadMessage("op1", "client");
    public static final ReadAck TEST_READ_ACK = new ReadAck("op1", new Database.Entry(new Tag(1,1), new byte[0]));
    public static final ReadTagMessage TEST_READ_TAG_MESSAGE = new ReadTagMessage("op2", "client");
    public static final ReadTagAck TEST_READ_TAG_ACK = new ReadTagAck("op2", new Tag(1, 1));
    public static final WriteMessage TEST_WRITE_MESSAGE = new WriteMessage("op3", "client", new Database.Entry(new Tag(1,1), new byte[0]));
    public static final WriteAck TEST_WRITE_ACK = new WriteAck("op3", new Database.Entry(new Tag(1,1), new byte[0]));
    public static final WriteBackMessage TEST_WRITE_BACK_MESSAGE = new WriteBackMessage("op4", "client", new Database.Entry(new Tag(1,1), new byte[0]));
    public static final WriteBackAck TEST_WRITE_BACK_ACK = new WriteBackAck("op4",  new Database.Entry(new Tag(1,1), new byte[0]));

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
    public static final ReadMessageNotification TEST_READ_MESSAGE_NOTIFICATION = new ReadMessageNotification(TEST_READ_MESSAGE, TEST_HOST);
    public static final ReadAckNotification TEST_READ_ACK_NOTIFICATION = new ReadAckNotification(TEST_READ_ACK, TEST_HOST);
    public static final ReadTagMessageNotification TEST_READ_TAG_MESSAGE_NOTIFICATION = new ReadTagMessageNotification(TEST_READ_TAG_MESSAGE, TEST_HOST);
    public static final ReadTagAckNotification TEST_READ_TAG_ACK_NOTIFICATION = new ReadTagAckNotification(TEST_READ_TAG_ACK, TEST_HOST);
    public static final WriteMessageNotification TEST_WRITE_MESSAGE_NOTIFICATION = new WriteMessageNotification(TEST_WRITE_MESSAGE, TEST_HOST);
    public static final WriteAckNotification TEST_WRITE_ACK_NOTIFICATION = new WriteAckNotification(TEST_WRITE_ACK, TEST_HOST);
    public static final WriteBackMessageNotification TEST_WRITE_BACK_MESSAGE_NOTIFICATION = new WriteBackMessageNotification(TEST_WRITE_BACK_MESSAGE, TEST_HOST);
    public static final WriteBackAckNotification TEST_WRITE_BACK_ACK_NOTIFICATION = new WriteBackAckNotification(TEST_WRITE_BACK_ACK, TEST_HOST);
    public static final ConnectionUpNotification TEST_CONNECTION_UP_NOTIFICATION = new ConnectionUpNotification(TEST_HOST, 1, 1, 0);
    public static final ConnectionDownNotification TEST_CONNECTION_DOWN_NOTIFICATION = new ConnectionDownNotification(TEST_HOST, 1, 0);
    public static final WriteMessageNotification TEST_BIT_MESSAGE_NOTIFICATION = new WriteMessageNotification(TEST_WRITE_MESSAGE, TEST_HOST);


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
