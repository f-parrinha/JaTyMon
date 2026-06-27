package jatymon.babelprojects.ping.protocols;

import jatymon.annotations.Typestate;
import jatymon.babelprojects.ping.ConfigKeys;
import jatymon.babelprojects.ping.messages.EchoReplyMessage;
import jatymon.babelprojects.ping.notifications.connection.ConnectionDownNotification;
import jatymon.babelprojects.ping.notifications.connection.ConnectionUpNotification;
import jatymon.babelprojects.ping.notifications.messages.ReceivedEchoRequest;
import jatymon.babelprojects.ping.requests.SendMessageRequest;
import jatymon.babelprojects.ping.utils.NetworkUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.fct.di.novasys.babel.core.GenericProtocol;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.network.data.Host;

import java.net.UnknownHostException;
import java.util.Properties;

@Typestate("PingReceiver")
public class PingReceiverProtocol extends GenericProtocol {
    public static final String NAME = "ReceiverPing";
    public static final short ID = 102;
    private static final Logger logger = LogManager.getLogger(PingReceiverProtocol.class);

    protected int peersSize;

    public PingReceiverProtocol() {
        super(NAME, ID);
    }

    @Override
    public void init(final Properties properties) throws HandlerRegistrationException, UnknownHostException {
        peersSize = NetworkUtils.setupPeers(properties.getProperty(ConfigKeys.PEERS_CONFIG)).size();

        subscribeNotification(ConnectionUpNotification.ID, this::uponConnectionUp);
        subscribeNotification(ConnectionDownNotification.ID, this::uponConnectionDown);
        subscribeNotification(ReceivedEchoRequest.ID, this::uponReceivedEchoRequest);
    }

    /* TYPESTATE METHODS */

    public void uponConnectionUp(final ConnectionUpNotification notification, final short sourceProto) {
        logger.info("Connected to {}", notification.getHost());
    }

    public void uponConnectionDown(final ConnectionDownNotification notification, final short sourceProto) {
        logger.info("Disconnected from {}", notification.getHost());
    }

    public void uponReceivedEchoRequest(final ReceivedEchoRequest notification, final short sourceProto) {
        final Host sender = notification.getSender();
        logger.info("Received an EchoRequest from {}", sender);
        sendEchoReply(sender);
    }

    public void sendEchoReply(final Host sender) {
        sendRequest(new SendMessageRequest(new EchoReplyMessage(), sender), DispatcherProtocol.PROTO_ID);
    }
}
