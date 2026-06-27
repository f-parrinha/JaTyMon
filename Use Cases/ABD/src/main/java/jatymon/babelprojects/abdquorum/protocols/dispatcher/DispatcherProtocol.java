package jatymon.babelprojects.abdquorum.protocols.dispatcher;

import jatymon.annotations.Ext;
import jatymon.annotations.Typestate;
import jatymon.babelprojects.abdquorum.ConfigKeys;
import jatymon.babelprojects.abdquorum.messages.StopMessage;
import jatymon.babelprojects.abdquorum.messages.client.ClientReadAck;
import jatymon.babelprojects.abdquorum.messages.client.ClientReadMessage;
import jatymon.babelprojects.abdquorum.messages.client.ClientWriteAck;
import jatymon.babelprojects.abdquorum.messages.client.ClientWriteMessage;
import jatymon.babelprojects.abdquorum.messages.replica.readop.ReadAck;
import jatymon.babelprojects.abdquorum.messages.replica.readop.ReadMessage;
import jatymon.babelprojects.abdquorum.messages.replica.readop.WriteBackAck;
import jatymon.babelprojects.abdquorum.messages.replica.writeop.ReadTagAck;
import jatymon.babelprojects.abdquorum.messages.replica.writeop.ReadTagMessage;
import jatymon.babelprojects.abdquorum.messages.replica.readop.WriteBackMessage;
import jatymon.babelprojects.abdquorum.messages.replica.writeop.WriteAck;
import jatymon.babelprojects.abdquorum.messages.replica.writeop.WriteMessage;
import jatymon.babelprojects.abdquorum.notifications.connection.ConnectionDownNotification;
import jatymon.babelprojects.abdquorum.notifications.connection.ConnectionUpNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.client.ClientReadAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.client.ClientReadMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.client.ClientWriteAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.client.ClientWriteMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.readop.ReadAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.readop.ReadMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.readop.WriteBackAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.writeop.ReadTagAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.writeop.ReadTagMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.readop.WriteBackMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.writeop.WriteAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.writeop.WriteMessageNotification;
import jatymon.babelprojects.abdquorum.requests.BroadcastMessageRequest;
import jatymon.babelprojects.abdquorum.requests.ConnectionRequest;
import jatymon.babelprojects.abdquorum.requests.SendMessageRequest;
import jatymon.babelprojects.abdquorum.timers.ConnectionRetryTimer;
import jatymon.babelprojects.abdquorum.utils.NetworkUtils;
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
 * The dispatcher is used to establish connections and delegate messages to the corresponding sub-protocols.
 *  It is considered that the implementation of the AlternatingBitProtocol is divided in two roles, and consequentially, in two
 * different protocols: the {@code ABPSender} and the {@code ABPReceiver}. The dispatcher plays a vital role because both protocols
 * share the same channel. However, the project unifies all protocols with the dispatcher. It is always employed and any new protocol
 * should receive/send messages from/via the dispatcher.
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

    @Ext
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

        // Channel/system
        registerChannelEventHandler(channelId, OutConnectionFailed.EVENT_ID, this::uponOutConnectionFailed);
        registerChannelEventHandler(channelId, OutConnectionUp.EVENT_ID, this::uponOutConnectionUp);
        registerChannelEventHandler(channelId, OutConnectionDown.EVENT_ID, this::uponOutConnectionDown);
        registerChannelEventHandler(channelId, InConnectionUp.EVENT_ID, this::uponInConnectionUp);
        registerChannelEventHandler(channelId, InConnectionDown.EVENT_ID, this::uponInConnectionDown);
        registerMessageHandler(channelId, StopMessage.ID, this::uponStopMessage);
        registerMessageSerializer(channelId, StopMessage.ID, StopMessage.serializer);

        // Client messages
        registerMessageHandler(channelId, ClientReadMessage.ID, this::uponClientReadMessage);
        registerMessageHandler(channelId, ClientReadAck.ID, this::uponClientReadAck);
        registerMessageHandler(channelId, ClientWriteMessage.ID, this::uponClientWriteMessage);
        registerMessageHandler(channelId, ClientWriteAck.ID, this::uponClientWriteAck);
        registerMessageSerializer(channelId, ClientReadMessage.ID, ClientReadMessage.serializer);
        registerMessageSerializer(channelId, ClientReadAck.ID, ClientReadAck.serializer);
        registerMessageSerializer(channelId, ClientWriteMessage.ID, ClientWriteMessage.serializer);
        registerMessageSerializer(channelId, ClientWriteAck.ID, ClientWriteAck.serializer);

        // Internal quorum messages
        registerMessageHandler(channelId, ReadTagMessage.ID, this::uponReadTagMessage);
        registerMessageHandler(channelId, ReadTagAck.ID, this::uponReadTagAck);
        registerMessageHandler(channelId, ReadMessage.ID, this::uponReadMessage);
        registerMessageHandler(channelId, ReadAck.ID, this::uponReadAck);
        registerMessageHandler(channelId, WriteMessage.ID, this::uponWriteMessage);
        registerMessageHandler(channelId, WriteAck.ID, this::uponWriteAck);
        registerMessageHandler(channelId, WriteBackMessage.ID, this::uponWriteBackMessage);
        registerMessageHandler(channelId, WriteBackAck.ID, this::uponWriteBackAck);
        registerMessageSerializer(channelId, ReadTagMessage.ID, ReadTagMessage.serializer);
        registerMessageSerializer(channelId, ReadTagAck.ID, ReadTagAck.serializer);
        registerMessageSerializer(channelId, ReadMessage.ID, ReadMessage.serializer);
        registerMessageSerializer(channelId, ReadAck.ID, ReadAck.serializer);
        registerMessageSerializer(channelId, WriteMessage.ID, WriteMessage.serializer);
        registerMessageSerializer(channelId, WriteAck.ID, WriteAck.serializer);
        registerMessageSerializer(channelId, WriteBackMessage.ID, WriteBackMessage.serializer);
        registerMessageSerializer(channelId, WriteBackAck.ID, WriteBackAck.serializer);

        // Requests
        registerRequestHandler(SendMessageRequest.ID, this::uponSendMessageRequest);
        registerRequestHandler(BroadcastMessageRequest.ID, this::uponBroadcastMessageRequest);
        registerRequestHandler(ConnectionRequest.ID, this::uponConnectionRequest);
        registerTimerHandler(ConnectionRetryTimer.ID, this::uponConnectionRetryTimer);

        // Start
        connectAll();
    }


    /* -------------- CLIENT MESSAGE EVENT HANDLERS -------------- */


    protected void uponClientReadMessage(final ClientReadMessage message, final Host sender, final short sourceProto, final int channelId) {
        triggerNotification(new ClientReadMessageNotification(message, sender));
    }

    protected void uponClientReadAck(final ClientReadAck message, final Host sender, final short sourceProto, final int channelId) {
        triggerNotification(new ClientReadAckNotification(message, sender));
    }

    protected void uponClientWriteMessage(final ClientWriteMessage message, final Host sender, final short sourceProto, final int channelId) {
        triggerNotification(new ClientWriteMessageNotification(message, sender));
    }

    protected void uponClientWriteAck(final ClientWriteAck message, final Host sender, final short sourceProto, final int channelId) {
        triggerNotification(new ClientWriteAckNotification(message, sender));
    }


    /* -------------- QUORUM MESSAGE EVENT HANDLERS -------------- */


    protected void uponReadTagMessage(final ReadTagMessage message, final Host sender, final short sourceProto, final int channelId) {
        triggerNotification(new ReadTagMessageNotification(message, sender));
    }

    protected void uponReadTagAck(final ReadTagAck message, final Host sender, final short sourceProto, final int channelId) {
        triggerNotification(new ReadTagAckNotification(message, sender));
    }

    protected void uponReadMessage(final ReadMessage message, final Host sender, final short sourceProto, final int channelId) {
        triggerNotification(new ReadMessageNotification(message, sender));
    }

    protected void uponReadAck(final ReadAck message, final Host sender, final short sourceProto, final int channelId) {
        triggerNotification(new ReadAckNotification(message, sender));
    }

    protected void uponWriteMessage(final WriteMessage message, final Host sender, final short sourceProto, final int channelId) {
        triggerNotification(new WriteMessageNotification(message, sender));
    }

    protected void uponWriteAck(final WriteAck message, final Host sender, final short sourceProto, final int channelId) {
        triggerNotification(new WriteAckNotification(message, sender));
    }

    protected void uponWriteBackMessage(final WriteBackMessage message, final Host sender, final short sourceProto, final int channelId) {
        triggerNotification(new WriteBackMessageNotification(message, sender));
    }

    protected void uponWriteBackAck(final WriteBackAck message, final Host sender, final short sourceProto, final int channelId) {
        triggerNotification(new WriteBackAckNotification(message, sender));
    }


    /* -------------- REQUEST EVENT HANDLERS -------------- */


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


    /* -------------- CONNECTION/SYSTEM EVENT HANDLERS -------------- */


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

    private void uponStopMessage(final StopMessage message, final Host sender, final short protoId, final int channelId) {
        logger.info("Goodbye!");
        System.exit(0);
    }


    /* -------------- TIMER EVENT HANDLERS -------------- */


    private void uponConnectionRetryTimer(final ConnectionRetryTimer timer, long timerId) {
        final Host host = timer.getHost();
        logger.info("Reconnecting with {}", host);
        openConnection(host, channelId);
    }


    /* -------------- AUX METHODS -------------- */


    protected void connectAll() {
        for (final Host host : peers) {
            if (connections.contains(host)) continue;
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
