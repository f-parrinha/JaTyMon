package jatymon.babelprojects.multipaxos.protocols.smr;

import jatymon.annotations.Discardable;
import jatymon.annotations.Ext;
import jatymon.annotations.Typestate;
import jatymon.babelprojects.multipaxos.ConfigKeys;
import jatymon.babelprojects.multipaxos.messages.client.*;
import jatymon.babelprojects.multipaxos.messages.replica.*;
import jatymon.babelprojects.multipaxos.notifications.connection.ConnectionDownNotification;
import jatymon.babelprojects.multipaxos.notifications.connection.ConnectionUpNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.client.ClientReadMessageNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.client.ClientWriteMessageNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.replica.AcceptMessageNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.replica.AcceptOkMessageNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.replica.PrepareMessageNotification;
import jatymon.babelprojects.multipaxos.notifications.messages.replica.PrepareOkMessageNotification;
import jatymon.babelprojects.multipaxos.protocols.dispatcher.DispatcherProtocol;
import jatymon.babelprojects.multipaxos.requests.BroadcastMessageRequest;
import jatymon.babelprojects.multipaxos.requests.SendMessageRequest;
import jatymon.babelprojects.multipaxos.session.Operation;
import jatymon.babelprojects.multipaxos.session.Session;
import jatymon.babelprojects.multipaxos.timers.MultiPaxosRetryTimer;
import jatymon.babelprojects.multipaxos.util.NetworkUtils;
import jatymon.exceptions.discarding.DiscardIllegalAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.fct.di.novasys.babel.core.GenericProtocol;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.network.data.Host;

import java.io.IOException;
import java.net.InetAddress;
import java.util.*;

/**
 * Class {@code MultiPaxosProtocol} implements Multi-Paxos for incoming READ and WRITE operations from a client. Each WRITE
 * operation takes a slot. READ operations perform a lookup in the current slot (or slot index).
 */
@Typestate("MultiPaxos")
public class MultiPaxosProtocol extends GenericProtocol {
    public static final short PROTO_ID = 104;
    public static final String PROTO_NAME = "MultiPaxos";
    public static final String DUPLICATED_MESSAGE_WARN = "Discarding duplicated message '{}'";

    private static final Logger logger = LogManager.getLogger(MultiPaxosProtocol.class);

    private final Session session;

    // Stores promised and accepted ballots/values for different slots
    private final Map<Integer, Integer> promisedBallots;
    private final Map<Integer, Integer> acceptedBallots;
    private final Map<Integer, byte[]> acceptedValues;

    // This stores the current value of each client
    private final Map<String, byte[]> stateMachine;

    @Ext
    protected int peersSize;

    private int ballot;
    private int nextSlot;
    private Host leader;
    private Host myself;

    public MultiPaxosProtocol() {
        super(PROTO_NAME, PROTO_ID);
        this.session = new Session();
        this.promisedBallots = new HashMap<>();
        this.acceptedBallots = new HashMap<>();
        this.acceptedValues = new HashMap<>();
        this.stateMachine = new HashMap<>();
    }

    @Override
    public void init(final Properties props) throws HandlerRegistrationException, IOException {
        final Set<Host> peers = NetworkUtils.setupPeers(props.getProperty(ConfigKeys.PEERS_CONFIG));
        final InetAddress address = InetAddress.getByName(props.getProperty(ConfigKeys.ADDRESS_CONFIG));
        final String port = props.getProperty(ConfigKeys.PORT_CONFIG);

        // Ballot starts with the index of the proposer in the set of peers (evidently, the last one will be the leader)
        this.myself = new Host(address, Integer.parseInt(port));
        this.ballot = new ArrayList<>(peers).indexOf(myself);
        this.nextSlot = 0;
        this.peersSize = peers.size();

        // Connection handlers
        subscribeNotification(ConnectionUpNotification.ID, this::uponConnectionUp);
        subscribeNotification(ConnectionDownNotification.ID, this::uponConnectionDown);

        // Client message handlers
        subscribeNotification(ClientWriteMessageNotification.ID, this::uponClientWriteMessage);
        subscribeNotification(ClientReadMessageNotification.ID, this::uponClientReadMessage);

        // Multi-paxos message handlers
        subscribeNotification(PrepareMessageNotification.ID, this::uponPrepareMessage);
        subscribeNotification(PrepareOkMessageNotification.ID, this::uponPrepareOkMessage);
        subscribeNotification(AcceptMessageNotification.ID, this::uponAcceptMessage);
        subscribeNotification(AcceptOkMessageNotification.ID, this::uponAcceptOkMessage);

        registerTimerHandler(MultiPaxosRetryTimer.ID, this::uponPaxosRetryTimer);
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
        final ClientReadMessage clientMessage = notification.getMessage();
        if (!myself.equals(leader) && leader != null) {
            sendRequest(new SendMessageRequest(clientMessage, leader), DispatcherProtocol.PROTO_ID);
            return;
        }

        ballot += peersSize;

        final String opId = session.startRead(clientMessage.getClientId(), clientMessage.getHost(), nextSlot, ballot);
        handleClientRequest(opId, new byte[0], true);
    }

    protected void uponClientWriteMessage(final ClientWriteMessageNotification notification, final short protoId) {
        final ClientWriteMessage clientMessage = notification.getMessage();
        if (!myself.equals(leader) && leader != null) {
            sendRequest(new SendMessageRequest(clientMessage, leader), DispatcherProtocol.PROTO_ID);
            return;
        }

        nextSlot++;
        ballot += peersSize;

        final byte[] value = clientMessage.getValue();
        final String opId = session.startWrite(clientMessage.getClientId(), clientMessage.getHost(), nextSlot, ballot, value);
        handleClientRequest(opId, value, false);
    }

    private void handleClientRequest(final String opId, final byte[] value, final boolean noop) {
        final ReplicaMessage toSend = myself.equals(leader)
                ? new AcceptMessage(opId, nextSlot, ballot, value, noop)
                : new PrepareMessage(opId, nextSlot, ballot);
        sendRequest(new BroadcastMessageRequest(toSend), DispatcherProtocol.PROTO_ID);
        logger.debug("Broadcasting {}", toSend);

        // Set up a retry timer (the leader may stop working)
        final long timerId = setupTimer(new MultiPaxosRetryTimer(opId), MultiPaxosRetryTimer.TIMEOUT);
        session.getOp(opId).setRetryTimerId(timerId);
    }


    /* --------------- PAXOS OP METHODS --------------- */


    protected void uponPrepareMessage(final PrepareMessageNotification notification, final short protoId) {
        final PrepareMessage prepare = notification.getMessage();
        final String opId = prepare.getOpId();
        final int slot = prepare.getSlot();
        final int ballot = prepare.getBallot();

        if (ballot > promisedBallots.getOrDefault(prepare.getSlot(), -1)) {
            promisedBallots.put(slot, ballot);
            final PrepareOkMessage promise = new PrepareOkMessage(opId, ballot,
                    acceptedBallots.getOrDefault(slot, 0),
                    acceptedValues.getOrDefault(slot, new byte[0]));

            final Host host = notification.getSender();
            sendRequest(new SendMessageRequest(promise, host), DispatcherProtocol.PROTO_ID);
            logger.debug("Sending {} to {}", promise, host);
        }
    }

    @Discardable
    protected void uponPrepareOkMessage(final PrepareOkMessageNotification notification, final short protoId) {
        final PrepareOkMessage prepareOk = notification.getMessage();
        final String opId = prepareOk.getOpId();
        if (!session.hasOp(opId)) {
            throw new DiscardIllegalAction();
        } else if (!session.receiveMessage(opId, prepareOk)) {
            logger.warn(DUPLICATED_MESSAGE_WARN, prepareOk);
            return;
        } else if (!session.hasQuorum(opId, peersSize)) {
            return;
        }

        // Cancel old retry timer
        final Operation op = session.stop(opId);
        cancelTimer(op.getRetryTimerId());

        // Choose propose value (may update if there was a value already proposed)
        final PrepareOkMessage highest = getHighestPrepareOk(op.getReceived());
        leader = myself;
        logger.info("I ({}) am the leader!", myself);

        if (highest != null) {
            ballot = highest.getBallot();
            op.setValue(highest.getValue());
        }

        // Broadcast and start ACCEPT phase
        final String newOpId = session.restart(op);
        final AcceptMessage accept = new AcceptMessage(newOpId, op.getSlot(), op.getBallot(), op.getValue(), op.getType() == Operation.Type.READ);
        sendRequest(new BroadcastMessageRequest(accept), DispatcherProtocol.PROTO_ID);
        logger.debug("Broadcasting {}", accept);

        // Start new retry (for leader)
        long newTimerId = setupTimer(new MultiPaxosRetryTimer(newOpId), MultiPaxosRetryTimer.TIMEOUT);
        session.getOp(newOpId).setRetryTimerId(newTimerId);
    }

    protected boolean uponAcceptMessage(final AcceptMessageNotification notification, final short protoId) {
        final AcceptMessage accept = notification.getMessage();
        final int slot = accept.getSlot();
        final int ballot = accept.getBallot();
        final byte[] value = accept.getValue();

        if (ballot >= promisedBallots.getOrDefault(slot, -1)) {
            nextSlot = slot;
            leader = notification.getSender();
            if (!accept.isNoop()) {
                promisedBallots.put(slot, ballot);
                acceptedBallots.put(slot, ballot);
                acceptedValues.put(slot, value);
            }

            final Host host = notification.getSender();
            final AcceptOkMessage learn = new AcceptOkMessage(accept.getOpId(), ballot, value);
            sendRequest(new SendMessageRequest(learn,  host), DispatcherProtocol.PROTO_ID);
            logger.debug("Sending {} to {}", learn, host);
        }
        return leader.equals(myself);
    }

    @Discardable
    protected void uponAcceptOkMessage(final AcceptOkMessageNotification notification, final short protoId) {
        final AcceptOkMessage acceptOk = notification.getMessage();
        final String opId = acceptOk.getOpId();
        if (!session.hasOp(opId)) {
            throw new DiscardIllegalAction();
        } else if (!session.receiveMessage(opId, acceptOk)) {
            logger.warn(DUPLICATED_MESSAGE_WARN, acceptOk);
            return;
        } else if (!session.hasQuorum(opId, peersSize)){
            return;
        }

        final Operation op = session.stop(opId);
        if (op.getType() == Operation.Type.READ) {
            op.setValue(stateMachine.getOrDefault(op.getClientId(), new byte[0]));
        } else {
            acceptedValues.put(op.getSlot(), op.getValue());
            stateMachine.put(op.getClientId(), op.getValue());
        }

        // Send client reply
        final Host host = op.getClientHost();
        final ClientMessage clientReply = buildClientReply(op, op.getValue());
        sendRequest(new SendMessageRequest(clientReply, host), DispatcherProtocol.PROTO_ID);
        logger.debug("Sending {} to {}", clientReply, host);

        cancelTimer(op.getRetryTimerId());
    }

    protected void uponPaxosRetryTimer(final MultiPaxosRetryTimer timer, final long timerId) {
        final String opId = timer.getOpId();
        if (!session.hasOp(opId)) return; // already completed, ignore

        // Suspect leader, force prepare phase
        ballot += peersSize;
        leader = null;

        // Clear received messages and retry
        session.clearReceived(opId);
        final Operation op = session.getOp(opId);
        op.setBallot(ballot);

        // Re-issue as prepare since we have no leader
        sendRequest(new BroadcastMessageRequest(new PrepareMessage(opId, op.getSlot(), ballot)), DispatcherProtocol.PROTO_ID);

        // Reset timer for this retry
        long newTimerId = setupTimer(new MultiPaxosRetryTimer(opId), MultiPaxosRetryTimer.TIMEOUT);
        op.setRetryTimerId(newTimerId);
    }


    /* --------------- AUX METHODS --------------- */


    private ClientMessage buildClientReply(final Operation op, final byte[] value) {
        final String clientId = op.getClientId();
        final Host clientHost = op.getClientHost();
        return switch (op.getType()) {
            case Operation.Type.WRITE -> new ClientWriteAck(clientId, clientHost);
            case Operation.Type.READ -> new ClientReadAck(clientId, clientHost, value);
        };
    }

    private PrepareOkMessage getHighestPrepareOk(final Set<ReplicaMessage> messages) {
        PrepareOkMessage max = null;
        for (final var m : messages) {
            if (!(m instanceof PrepareOkMessage prepareOk)) continue;
            if (prepareOk.getHighestAccepted() > ballot) max = prepareOk;
        }
        return max;
    }
}