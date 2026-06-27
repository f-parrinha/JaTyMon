package jatymon.babelprojects.abp.protocols;

import jatymon.annotations.Typestate;
import jatymon.babelprojects.abp.ConfigKeys;
import jatymon.babelprojects.abp.messages.AckMessage;
import jatymon.babelprojects.abp.messages.BitMessage;
import jatymon.babelprojects.abp.notifications.connection.ConnectionDownNotification;
import jatymon.babelprojects.abp.notifications.connection.ConnectionUpNotification;
import jatymon.babelprojects.abp.notifications.messages.ReceivedBitMessageNotification;
import jatymon.babelprojects.abp.requests.SendMessageRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.fct.di.novasys.babel.core.GenericProtocol;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.network.data.Host;

import java.io.IOException;
import java.util.Properties;

@Typestate("ABPReceiver")
public class ABPReceiverProtocol extends GenericProtocol {
    public static final String PROTO_NAME = "ABPReceiver";
    public static final short PROTO_ID = 102;
    private static final Logger logger = LogManager.getLogger(ABPReceiverProtocol.class);

    private byte lastAckBit;

    public ABPReceiverProtocol() {
        super(PROTO_NAME, PROTO_ID);

        // Must be different than 0 and 1
        this.lastAckBit = 2;
    }

    @Override
    public void init(final Properties props) throws HandlerRegistrationException, IOException {
        final int port = Integer.parseInt(props.getProperty(ConfigKeys.PORT_CONFIG));
        final int receiverPort = Integer.parseInt(props.getProperty(ConfigKeys.RECEIVER_CONFIG));

        // Do not set up the protocol if myself is not configured to be the sender
        if (port != 0 && port != receiverPort) {
            return;
        }

        subscribeNotification(ReceivedBitMessageNotification.ID, this::receiveBitMessage);
        subscribeNotification(ConnectionUpNotification.ID, this::uponConnectionUp);
        subscribeNotification(ConnectionDownNotification.ID, this::uponConnectionDown);
    }

    protected void uponConnectionUp(final ConnectionUpNotification notification, final short protoId) {
        logger.info("Connection up with {}", notification.getHost());
    }

    protected void uponConnectionDown(final ConnectionDownNotification notification, final short protoId) {
        final Host host = notification.getHost();
        logger.info("Connection down with {}", host);
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
}
