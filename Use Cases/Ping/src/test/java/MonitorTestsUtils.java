import jatymon.babelprojects.ping.messages.EchoReplyMessage;
import jatymon.babelprojects.ping.messages.EchoRequestMessage;
import jatymon.babelprojects.ping.notifications.connection.ConnectionDownNotification;
import jatymon.babelprojects.ping.notifications.connection.ConnectionUpNotification;
import jatymon.babelprojects.ping.notifications.messages.ReceivedEchoReply;
import jatymon.babelprojects.ping.notifications.messages.ReceivedEchoRequest;
import jatymon.babelprojects.ping.timers.PingTimer;
import jatymon.math.JaTyMonMath;
import pt.unl.fct.di.novasys.babel.core.Babel;
import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.channel.tcp.events.*;
import pt.unl.fct.di.novasys.network.data.Host;

import java.lang.reflect.Field;
import java.net.InetAddress;
import java.util.Arrays;
import java.util.LinkedList;

public class MonitorTestsUtils {
    public static final InetAddress TEST_ADDRESS = InetAddress.getLoopbackAddress();
    public static final int TEST_PORT = 8080;
    public static final Host TEST_HOST = new Host(InetAddress.getLoopbackAddress(), TEST_PORT);
    public static final String BABEL_INSTANCE_FIELD_NAME = "system";
    public static final String CURRENT_STATE_FIELD_NAME = "currentState";

    // Timers
    public static final PingTimer TEST_SEND_BIT_TIMER = new PingTimer();

    // Messages
    public static final EchoRequestMessage TEST_REQUEST_MESSAGE = new EchoRequestMessage();
    public static final EchoReplyMessage TEST_REPLY_MESSAGE = new EchoReplyMessage();

    // Events
    public static final InConnectionUp TEST_IN_CONNECTION_UP = new InConnectionUp(TEST_HOST);
    public static final InConnectionDown TEST_IN_CONNECTION_DOWN = new InConnectionDown(TEST_HOST, new Throwable());
    public static final OutConnectionUp TEST_OUT_CONNECTION_UP = new OutConnectionUp(TEST_HOST);
    public static final OutConnectionDown TEST_OUT_CONNECTION_DOWN = new OutConnectionDown(TEST_HOST, new Throwable());
    public static final OutConnectionFailed<ProtoMessage> TEST_OUT_CONNECTION_FAILED = new OutConnectionFailed<>(TEST_HOST, new LinkedList<>(), new Throwable());


    // Notifications
    public static final ReceivedEchoReply TEST_ACK_MESSAGE_NOTIFICATION = new ReceivedEchoReply(TEST_REPLY_MESSAGE, TEST_HOST);
    public static final ReceivedEchoRequest TEST_BIT_MESSAGE_NOTIFICATION = new ReceivedEchoRequest(TEST_REQUEST_MESSAGE, TEST_HOST);
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
     */
    public static void changeCurrentStateVar(Object target, String stateName) throws NoSuchFieldException, IllegalAccessException {
        final Field stateField = target.getClass().getDeclaredField(CURRENT_STATE_FIELD_NAME);
        stateField.setAccessible(true);

        final Object desiredState = Arrays.stream(stateField.getType().getEnumConstants())
                .filter(e -> ((Enum<?>) e).name().equals(stateName))
                .findFirst()
                .orElseThrow();
        stateField.set(target, desiredState);
    }
}
