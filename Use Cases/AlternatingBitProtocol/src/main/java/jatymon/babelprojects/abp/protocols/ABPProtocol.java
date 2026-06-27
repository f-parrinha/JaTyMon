package jatymon.babelprojects.abp.protocols;

import jatymon.annotations.Typestate;
import jatymon.babelprojects.abp.ConfigKeys;
import jatymon.babelprojects.abp.messages.AckMessage;
import jatymon.babelprojects.abp.messages.BitMessage;
import jatymon.babelprojects.abp.notifications.connection.ConnectionDownNotification;
import jatymon.babelprojects.abp.notifications.connection.ConnectionUpNotification;
import jatymon.babelprojects.abp.notifications.messages.ReceivedAckMessageNotification;
import jatymon.babelprojects.abp.notifications.messages.ReceivedBitMessageNotification;
import jatymon.babelprojects.abp.requests.BroadcastMessageRequest;
import jatymon.babelprojects.abp.requests.SendMessageRequest;
import jatymon.babelprojects.abp.timers.SendBitTimer;
import jatymon.babelprojects.abp.utils.NetworkUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.fct.di.novasys.babel.core.GenericProtocol;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.network.data.Host;

import java.io.IOException;
import java.util.Properties;

/**
 * This is an implementation of the AlternatingBitProtocol. It does not separate into two distinct roles: sender and receiver.
 * The class contains then, the logic for both participants. This version of the protocol can be executed with multiple participants.
 */
@Typestate("ABP")
public class ABPProtocol extends GenericProtocol {
    public static final short PROTO_ID = 101;
    public static final String PROTO_NAME = "ABPDual";
    private static final Logger logger = LogManager.getLogger(ABPProtocol.class);

    protected int peersSize;

    private byte bit;
    private byte lastAckBit;
    private long sendTimerId;
    private int conns;
    private long sendBitTimeout;

    public ABPProtocol() {
        super(PROTO_NAME, PROTO_ID);
    }

    @Override
    public void init(final Properties props) throws HandlerRegistrationException, IOException {
        conns = 0;
        peersSize = NetworkUtils.setupPeers(props.getProperty(ConfigKeys.PEERS_CONFIG)).size();
        sendBitTimeout = Long.parseLong(props.getProperty(ConfigKeys.SEND_BIT_TIMEOUT));
        registerTimerHandler(SendBitTimer.ID, this::uponSendBitTimer);

        subscribeNotification(ReceivedAckMessageNotification.ID, this::receiveAckMessage);
        subscribeNotification(ReceivedBitMessageNotification.ID, this::receiveBitMessage);
        subscribeNotification(ConnectionUpNotification.ID, this::uponConnectionUp);
        subscribeNotification(ConnectionDownNotification.ID, this::uponConnectionDown);
    }


    protected void uponConnectionUp(final ConnectionUpNotification notification, final short protoId) {
        logger.info("Connection up with {}", notification.getHost());
        conns = notification.getConnections();

        if (conns == peersSize - 1) {
            sendTimerId = setupPeriodicTimer(new SendBitTimer(), sendBitTimeout, sendBitTimeout);
        }
    }

    protected void uponConnectionDown(final ConnectionDownNotification notification, final short protoId) {
        final Host host = notification.getHost();
        logger.info("Connection down with {}", host);
        conns = notification.getConnections();
        cancelTimer(sendTimerId);
    }

    protected void receiveAckMessage(final ReceivedAckMessageNotification notification, short protoId) {
        final AckMessage message = notification.getMessage();
        if (message.getBit() == bit) {
            logger.info("Received {} from {}", message, notification.getSender());
            bit ^= 1;
        } else {
            logger.info("Received outdated AckMessage for bit {}. Ignoring", message.getBit());
        }
    }

    protected void receiveBitMessage(final ReceivedBitMessageNotification notification, final short protoId) {
        final Host sender = notification.getSender();
        final BitMessage message = notification.getMessage();
        final byte receivedBit = message.getBit();
        if (receivedBit == lastAckBit) {
            logger.info("Received duplicate BitMessage. Re-sending AckMessage for bit {}", lastAckBit);
            sendAckMessage(sender, lastAckBit);
            return;
        }

        lastAckBit = receivedBit;
        logger.info("Received {} from {}", message, notification.getSender());
        sendAckMessage(sender, message.getBit());
    }

    protected void sendAckMessage(final Host host, byte bit) {
        final AckMessage message = new AckMessage(bit);
        logger.info("Sending {} to {}", message, host);
        sendRequest(new SendMessageRequest(message, host), DispatcherProtocol.PROTO_ID);
    }

    protected void uponSendBitTimer(final SendBitTimer timer, final long timerId) {
        final BitMessage message = new BitMessage(bit);
        logger.info("Sending {}", message);
        sendRequest(new BroadcastMessageRequest(message), DispatcherProtocol.PROTO_ID);
    }
}
