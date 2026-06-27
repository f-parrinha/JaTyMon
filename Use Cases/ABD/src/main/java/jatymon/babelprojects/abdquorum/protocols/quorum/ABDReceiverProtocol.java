package jatymon.babelprojects.abdquorum.protocols.quorum;

import jatymon.annotations.Ext;
import jatymon.annotations.Key;
import jatymon.annotations.Typestate;
import jatymon.babelprojects.abdquorum.ConfigKeys;
import jatymon.babelprojects.abdquorum.data.Database;
import jatymon.babelprojects.abdquorum.data.Tag;
import jatymon.babelprojects.abdquorum.utils.NetworkUtils;
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
import jatymon.babelprojects.abdquorum.notifications.messages.readop.ReadMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.readop.WriteBackMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.writeop.ReadTagMessageNotification;
import jatymon.babelprojects.abdquorum.notifications.messages.writeop.WriteMessageNotification;
import jatymon.babelprojects.abdquorum.protocols.dispatcher.DispatcherProtocol;
import jatymon.babelprojects.abdquorum.requests.SendMessageRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.fct.di.novasys.babel.core.GenericProtocol;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.network.data.Host;

import java.io.IOException;
import java.util.Properties;

@Typestate("ABDReceiver")
public class ABDReceiverProtocol extends GenericProtocol {
    public static final String PROTO_NAME = "ABDReceiver";
    public static final short PROTO_ID = 102;

    private static final Logger logger = LogManager.getLogger(ABDReceiverProtocol.class);

    private final Database db;

    @Ext
    protected int peersSize;

    public ABDReceiverProtocol() {
        super(PROTO_NAME, PROTO_ID);
        this.db = new Database();
    }

    @Override
    public void init(final Properties props) throws HandlerRegistrationException, IOException {
        this.peersSize = NetworkUtils.setupPeers(props.getProperty(ConfigKeys.PEERS_CONFIG)).size();

        subscribeNotification(ConnectionUpNotification.ID, this::uponConnectionUp);
        subscribeNotification(ConnectionDownNotification.ID, this::uponConnectionDown);

        subscribeNotification(ReadTagMessageNotification.ID, this::uponReadTagMessage);
        subscribeNotification(ReadMessageNotification.ID, this::uponReadMessage);
        subscribeNotification(WriteMessageNotification.ID, this::uponWriteMessage);
        subscribeNotification(WriteBackMessageNotification.ID, this::uponWriteBackMessage);
    }

    protected void uponConnectionUp(final ConnectionUpNotification notification, final short protoId) {
        logger.info("Connection up with {}", notification.getHost());
    }

    protected void uponConnectionDown(final ConnectionDownNotification notification, final short protoId) {
        final Host host = notification.getHost();
        logger.info("Connection down with {}", host);
    }


    /* --------------- READ OP METHODS --------------- */


    protected void uponReadMessage(final ReadMessageNotification notification, final short protoId) {
        final ReadMessage message = notification.getMessage();
        final Database.Entry dbEntry = db.getEntry(message.getClientId());

        final Host targetHost = notification.getSender();
        final ReadAck ack = new ReadAck(message.getOpId(), dbEntry);
        sendRequest(new SendMessageRequest(ack, targetHost), DispatcherProtocol.PROTO_ID);
        logger.debug("Sending {} to {}", ack, targetHost);
    }

    protected void uponWriteBackMessage(final WriteBackMessageNotification notification, final short protoId) {
        final WriteBackMessage message = notification.getMessage();
        final String clientId = message.getClientId();
        final Database.Entry entry = message.getEntry();
        final Database.Entry current = db.getEntry(clientId);
        if (entry.tag().isGreater(current.tag())) {
            db.put(clientId, entry);
        }

        final Host targetHost = notification.getSender();
        final WriteBackAck ack = new WriteBackAck(message.getOpId(), db.getEntry(clientId));
        sendRequest(new SendMessageRequest(ack, targetHost), DispatcherProtocol.PROTO_ID);
        logger.debug("Sending {} to {}", ack, targetHost);
    }


    /* --------------- WRITE OP METHODS --------------- */


    protected void uponReadTagMessage(final ReadTagMessageNotification notification, final short protoId) {
        final ReadTagMessage message = notification.getMessage();
        final String opId = message.getOpId();
        final Tag tag = db.getEntry(message.getClientId()).tag();

        final Host targetHost = notification.getSender();
        final ReadTagAck ack = new ReadTagAck(opId, tag);
        sendRequest(new SendMessageRequest(ack, targetHost), protoId);
        logger.debug("Sending {} to {}", ack, targetHost);
    }


    protected void uponWriteMessage(final WriteMessageNotification notification, final short protoId) {
        final WriteMessage message = notification.getMessage();
        final String clientId = message.getClientId();
        final Database.Entry toWrite = message.getEntry();
        final Database.Entry current = db.getEntry(clientId);
        if (toWrite.tag().isGreater(current.tag())) {
            db.put(clientId, toWrite);
        }

        final Host targetHost = notification.getSender();
        final WriteAck ack = new WriteAck(message.getOpId(), db.getEntry(clientId));
        sendRequest(new SendMessageRequest(ack, targetHost), DispatcherProtocol.PROTO_ID);
        logger.debug("Sending {} to {}", ack, targetHost);
    }
}
