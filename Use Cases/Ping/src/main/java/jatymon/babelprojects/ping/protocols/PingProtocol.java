package jatymon.babelprojects.ping.protocols;

import jatymon.babelprojects.ping.ConfigKeys;
import jatymon.babelprojects.ping.messages.EchoReplyMessage;
import jatymon.babelprojects.ping.messages.EchoRequestMessage;
import jatymon.babelprojects.ping.notifications.connection.ConnectionDownNotification;
import jatymon.babelprojects.ping.notifications.connection.ConnectionUpNotification;
import jatymon.babelprojects.ping.notifications.messages.ReceivedEchoReply;
import jatymon.babelprojects.ping.notifications.messages.ReceivedEchoRequest;
import jatymon.babelprojects.ping.requests.BroadcastMessageRequest;
import jatymon.babelprojects.ping.requests.SendMessageRequest;
import jatymon.babelprojects.ping.timers.PingTimer;
import jatymon.babelprojects.ping.utils.NetworkUtils;
import jatymon.annotations.Typestate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.fct.di.novasys.babel.core.GenericProtocol;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.network.data.Host;

import java.net.UnknownHostException;
import java.util.Properties;

@Typestate("Ping")
public class PingProtocol extends GenericProtocol {
    public static final String PROTO_NAME = "Ping";
    public static final short PROTO_ID = 101;
    private static final Logger logger = LogManager.getLogger(PingProtocol.class);

    protected int peersSize;
    private long pingTimeout;

    public PingProtocol() {
        super(PROTO_NAME, PROTO_ID);
    }

    @Override
    public void init(final Properties properties) throws HandlerRegistrationException, UnknownHostException {
        pingTimeout = Long.parseLong(properties.getProperty(ConfigKeys.PING_TIMEOUT_CONFIG));
        peersSize = NetworkUtils.setupPeers(properties.getProperty(ConfigKeys.PEERS_CONFIG)).size();

        subscribeNotification(ConnectionUpNotification.ID, this::uponConnectionUp);
        subscribeNotification(ConnectionDownNotification.ID, this::uponConnectionDown);
        subscribeNotification(ReceivedEchoRequest.ID, this::uponReceivedEchoRequest);
        subscribeNotification(ReceivedEchoReply.ID, this::uponReceivedEchoReply);
        registerTimerHandler(PingTimer.TIMER_ID, this::uponPingTimer);
    }

    /* TYPESTATE METHODS */


    public void uponConnectionUp(final ConnectionUpNotification notification, final short sourceProto) {
        logger.info("Connected to {}", notification.getHost());

        // Begin ping!
        if (notification.getConnections() == peersSize - 1) {
            logger.info("Staring Ping!");
            setupPeriodicTimer(new PingTimer(), 0, pingTimeout);
        }
    }

    public void uponConnectionDown(final ConnectionDownNotification notification, final short sourceProto) {
        logger.info("Disconnected from {}", notification.getHost());
    }

    public void uponReceivedEchoRequest(final ReceivedEchoRequest notification, final short sourceProto) {
        final Host sender = notification.getSender();
        logger.info("Received an EchoRequest from {}", sender);
        sendRequest(new SendMessageRequest(new EchoReplyMessage(), sender), DispatcherProtocol.PROTO_ID);
    }

    public void uponReceivedEchoReply(final ReceivedEchoReply notification, final short sourceProto) {
        logger.info("Received an EchoReply from {}", notification.getSender());
    }

    public void broadcastEchoRequest() {
        sendRequest(new BroadcastMessageRequest(new EchoRequestMessage()), DispatcherProtocol.PROTO_ID);
    }


    /* TIMER HANDLERS */

    public void uponPingTimer(final PingTimer timer, final long timerId) {
        broadcastEchoRequest();
    }
}
