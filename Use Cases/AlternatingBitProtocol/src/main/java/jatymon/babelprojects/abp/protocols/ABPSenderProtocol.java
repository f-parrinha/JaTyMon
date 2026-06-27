package jatymon.babelprojects.abp.protocols;

import jatymon.annotations.Typestate;
import jatymon.babelprojects.abp.ConfigKeys;
import jatymon.babelprojects.abp.messages.AckMessage;
import jatymon.babelprojects.abp.messages.BitMessage;
import jatymon.babelprojects.abp.notifications.connection.ConnectionDownNotification;
import jatymon.babelprojects.abp.notifications.connection.ConnectionUpNotification;
import jatymon.babelprojects.abp.notifications.messages.ReceivedAckMessageNotification;
import jatymon.babelprojects.abp.requests.BroadcastMessageRequest;
import jatymon.babelprojects.abp.timers.SendBitTimer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.fct.di.novasys.babel.core.GenericProtocol;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.network.data.Host;

import java.io.IOException;
import java.util.Properties;

@Typestate("ABPSender")
public class ABPSenderProtocol extends GenericProtocol {
    public static final String PROTO_NAME = "ABPSender";
    public static final short PROTO_ID = 103;
    private static final Logger logger = LogManager.getLogger(ABPSenderProtocol.class);

    private byte bit;
    private long sendTimerId;
    private long sendBitTimeout;

    public ABPSenderProtocol() {
        super(PROTO_NAME, PROTO_ID);
        bit = 0;
    }

    @Override
    public void init(final Properties props) throws HandlerRegistrationException, IOException {
        final int port = Integer.parseInt(props.getProperty(ConfigKeys.PORT_CONFIG));
        final int senderPort = Integer.parseInt(props.getProperty(ConfigKeys.SENDER_CONFIG));
        if (port != 0 && port != senderPort) {
            // Do not set up the protocol if myself is not configured to be the sender
            return;
        }

        sendBitTimeout = Long.parseLong(props.getProperty(ConfigKeys.SEND_BIT_TIMEOUT));

        registerTimerHandler(SendBitTimer.ID, this::uponSendBitTimer);

        subscribeNotification(ReceivedAckMessageNotification.ID, this::receiveAckMessage);
        subscribeNotification(ConnectionUpNotification.ID, this::uponConnectionUp);
        subscribeNotification(ConnectionDownNotification.ID, this::uponConnectionDown);
    }

    protected void uponConnectionUp(final ConnectionUpNotification notification, final short protoId) {
        logger.info("Connection up with {}", notification.getHost());
        sendTimerId = setupPeriodicTimer(new SendBitTimer(), sendBitTimeout, sendBitTimeout);
    }

    protected void uponConnectionDown(final ConnectionDownNotification notification, final short protoId) {
        final Host host = notification.getHost();
        logger.info("Connection down with {}", host);
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

    protected void uponSendBitTimer(final SendBitTimer timer, final long timerId) {
        final BitMessage message = new BitMessage(bit);
        logger.info("Sending {}", message);
        sendRequest(new BroadcastMessageRequest(message), DispatcherProtocol.PROTO_ID);
    }
}
