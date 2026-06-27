package jatymon.babelprojects.multipaxos;

import jatymon.babelprojects.multipaxos.protocols.client.ClientProtocol;
import jatymon.babelprojects.multipaxos.protocols.client.MonitoredClientProtocol;
import jatymon.babelprojects.multipaxos.protocols.dispatcher.DispatcherProtocol;
import jatymon.babelprojects.multipaxos.protocols.dispatcher.FaultyDispatcherProtocol;
import jatymon.babelprojects.multipaxos.protocols.dispatcher.MonitoredDispatcherProtocol;
import jatymon.babelprojects.multipaxos.protocols.dispatcher.MonitoredFaultyDispatcherProtocol;
import jatymon.babelprojects.multipaxos.protocols.smr.MonitoredMultiPaxosProtocol;
import jatymon.babelprojects.multipaxos.protocols.smr.MultiPaxosProtocol;
import jatymon.babelprojects.multipaxos.util.NetworkUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.fct.di.novasys.babel.core.Babel;
import pt.unl.fct.di.novasys.babel.exceptions.HandlerRegistrationException;
import pt.unl.fct.di.novasys.babel.exceptions.InvalidParameterException;
import pt.unl.fct.di.novasys.babel.exceptions.ProtocolAlreadyExistsException;

import java.io.IOException;
import java.util.Properties;

public class Main {
    private static final String DEFAULT_CONFIG = "config.properties";
    private static final Logger logger = LogManager.getLogger(Main.class);

    public static void main(String[] args) throws ProtocolAlreadyExistsException, InvalidParameterException, IOException, HandlerRegistrationException {
        final Properties props = Babel.loadConfig(args, DEFAULT_CONFIG);
        final Babel babel = Babel.getInstance();
        NetworkUtils.addInterfaceIp(props);

        registerProtocol(babel, props);
        babel.start();
    }

    private static void registerProtocol(final Babel babel, final Properties props) throws ProtocolAlreadyExistsException, HandlerRegistrationException, IOException {
        final String protoType = props.getProperty(ConfigKeys.PROTOCOL_TYPE_CONFIG, "").strip();
        if (protoType.isBlank()) {
            logger.info("Defaulting 'simple'");
            registerSimple(babel, props);
            return;
        }

        // Parse protocol_type
        switch (protoType.toLowerCase()) {
            case ConfigKeys.SIMPLE_PROTO_CONFIG:
                registerSimple(babel, props);
                break;
            case ConfigKeys.SENDER_RECEIVER_PROTO_CONFIG:
                registerSenderReceiver(babel, props);
                break;
            default:
                logger.warn("Wrong '{}' key: <{}> or <{}>.",
                        ConfigKeys.PROTOCOL_TYPE_CONFIG,
                        ConfigKeys.SIMPLE_PROTO_CONFIG,
                        ConfigKeys.SENDER_RECEIVER_PROTO_CONFIG);
                registerSenderReceiver(babel, props);
        }
    }

    private static void registerSimple(final Babel babel, final Properties props) throws ProtocolAlreadyExistsException, HandlerRegistrationException, IOException {
        final boolean faultTestOn = Boolean.parseBoolean(props.getProperty(ConfigKeys.FAULT_TEST_ON));
        final boolean useMonitors = Boolean.parseBoolean(props.getProperty(ConfigKeys.USE_MONITORS));

        final ClientProtocol clientProto = useMonitors ? new MonitoredClientProtocol() :  new ClientProtocol();
        final MultiPaxosProtocol paxosProto = useMonitors ? new MonitoredMultiPaxosProtocol() : new MultiPaxosProtocol();
        final DispatcherProtocol dispatcherProto = faultTestOn
                ? (useMonitors ? new MonitoredFaultyDispatcherProtocol() : new FaultyDispatcherProtocol())
                : (useMonitors ? new MonitoredDispatcherProtocol() : new DispatcherProtocol());

        babel.registerProtocol(dispatcherProto);
        babel.registerProtocol(paxosProto);
        babel.registerProtocol(clientProto);

        dispatcherProto.init(props);
        paxosProto.init(props);
        clientProto.init(props);
    }

    private static void registerSenderReceiver(final Babel babel, final Properties props) throws ProtocolAlreadyExistsException, HandlerRegistrationException, IOException {
        // Redirect because this project does not have SENDER_RECEIVER mode, and we are using the same structure for the main class as in other examples.
        registerSimple(babel, props);
    }
}