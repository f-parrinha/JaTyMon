package jatymon.babelprojects.abdquorum.protocols.quorum;

import jatymon.annotations.Discardable;
import jatymon.annotations.Ext;
import jatymon.annotations.Key;
import jatymon.annotations.Typestate;
import jatymon.babelprojects.abdquorum.ConfigKeys;
import jatymon.babelprojects.abdquorum.data.Database;
import jatymon.babelprojects.abdquorum.data.Tag;
import jatymon.babelprojects.abdquorum.messages.tags.EntryMessage;
import jatymon.babelprojects.abdquorum.operations.Operation;
import jatymon.babelprojects.abdquorum.operations.ReadOperation;
import jatymon.babelprojects.abdquorum.operations.Session;
import jatymon.babelprojects.abdquorum.operations.WriteOperation;
import jatymon.babelprojects.abdquorum.utils.NetworkUtils;
import jatymon.babelprojects.abdquorum.exceptions.IllegalMessageException;
import jatymon.babelprojects.abdquorum.messages.replica.ReplicaMessage;
import jatymon.babelprojects.abdquorum.messages.tags.TagMessage;
import jatymon.babelprojects.abdquorum.messages.client.ClientReadAck;
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
import jatymon.babelprojects.abdquorum.notifications.connection.ConnectionDownNotification;
import jatymon.babelprojects.abdquorum.notifications.connection.ConnectionUpNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.client.ClientReadMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.client.ClientWriteMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.readop.ReadAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.readop.WriteBackAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.writeop.ReadTagAckNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.writeop.WriteAckNotification;
import jatymon.babelprojects.abdquorum.protocols.dispatcher.DispatcherProtocol;
import jatymon.babelprojects.abdquorum.requests.BroadcastMessageRequest;
import jatymon.babelprojects.abdquorum.requests.SendMessageRequest;
import jatymon.exceptions.discarding.DiscardIllegalAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.fct.di.novasys.babel.core.GenericProtocol;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.network.data.Host;

import java.io.IOException;
import java.util.Properties;
import java.util.Set;

@Typestate("ABDSender")
public class ABDSenderProtocol extends GenericProtocol {
    public static final String PROTO_NAME = "ABPSender";
    public static final short PROTO_ID = 103;
    public static final String DUPLICATED_MESSAGE_WARN = "Discarding duplicated message '{}'";

    private static final Logger logger = LogManager.getLogger(ABDSenderProtocol.class);

    private final Session session;

    private int version;

    @Ext
    protected int peersSize;

    public ABDSenderProtocol() {
        super(PROTO_NAME, PROTO_ID);
        this.session = new Session();
    }

    @Override
    public void init(final Properties props) throws HandlerRegistrationException, IOException {
        this.version = Integer.parseInt(props.getProperty(ConfigKeys.PORT_CONFIG));
        this.peersSize = NetworkUtils.setupPeers(props.getProperty(ConfigKeys.PEERS_CONFIG)).size();


        subscribeNotification(ConnectionUpNotification.ID, this::uponConnectionUp);
        subscribeNotification(ConnectionDownNotification.ID, this::uponConnectionDown);

        subscribeNotification(ClientWriteMessageNotification.ID, this::uponClientWriteMessage);
        subscribeNotification(ClientReadMessageNotification.ID, this::uponClientReadMessage);

        subscribeNotification(ReadTagAckNotification.ID, this::uponReadTagAck);
        subscribeNotification(ReadAckNotification.ID, this::uponReadAck);
        subscribeNotification(WriteAckNotification.ID, this::uponWriteAck);
        subscribeNotification(WriteBackAckNotification.ID, this::uponWriteBackAck);
    }


    /* --------------- CONNECTION METHODS --------------- */


    protected void uponConnectionUp(final ConnectionUpNotification notification, final short protoId) {
        logger.info("Connection up with {}", notification.getHost());
    }

    protected void uponConnectionDown(final ConnectionDownNotification notification, final short protoId) {
        final Host host = notification.getHost();
        logger.info("Connection down with {}", host);
    }


    /* --------------- CLIENT OP METHODS --------------- */


    protected void uponClientReadMessage(final ClientReadMessageNotification notification, final short protoId) {
        final String clientId = notification.getMessage().getClientId();
        final String opId = session.startRead(clientId, notification.getSender());

        final ReadMessage read = new ReadMessage(opId, clientId);
        sendRequest(new BroadcastMessageRequest(read), DispatcherProtocol.PROTO_ID);
        logger.debug("Broadcasting {}", read);
    }

    protected void uponClientWriteMessage(final ClientWriteMessageNotification notification, final short protoId) {
        final ClientWriteMessage message = notification.getMessage();
        final String clientId = message.getClientId();
        final byte[] value = message.getValue();
        final String opId = session.startWrite(clientId, notification.getSender(), value);

        final ReadTagMessage readTag = new ReadTagMessage(opId, clientId);
        sendRequest(new BroadcastMessageRequest(readTag), DispatcherProtocol.PROTO_ID);
        logger.debug("Broadcasting {}", readTag);
    }


    /* --------------- READ OP METHODS --------------- */


    @Discardable
    protected void uponReadAck(final ReadAckNotification notification, final short protoId) {
        final ReadAck message = notification.getMessage();
        final String opId = message.getOpId();
        if (!session.hasOp(opId)) {
            throw new DiscardIllegalAction();
        } else if (!session.receiveMessage(opId, message)) {
            logger.warn(DUPLICATED_MESSAGE_WARN, message);
            return;
        } else if (!session.hasQuorum(opId, peersSize)) {
            return;
        }

        // Get max tag
        final ReadOperation op = session.stopRead(opId);
        Database.Entry maxEntry = getMaxEntryMessage(op.getReceived(), message).getEntry();

        // Start write-back
        final String clientId = op.getClientId();
        final WriteBackMessage writeBack = new WriteBackMessage(session.startRead(clientId, op.getClientHost()), clientId, maxEntry);
        sendRequest(new BroadcastMessageRequest(writeBack), DispatcherProtocol.PROTO_ID);
        logger.debug("Broadcasting {}", writeBack);
    }


    @Discardable
    protected void uponWriteBackAck(final WriteBackAckNotification notification, final short protoId) {
        final WriteBackAck message = notification.getMessage();
        final String opId = message.getOpId();
        if (!session.hasOp(opId)) {
            throw new DiscardIllegalAction();
        } else if (!session.receiveMessage(opId, message)) {
            logger.warn(DUPLICATED_MESSAGE_WARN, message);
            return;
        } else if (!session.hasQuorum(opId, peersSize)) {
            return;
        }

        ReadOperation op = session.stopRead(opId);
        Database.Entry maxEntry = getMaxEntryMessage(op.getReceived(), message).getEntry();

        final Host client = op.getClientHost();
        final ClientReadAck ack = new ClientReadAck(op.getClientId(), maxEntry.value());
        sendRequest(new SendMessageRequest(ack, client), DispatcherProtocol.PROTO_ID);
        logger.debug("Sending {} to {}", ack, client);
    }


    /* --------------- WRITE OP METHODS --------------- */


    @Discardable
    protected void uponReadTagAck(final ReadTagAckNotification notification, final short protoId) {
        final ReadTagAck message = notification.getMessage();
        final String opId = message.getOpId();
        if (!session.hasOp(opId)) {
            throw new DiscardIllegalAction();
        } else if (!session.receiveMessage(opId, message)) {
            logger.warn(DUPLICATED_MESSAGE_WARN, message);
            return;
        } else if (!session.hasQuorum(opId, peersSize)) {
            return;
        }

        // Prepare data for new quorum round
        final WriteOperation op = session.stopWrite(opId);
        final int maxSeq = getMaxTagMessage(op.getReceived(), message).getTag().getSeqNumb();
        final byte[] value = op.getValue();
        final String clientId = op.getClientId();
        final Database.Entry entry = new Database.Entry(new Tag(maxSeq + 1, version), value);

        // Send write
        final WriteMessage write = new WriteMessage(session.startWrite(clientId, op.getClientHost(), value), clientId, entry);
        sendRequest(new BroadcastMessageRequest(write), DispatcherProtocol.PROTO_ID);
        logger.debug("Broadcasting {}", write);
    }

    @Discardable
    protected void uponWriteAck(final WriteAckNotification notification, final short protoId) {
        final WriteAck message = notification.getMessage();
        final String opId = message.getOpId();
        if (!session.hasOp(opId)) {
            throw new DiscardIllegalAction();
        } if (!session.receiveMessage(opId, message)) {
            logger.warn(DUPLICATED_MESSAGE_WARN, message);
            return;
        } else if (!session.hasQuorum(opId, peersSize)) {
            return;
        }

        final Operation op = session.stop(opId);
        final Host client = op.getClientHost();
        final ClientWriteAck ack = new ClientWriteAck(op.getClientId());
        sendRequest(new SendMessageRequest(ack, client), DispatcherProtocol.PROTO_ID);
        logger.debug("Sending {} to {}", ack, client);
    }


    /* --------------- AUX METHODS --------------- */


    private EntryMessage getMaxEntryMessage(final Set<ReplicaMessage> received, final EntryMessage startMax) {
        EntryMessage max = startMax;
        for(final ReplicaMessage m : received) {
            if (!(m instanceof final EntryMessage ack)) throw new IllegalMessageException(m);
            final Database.Entry otherEntry = ack.getEntry();
            if (otherEntry.tag().isGreater(max.getEntry().tag())) {
                max = ack;
            }
        }
        return max;
    }

    private TagMessage getMaxTagMessage(final Set<ReplicaMessage> received, final TagMessage startMax) {
        TagMessage max = startMax;
        for (final var m : received) {
            if (!(m instanceof TagMessage tagMessage)) throw new IllegalMessageException(m);
            final int otherSeq = tagMessage.getTag().getSeqNumb();
            if (otherSeq > max.getTag().getSeqNumb()) {
                max = tagMessage;
            }
        }
        return max;
    }
}
