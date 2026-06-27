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
import jatymon.babelprojects.abp.requests.ConnectionRequest;
import jatymon.babelprojects.abp.requests.SendMessageRequest;
import jatymon.babelprojects.abp.timers.ConnectionRetryTimer;
import jatymon.babelprojects.abp.utils.NetworkUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.fct.di.novasys.babel.core.GenericProtocol;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.babel.generic.ProtoMessage;
import pt.unl.fct.di.novasys.channel.tcp.TCPChannel;
import pt.unl.fct.di.novasys.channel.tcp.events.*;
import pt.unl.fct.di.novasys.network.data.Host;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

/**
 * The dispatcher is used to establish connections and delegate the messages to the corresponding sub-protocols (sender/receiver).
 * It is considered that the implementation of the AlternatingBitProtocol is divided in two roles, and consequentially, in two
 * different protocols: the {@code ABPSender} and the {@code ABPReceiver}. The dispatcher plays a vital role because both protocols
 * share the same channel.
 */
@Typestate("Dispatcher")
public class DispatcherProtocol extends GenericProtocol {
    public static final String PROTO_NAME = "Dispatcher";
    public static final short PROTO_ID = 100;
    private static final Logger logger = LogManager.getLogger(DispatcherProtocol.class);

    protected final Set<Host> connections;
    protected final Set<Host> peers;
    protected int channelId;
    protected Host myself;

    // Typestate variable
    protected int conns;

    public DispatcherProtocol() {
        this(PROTO_NAME, PROTO_ID);
    }

    public DispatcherProtocol(final String protoName, final short protoId) {
        super(protoName, protoId);
        this.peers = new TreeSet<>();
        this.connections = new TreeSet<>();
    }

    @Override
    public void init(final Properties props) throws HandlerRegistrationException, IOException {
        final InetAddress address = InetAddress.getByName(props.getProperty(ConfigKeys.ADDRESS_CONFIG));
        final String port = props.getProperty(ConfigKeys.PORT_CONFIG);
        this.myself = new Host(address, Integer.parseInt(port));
        this.peers.addAll(NetworkUtils.setupPeers(props.getProperty(ConfigKeys.PEERS_CONFIG)));
        this.channelId = createTcpChannel(address.getHostAddress(), port);

        registerChannelEventHandler(channelId, OutConnectionFailed.EVENT_ID, this::uponOutConnectionFailed);
        registerChannelEventHandler(channelId, OutConnectionUp.EVENT_ID, this::uponOutConnectionUp);
        registerChannelEventHandler(channelId, OutConnectionDown.EVENT_ID, this::uponOutConnectionDown);
        registerChannelEventHandler(channelId, InConnectionUp.EVENT_ID, this::uponInConnectionUp);
        registerChannelEventHandler(channelId, InConnectionDown.EVENT_ID, this::uponInConnectionDown);
        registerMessageHandler(channelId, BitMessage.ID, this::uponBitMessage);
        registerMessageHandler(channelId, AckMessage.ID, this::uponAckMessage);
        registerMessageSerializer(channelId, BitMessage.ID, BitMessage.serializer);
        registerMessageSerializer(channelId, AckMessage.ID, AckMessage.serializer);
        registerRequestHandler(SendMessageRequest.ID, this::uponSendMessageRequest);
        registerRequestHandler(BroadcastMessageRequest.ID, this::uponBroadcastMessageRequest);
        registerRequestHandler(ConnectionRequest.ID, this::uponConnectionRequest);
        registerTimerHandler(ConnectionRetryTimer.ID, this::uponConnectionRetryTimer);

        connectAll();
    }

    /* MESSAGE EVENT HANDLERS */

    protected void uponBitMessage(final BitMessage message, final Host sender, final short sourceProto, final int channelId) {
        triggerNotification(new ReceivedBitMessageNotification(message, sender));
    }

    protected void uponAckMessage(final AckMessage message, final Host sender, final short sourceProto, final int channelId) {
        triggerNotification(new ReceivedAckMessageNotification(message, sender));
    }

    /* REQUEST EVENT HANDLERS */

    protected void uponSendMessageRequest(final SendMessageRequest request, short sourceProto) {
        sendMessage(channelId, request.getMessage(), request.getDestination());
    }

    protected void uponBroadcastMessageRequest(final BroadcastMessageRequest request, short sourceProto) {
        for (final Host host : connections) {
            sendMessage(channelId, request.getMessage(), host);
        }
    }

    protected void uponConnectionRequest(final ConnectionRequest request, short sourceProto) {
        final Host host = request.getHost();
        if (connections.contains(host)) {
            return;
        }

        setupTimer(new ConnectionRetryTimer(host, channelId), request.getDelay());
    }

    /* CONNECTION EVENT HANDLERS */

    protected void uponInConnectionUp(final InConnectionUp event, final int channel) {
        final Host host = event.getNode();
        if (connections.add(host)) {
            triggerNotification(new ConnectionUpNotification(event.getNode(), connections.size(), peers.size(), channelId));
        }

        conns = connections.size();
    }

    protected void uponInConnectionDown(final InConnectionDown event, final int channel) {
        final Host host = event.getNode();
        if (connections.remove(host)) {
            triggerNotification(new ConnectionDownNotification(event.getNode(), connections.size(), channelId));
        }

        conns = connections.size();
    }

    protected void uponOutConnectionFailed(final OutConnectionFailed<ProtoMessage> event, final int channel) {
        setupTimer(new ConnectionRetryTimer(event.getNode(), channel), ConnectionRetryTimer.TIMEOUT);
    }

    protected void uponOutConnectionUp(final OutConnectionUp event, final int channel) {
        final Host host = event.getNode();
        if (connections.add(host)) {
            triggerNotification(new ConnectionUpNotification(event.getNode(), connections.size(), peers.size(), channelId));
        }

        conns = connections.size();
    }

    protected void uponOutConnectionDown(final OutConnectionDown event, final int channel) {
        final Host host = event.getNode();
        if (connections.remove(host)) {
            triggerNotification(new ConnectionDownNotification(event.getNode(), connections.size(), channelId));
        }

        conns = connections.size();
    }

    /* TIMER EVENT HANDLERS */

    private void uponConnectionRetryTimer(final ConnectionRetryTimer timer, long timerId) {
        final Host host = timer.getHost();
        logger.info("Reconnecting with {}", host);
        openConnection(host, channelId);
    }

    /* AUX METHODS */

    protected void connectAll() {
        for (final Host host : peers) {
            if (host.equals(myself) || connections.contains(host)) continue;
            logger.info("Connecting with {}", host);
            openConnection(host, channelId);
        }
    }

    protected void disconnectAll() {
        for (final Host host : connections) {
            logger.info("Disconnecting from {}", host);
            closeConnection(host);
        }
    }

    protected int createTcpChannel(final String myAddress, final String port) {
        try {
            var channelProps = new Properties();
            channelProps.setProperty(TCPChannel.ADDRESS_KEY, myAddress);
            channelProps.setProperty(TCPChannel.PORT_KEY, port);
            channelProps.setProperty(TCPChannel.HEARTBEAT_INTERVAL_KEY, "1000");
            channelProps.setProperty(TCPChannel.HEARTBEAT_TOLERANCE_KEY, "3000");
            channelProps.setProperty(TCPChannel.CONNECT_TIMEOUT_KEY, "1000");
            return createChannel(TCPChannel.NAME, channelProps);
        } catch (IOException e) {
            e.fillInStackTrace();
            throw new RuntimeException(e);
        }
    }
}
