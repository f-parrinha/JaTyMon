package jatymon.babelprojects.ping.protocols;

import jatymon.annotations.Typestate;
import jatymon.babelprojects.ping.ConfigKeys;
import jatymon.babelprojects.ping.messages.EchoRequestMessage;
import jatymon.babelprojects.ping.notifications.connection.ConnectionDownNotification;
import jatymon.babelprojects.ping.notifications.connection.ConnectionUpNotification;
import jatymon.babelprojects.ping.notifications.messages.ReceivedEchoReply;
import jatymon.babelprojects.ping.requests.BroadcastMessageRequest;
import jatymon.babelprojects.ping.timers.PingTimer;
import jatymon.babelprojects.ping.utils.NetworkUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.fct.di.novasys.babel.core.GenericProtocol;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;

import java.net.UnknownHostException;
import java.util.Properties;

@Typestate("PingSender")
public class PingSenderProtocol extends GenericProtocol {
    public static final String NAME = "SenderPing";
    public static final short ID = 103;
    private static final Logger logger = LogManager.getLogger(PingSenderProtocol.class);

    protected int peersSize;
    
    private long pingTimeout;

    public PingSenderProtocol() {
        super(NAME, ID);
    }

    @Override
    public void init(final Properties properties) throws HandlerRegistrationException, UnknownHostException {
        peersSize = NetworkUtils.setupPeers(properties.getProperty(ConfigKeys.PEERS_CONFIG)).size();
        pingTimeout = Long.parseLong(properties.getProperty(ConfigKeys.PING_TIMEOUT_CONFIG));

        subscribeNotification(ConnectionDownNotification.ID, this::uponConnectionDown);
        subscribeNotification(ConnectionUpNotification.ID, this::uponConnectionUp);
        subscribeNotification(ReceivedEchoReply.ID, this::uponReceiveEchoReply);
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

    public void uponReceiveEchoReply(final ReceivedEchoReply notification, final short sourceProto) {
        logger.info("Received an EchoReply from {}", notification.getSender());
    }

    public void broadcastEchoRequest() {
        sendRequest(new BroadcastMessageRequest(new EchoRequestMessage()), DispatcherProtocol.PROTO_ID);
    }

    /* TIMER HANDLERS */

    private void uponPingTimer(final PingTimer timer, final long timerId) {
        broadcastEchoRequest();
    }

}
