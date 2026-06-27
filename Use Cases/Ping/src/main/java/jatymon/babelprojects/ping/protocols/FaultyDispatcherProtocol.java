package jatymon.babelprojects.ping.protocols;

import jatymon.annotations.Typestate;
import jatymon.babelprojects.ping.ConfigKeys;
import jatymon.babelprojects.ping.messages.EchoReplyMessage;
import jatymon.babelprojects.ping.messages.EchoRequestMessage;
import jatymon.babelprojects.ping.timers.FailureTimer;
import jatymon.babelprojects.ping.timers.ConnectionRetryTimer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.network.data.Host;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Typestate("Dispatcher")
public class FaultyDispatcherProtocol extends DispatcherProtocol {
    public static final String PROTO_NAME = "ABPFaultyDispatcher";
    private static final Logger logger = LogManager.getLogger(FaultyDispatcherProtocol.class);
    private static final float CONN_FAILURE_PROBABILITY = 0.5f;
    private static final Set<FaultType> DEFAULT_ENABLED_FAULTS = EnumSet.of(FaultType.Connection, FaultType.MessageDrop, FaultType.MessageTinker);

    private final Random random;

    private Set<FaultType> enabledFaults;
    private FaultType currentFaultType;
    private float faultProbability;
    private int minDelay;
    private int maxDelay;
    private int connFailMinTime;
    private int connFailMaxTime;


    public FaultyDispatcherProtocol() {
        super(PROTO_NAME, PROTO_ID);
        this.random = new Random();
        this.currentFaultType = FaultType.NoFailure;
    }

    @Override
    public void init(final Properties props) throws HandlerRegistrationException, IOException {
        super.init(props);

        enabledFaults = FaultType.fromFaultList(props.getProperty(ConfigKeys.FAULT_LIST_CONFIG, ""));
        faultProbability = Float.parseFloat(props.getProperty(ConfigKeys.FAULT_PROBABILITY_CONFIG));
        minDelay = Integer.parseInt(props.getProperty(ConfigKeys.FAULT_MIN_DELAY_CONFIG));
        maxDelay = Integer.parseInt(props.getProperty(ConfigKeys.FAULT_MAX_DELAY_CONFIG));
        connFailMinTime = Integer.parseInt(props.getProperty(ConfigKeys.FAULT_CONN_FAIL_MIN_TIME));
        connFailMaxTime = Integer.parseInt(props.getProperty(ConfigKeys.FAULT_CONN_FAIL_MAX_TIME));
        registerTimerHandler(FailureTimer.ID, this::uponFailureTimer);

        setupTimer(new FailureTimer(), getRandomFaultDelay());
    }

    @Override
    protected void uponEchoRequestMessage(EchoRequestMessage message, final Host sender, final short sourceProto, final int channelId) {
        switch (currentFaultType) {
            case MessageDrop -> {
                if (injectMessageDrop(message)) {
                    return;
                }
            }
            case MessageTinker -> {
                if (injectMessageTinker(message)) {
                    message = new EchoRequestMessage();
                }
            }
        }

        super.uponEchoRequestMessage(message, sender, sourceProto, channelId);
    }

    @Override
    protected void uponEchoReplyMessage(EchoReplyMessage message, final Host sender, final short sourceProto, final int channelId) {
        switch (currentFaultType) {
            case MessageDrop -> {
                if (injectMessageDrop(message)) {
                    return;
                }
            }
            case MessageTinker -> {
                if (injectMessageTinker(message)) {
                    message = new EchoReplyMessage();
                }
            }
        }

        super.uponEchoReplyMessage(message, sender, sourceProto, channelId);
    }

    private void uponFailureTimer(final FailureTimer timer, final long timerId) {
        if (random.nextFloat() <= faultProbability) {
            final List<FaultType> eligible = new ArrayList<>(enabledFaults);
            currentFaultType = eligible.get(random.nextInt(enabledFaults.size()));
            if (currentFaultType == FaultType.Connection) {
                injectConnectionFailure();
                currentFaultType = FaultType.NoFailure;
            }
        }

        setupTimer(timer, getRandomFaultDelay());
    }

    private boolean injectMessageDrop(final ProtoMessage message) {
        if (enabledFaults.contains(FaultType.MessageDrop)) {
            logger.warn("Injecting MessageDrop failure on {}", message);
            currentFaultType = FaultType.NoFailure;
            return true;
        }

        return false;
    }

    /* ---------- ERROR INJECTION METHODS ---------- */

    private boolean injectMessageTinker(final ProtoMessage message) {
        if (enabledFaults.contains(FaultType.MessageTinker)) {
            logger.warn("Injecting MessageTinker failure on {}", message);
            currentFaultType = FaultType.NoFailure;
            return true;
        }
        return false;
    }

    private void injectConnectionFailure() {
        if (!enabledFaults.contains(FaultType.Connection)) {
            return;
        }

        logger.warn("Injecting a connection failure");
        boolean injected = false;

        // 50/50 chance of disconnecting with a peer
        for (final Host host : List.copyOf(connections)) {
            if (random.nextFloat() <= CONN_FAILURE_PROBABILITY) {
                logger.warn("Connection with {} was dropped", host);
                closeConnection(host, channelId, 0);
                setupTimer(new ConnectionRetryTimer(host, channelId), getRandomConnFailTime());
                injected = true;
            }
        }

        // If no connection failed, make sure to fail at least one
        if (!injected && !connections.isEmpty()) {
            final Host host = connections.iterator().next();
            logger.warn("Connection with {} was dropped", host);
            closeConnection(host, channelId, 0);
            setupTimer(new ConnectionRetryTimer(host, channelId), getRandomConnFailTime());
        }
    }

    private long getRandomFaultDelay() {
        return minDelay + (long)(random.nextFloat() * (maxDelay - minDelay));
    }
    private long getRandomConnFailTime() {
        return connFailMinTime + (long) (random.nextFloat() * (connFailMaxTime - connFailMinTime));
    }


    private enum FaultType {
        Connection,
        MessageDrop,
        MessageTinker,
        NoFailure;

        /**
         * Returns the corresponding {@code FaultType} or null. It is case-sensitive.
         * @param name name to parse
         * @return fault type or null
         */
        public static FaultType fromString(final String name) {
            return switch (name) {
                case "ConnectionDrop" -> FaultType.Connection;
                case "MessageDrop" -> FaultType.MessageDrop;
                case "MessageTinker" -> FaultType.MessageTinker;
                default -> null;
            };
        }

        /**
         * Returns a set of faults given a string for a list of fault types separated by a comma
         * @param faultsString fault types separated by a comma
         * @return Set with fault types in the list
         */
        public static Set<FaultType> fromFaultList(final String faultsString) {
            return Arrays.stream(faultsString.split(","))
                    .map(String::strip)
                    .filter(s -> !s.isEmpty())
                    .map(FaultType::fromString)
                    .filter(Objects::nonNull)
                    .collect(Collectors.toCollection(() -> EnumSet.noneOf(FaultType.class)));

        }
    }
}
