package jatymon.babelprojects.abdquorum.protocols.dispatcher;

import jatymon.annotations.Typestate;
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
import jatymon.babelprojects.abdquorum.protocols.client.ClientProtocol;
import jatymon.babelprojects.abdquorum.timers.FailureTimer;
import jatymon.babelprojects.abdquorum.timers.ConnectionRetryTimer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.network.data.Host;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * The faulty dispatcher automatically induces three types of faults: message drop, tinkering, and connection drop. The faulty
 * dispatcher is a subclass of the dispatcher, making the configuration of fault on/off easy to implement
 */
@Typestate("Dispatcher")
public class FaultyDispatcherProtocol extends DispatcherProtocol {
    public static final String PROTO_NAME = "FaultyDispatcher";
    private static final Logger logger = LogManager.getLogger(FaultyDispatcherProtocol.class);
    private static final float CONN_FAILURE_PROBABILITY = 0.5f;
    private static final Set<FaultType> DEFAULT_ENABLED_FAULTS = EnumSet.of(FaultType.Connection, FaultType.MessageDrop, FaultType.MessageTinker);

    private final Random rand;

    private Set<FaultType> enabledFaults;
    private FaultType currentFaultType;
    private float faultProbability;
    private int minDelay;
    private int maxDelay;
    private int connFailMinTime;
    private int connFailMaxTime;


    public FaultyDispatcherProtocol() {
        super(PROTO_NAME, PROTO_ID);
        this.rand = new Random();
        this.currentFaultType = FaultType.NoFailure;
    }

    @Override
    public void init(final Properties props) throws HandlerRegistrationException, IOException {
        super.init(props);

        long faultStartDelay = Long.parseLong(props.getProperty(ConfigKeys.FAULT_START_DELAY));
        enabledFaults = FaultType.fromFaultList(props.getProperty(ConfigKeys.FAULT_LIST_CONFIG, ""));
        faultProbability = Float.parseFloat(props.getProperty(ConfigKeys.FAULT_PROBABILITY_CONFIG));
        minDelay = Integer.parseInt(props.getProperty(ConfigKeys.FAULT_MIN_DELAY_CONFIG));
        maxDelay = Integer.parseInt(props.getProperty(ConfigKeys.FAULT_MAX_DELAY_CONFIG));
        connFailMinTime = Integer.parseInt(props.getProperty(ConfigKeys.FAULT_CONN_FAIL_MIN_TIME));
        connFailMaxTime = Integer.parseInt(props.getProperty(ConfigKeys.FAULT_CONN_FAIL_MAX_TIME));
        if (enabledFaults.isEmpty()) {
            enabledFaults = DEFAULT_ENABLED_FAULTS;
        }

        registerTimerHandler(FailureTimer.ID, this::uponFailureTimer);
        setupTimer(new FailureTimer(), faultStartDelay);
    }


    /* -------------- CLIENT MESSAGE EVENT HANDLERS -------------- */


    @Override
    protected void uponClientReadMessage(ClientReadMessage message, final Host sender, final short sourceProto, final int channelId) {
        switch (currentFaultType) {
            case MessageDrop -> {
                if (injectMessageDrop(message)) {
                    return;
                }
            }
            case MessageTinker -> {
                if (injectMessageTinker(message)) {
                    message = new ClientReadMessage(getRandId());
                }
            }
        }
        super.uponClientReadMessage(message, sender, sourceProto, channelId);
    }

    @Override
    protected void uponClientReadAck(ClientReadAck message, final Host sender, final short sourceProto, final int channelId) {
        switch (currentFaultType) {
            case MessageDrop -> {
                if (injectMessageDrop(message)) {
                    return;
                }
            }
            case MessageTinker -> {
                if (injectMessageTinker(message)) {
                    message = new ClientReadAck(getRandId(), getRandBytes());
                }
            }
        }
        super.uponClientReadAck(message, sender, sourceProto, channelId);
    }

    @Override
    protected void uponClientWriteMessage(ClientWriteMessage message, final Host sender, final short sourceProto, final int channelId) {
        switch (currentFaultType) {
            case MessageDrop -> {
                if (injectMessageDrop(message)) {
                    return;
                }
            }
            case MessageTinker -> {
                if (injectMessageTinker(message)) {
                    message = new ClientWriteMessage(getRandId(), getRandBytes());
                }
            }
        }
        super.uponClientWriteMessage(message, sender, sourceProto, channelId);
    }

    @Override
    protected void uponClientWriteAck(ClientWriteAck message, final Host sender, final short sourceProto, final int channelId) {
        switch (currentFaultType) {
            case MessageDrop -> {
                if (injectMessageDrop(message)) {
                    return;
                }
            }
            case MessageTinker -> {
                if (injectMessageTinker(message)) {
                    message = new ClientWriteAck(getRandId());
                }
            }
        }
        super.uponClientWriteAck(message, sender, sourceProto, channelId);
    }


    /* -------------- QUORUM MESSAGE EVENT HANDLERS -------------- */


    @Override
    protected void uponReadTagMessage(ReadTagMessage message, final Host sender, final short sourceProto, final int channelId) {
        switch (currentFaultType) {
            case MessageDrop -> {
                if (injectMessageDrop(message)) {
                    return;
                }
            }
            case MessageTinker -> {
                if (injectMessageTinker(message)) {
                    message = new ReadTagMessage(getRandId(), getRandId());
                }
            }
        }
        super.uponReadTagMessage(message, sender, sourceProto, channelId);
    }

    @Override
    protected void uponReadTagAck(ReadTagAck message, final Host sender, final short sourceProto, final int channelId) {
        switch (currentFaultType) {
            case MessageDrop -> {
                if (injectMessageDrop(message)) {
                    return;
                }
            }
            case MessageTinker -> {
                if (injectMessageTinker(message)) {
                    message = new ReadTagAck(getRandId(), getRandTag());
                }
            }
        }
        super.uponReadTagAck(message, sender, sourceProto, channelId);
    }

    @Override
    protected void uponReadMessage(ReadMessage message, final Host sender, final short sourceProto, final int channelId) {
        switch (currentFaultType) {
            case MessageDrop -> {
                if (injectMessageDrop(message)) {
                    return;
                }
            }
            case MessageTinker -> {
                if (injectMessageTinker(message)) {
                    message = new ReadMessage(getRandId(), getRandId());
                }
            }
        }
        super.uponReadMessage(message, sender, sourceProto, channelId);
    }

    @Override
    protected void uponReadAck(ReadAck message, final Host sender, final short sourceProto, final int channelId) {
        switch (currentFaultType) {
            case MessageDrop -> {
                if (injectMessageDrop(message)) {
                    return;
                }
            }
            case MessageTinker -> {
                if (injectMessageTinker(message)) {
                    message = new ReadAck(getRandId(), getRandEntry());
                }
            }
        }
        super.uponReadAck(message, sender, sourceProto, channelId);
    }

    @Override
    protected void uponWriteMessage(WriteMessage message, final Host sender, final short sourceProto, final int channelId) {
        switch (currentFaultType) {
            case MessageDrop -> {
                if (injectMessageDrop(message)) {
                    return;
                }
            }
            case MessageTinker -> {
                if (injectMessageTinker(message)) {
                    message = new WriteMessage(getRandId(), getRandId(), getRandEntry());
                }
            }
        }
        super.uponWriteMessage(message, sender, sourceProto, channelId);
    }

    @Override
    protected void uponWriteAck(WriteAck message, final Host sender, final short sourceProto, final int channelId) {
        switch (currentFaultType) {
            case MessageDrop -> {
                if (injectMessageDrop(message)) {
                    return;
                }
            }
            case MessageTinker -> {
                if (injectMessageTinker(message)) {
                    message = new WriteAck(getRandId(), getRandEntry());
                }
            }
        }
        super.uponWriteAck(message, sender, sourceProto, channelId);
    }

    @Override
    protected void uponWriteBackMessage(WriteBackMessage message, final Host sender, final short sourceProto, final int channelId) {
        switch (currentFaultType) {
            case MessageDrop -> {
                if (injectMessageDrop(message)) {
                    return;
                }
            }
            case MessageTinker -> {
                if (injectMessageTinker(message)) {
                    message = new WriteBackMessage(getRandId(), getRandId(), getRandEntry());
                }
            }
        }
        super.uponWriteBackMessage(message, sender, sourceProto, channelId);
    }

    @Override
    protected void uponWriteBackAck(WriteBackAck message, final Host sender, final short sourceProto, final int channelId) {
        switch (currentFaultType) {
            case MessageDrop -> {
                if (injectMessageDrop(message)) {
                    return;
                }
            }
            case MessageTinker -> {
                if (injectMessageTinker(message)) {
                    message = new WriteBackAck(getRandId(), getRandEntry());
                }
            }
        }
        super.uponWriteBackAck(message, sender, sourceProto, channelId);
    }


    /* -------------- TIMER EVENT HANDLERS -------------- */


    private void uponFailureTimer(final FailureTimer timer, final long timerId) {
        if (enabledFaults.isEmpty()) {
            return;
        } else if (rand.nextFloat() <= faultProbability) {
            final List<FaultType> eligible = new ArrayList<>(enabledFaults);
            currentFaultType = eligible.get(rand.nextInt(enabledFaults.size()));
            if (currentFaultType == FaultType.Connection) {
                injectConnectionFailure();
                currentFaultType = FaultType.NoFailure;
            }
        }

        setupTimer(timer, getRandomFaultDelay());
    }


    /* -------------- ERROR INJECTION METHODS -------------- */


    private boolean injectMessageDrop(final ProtoMessage message) {
        if (enabledFaults.contains(FaultType.MessageDrop)) {
            logger.warn("Injecting MessageDrop failure on {}", message);
            currentFaultType = FaultType.NoFailure;
            return true;
        }

        return false;
    }


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
            if (rand.nextFloat() <= CONN_FAILURE_PROBABILITY) {
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


    /* -------------- AUX METHODS -------------- */

    private String getRandId() {
        return UUID.randomUUID().toString();
    }

    private byte[] getRandBytes() {
        final byte[] tinkeredBytes = new byte[rand.nextInt(ClientProtocol.BYTES_LEN)];
        rand.nextBytes(tinkeredBytes);
        return tinkeredBytes;
    }

    private Tag getRandTag() {
        return new Tag(rand.nextInt(10000), rand.nextInt(10000));
    }

    private Database.Entry getRandEntry() {
        return new Database.Entry(getRandTag(), getRandBytes());
    }

    private long getRandomFaultDelay() {
        return minDelay + (long)(rand.nextFloat() * (maxDelay - minDelay));
    }

    private long getRandomConnFailTime() {
        return connFailMinTime + (long)(rand.nextFloat() * (connFailMaxTime - connFailMinTime));
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
