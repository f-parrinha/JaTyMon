package jatymon.babelprojects.abdquorum.protocols.client;

import jatymon.annotations.Typestate;
import jatymon.babelprojects.abdquorum.ConfigKeys;
import jatymon.babelprojects.abdquorum.utils.NetworkUtils;
import jatymon.babelprojects.abdquorum.messages.StopMessage;
import jatymon.babelprojects.abdquorum.messages.client.ClientReadAck;
import jatymon.babelprojects.abdquorum.messages.client.ClientReadMessage;
import jatymon.babelprojects.abdquorum.messages.client.ClientWriteMessage;
import jatymon.babelprojects.abdquorum.notifications.messages.client.ClientReadAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.client.ClientWriteAckNotification;
import jatymon.babelprojects.abdquorum.protocols.dispatcher.DispatcherProtocol;
import jatymon.babelprojects.abdquorum.requests.BroadcastMessageRequest;
import jatymon.babelprojects.abdquorum.requests.SendMessageRequest;
import jatymon.babelprojects.abdquorum.timers.ClientRetryTimer;
import jatymon.babelprojects.abdquorum.timers.ClientStartTimer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.fct.di.novasys.babel.core.GenericProtocol;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.network.data.Host;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.*;

@Typestate("ProbabilisticClient")
public class ClientProtocol extends GenericProtocol {
    public static final String PROTO_NAME = "ClientProtocol";
    public static final short PROTO_ID = 104;
    public static final int BYTES_LEN = 16;         // 128-bit data

    private static final Logger logger = LogManager.getLogger(ClientProtocol.class);

    private final Random rand;
    private final String clientId;

    private long retryTimerId;

    // For statistics
    private long readStartTime;
    private long writeStartTime;
    private double writeLatencySum = 0;
    private double readLatencySum = 0;
    private int writeLatencies = 0;
    private int readLatencies = 0;
    protected int opsNumb;
    private int opsCount;
    private int badWritesCount;
    private Timestamp startTimestamp;

    // For main client execution
    private byte[] lastWrittenValue;
    private Host[] peers;

    public ClientProtocol() {
        super(PROTO_NAME, PROTO_ID);
        this.rand = new Random();
        this.clientId = UUID.randomUUID().toString();
    }

    @Override
    public void init(final Properties props) throws HandlerRegistrationException, IOException {
        final long startDelay = Long.parseLong(props.getProperty(ConfigKeys.CLIENT_START_DELAY));
        final String port = props.getProperty(ConfigKeys.PORT_CONFIG);
        final List<String> clientsPorts = Arrays.stream(props.getProperty(ConfigKeys.CLIENTS_PORTS_CONFIG)
                .split(","))
                .map(String::strip)
                .toList();

        // Do not set up the protocol if it is not a client
        if (!clientsPorts.contains(port)) {
            return;
        }

        // Setup client state
        logger.info("{} is a Client", port);
        this.opsNumb = Integer.parseInt(props.getProperty(ConfigKeys.CLIENT_OPS_NUMB));
        this.peers = NetworkUtils.setupPeers(props.getProperty(ConfigKeys.PEERS_CONFIG)).toArray(new Host[0]);
        this.opsCount = 0;
        this.badWritesCount = 0;
        this.writeStartTime = -1;
        this.readStartTime = -1;
        this.lastWrittenValue = new byte[0];
        this.retryTimerId = -1;

        // Setup handlers
        registerTimerHandler(ClientStartTimer.ID, this::uponClientStartTimer);
        registerTimerHandler(ClientRetryTimer.ID, this::uponClientRetryTimer);
        subscribeNotification(ClientWriteAckNotification.ID, this::uponClientWriteAck);
        subscribeNotification(ClientReadAckNotification.ID, this::uponClientReadAck);

        setupTimer(new ClientStartTimer(), startDelay);
    }

    public void startClient(final Op op) {
        logger.info("Starting client operations");
        startTimestamp = Timestamp.from(Instant.now());

        if (op == Op.WRITE) {
            sendWriteMessage(true);
        } else {
            sendReadMessage(true);
        }
    }

    public void stopClient() {
        System.out.print("\n");
        logger.info("Client finished");
        showStatistics();
        sendRequest(new BroadcastMessageRequest(new StopMessage()), DispatcherProtocol.PROTO_ID);
    }

    public Op getFirstOp() {
        return rand.nextInt() == 0 ? Op.WRITE : Op.READ;
    }


    /* ------------ ACK HANDLERS ------------ */


    protected void uponClientWriteAck(final ClientWriteAckNotification notification, final short protoId) {
        collectWriteStatistics();

        logger.debug("Received {} from {}", notification.getMessage(), notification.getSender());
        sendReadMessage(true);
    }

    protected void uponClientReadAck(final ClientReadAckNotification notification, final short protoId) {
        collectReadStatistics();

        final ClientReadAck message = notification.getMessage();
        if (!clientId.equals(message.getClientId())) {
            logger.warn("Wrong reply destination. Client with id {} received an ACK for client with id {}", clientId, message.getClientId());
            return;
        }

        final byte[] readValue = message.getValue();
        if (!Arrays.equals(readValue, lastWrittenValue)) {
            badWritesCount++;
            logger.warn("Bad client operation. Last written value: {}. Read value: {}",
                    HexFormat.of().formatHex(lastWrittenValue),
                    HexFormat.of().formatHex(readValue));
        }

        logger.debug("Received {} from {}", message, notification.getSender());
        sendWriteMessage(true);
    }


    /* ------------ TIMER EVENT HANDLERS ------------ */


    protected void uponClientStartTimer(final ClientStartTimer timer, final long timerId) {
        final Op firstOp = getFirstOp();
        startClient(firstOp);
    }

    protected void uponClientRetryTimer(final ClientRetryTimer timer, final long timerId) {
        final Op op = timer.getOp();
        switch (op) {
            case WRITE -> sendWriteMessage(false);
            case READ -> sendReadMessage(false);
        }
    }


    /* ------------ SEND METHODS ------------ */


    protected void sendWriteMessage(final boolean updateState) {
        cancelTimer(retryTimerId);
        if (opsCount == opsNumb) {
            stopClient();
            return;
        }

        printProgress();

        if (updateState) {
            opsCount++;
            writeStartTime = System.currentTimeMillis();
            lastWrittenValue = getRandomValue();
        }

        final ClientWriteMessage message = new ClientWriteMessage(clientId, lastWrittenValue);
        final Host targetHost = getRandomPeer();
        sendRequest(new SendMessageRequest(message, targetHost), DispatcherProtocol.PROTO_ID);

        retryTimerId = setupTimer(new ClientRetryTimer(Op.WRITE), ClientRetryTimer.TIMEOUT);
        logger.debug("Sending {} to {}", message, targetHost);
    }

    protected void sendReadMessage(final boolean updateState) {
        cancelTimer(retryTimerId);

        if (updateState) {
            readStartTime = System.currentTimeMillis();
        }

        final ClientReadMessage message = new ClientReadMessage(clientId);
        final Host targetHost = getRandomPeer();
        sendRequest(new SendMessageRequest(message, targetHost), DispatcherProtocol.PROTO_ID);

        retryTimerId = setupTimer(new ClientRetryTimer(Op.READ), ClientRetryTimer.TIMEOUT);
        logger.debug("Sending {} to {}", message, targetHost);
    }


    /* ------------ STATISTIC METHODS ------------ */


    protected void collectWriteStatistics() {
        if (writeStartTime == -1) {
            return;
        }
        writeLatencies++;
        writeLatencySum += System.currentTimeMillis() - writeStartTime;
        writeStartTime = -1;
    }

    protected void collectReadStatistics() {
        if (readStartTime == -1) {
            return;
        }
        readLatencies++;
        readLatencySum += System.currentTimeMillis() - readStartTime;
        readStartTime = -1;
    }


    /* ---------- AUX METHODS ---------- */


    private Host getRandomPeer() {
        final int idx = rand.nextInt(peers.length);
        return peers[idx];
    }

    private byte[] getRandomValue() {
        final byte[] value = new byte[BYTES_LEN];
        rand.nextBytes(value);
        return value;
    }

    private void showStatistics() {
        final long elapsedMillis = System.currentTimeMillis() - startTimestamp.getTime();
        final double elapsedSeconds = elapsedMillis / 1000.0;
        double avgWriteLatency = writeLatencies == 0 ? 0 : writeLatencySum / writeLatencies;
        double avgReadLatency = readLatencies == 0 ? 0 : readLatencySum / readLatencies;

        logger.info("Total writes: {}", opsCount);
        logger.info("Bad writes: {}", badWritesCount);
        logger.info("Bad write percentage: {}", (float) badWritesCount / opsCount);
        logger.info("Average write latency (ms): {}", avgWriteLatency);
        logger.info("Average read latency (ms): {}", avgReadLatency);
        logger.info("Elapsed time (s): {}", elapsedSeconds);
    }

    private void printProgress() {
        int percent = (int) ((opsCount / (double) opsNumb) * 100);

        System.out.print("\rProgress: " + opsCount + "/" + opsNumb + " (" + percent + "%)");
        System.out.flush();
    }

    /**
     * Enum containing the types of operations a client can do
     */
    public enum Op { READ, WRITE }
}
