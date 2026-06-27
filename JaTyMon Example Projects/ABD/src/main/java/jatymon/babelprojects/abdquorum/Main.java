package jatymon.babelprojects.abdquorum;

import jatymon.babelprojects.abdquorum.protocols.client.MonitoredClientProtocol;
import jatymon.babelprojects.abdquorum.utils.NetworkUtils;
import jatymon.babelprojects.abdquorum.protocols.client.ClientProtocol;
import jatymon.babelprojects.abdquorum.protocols.dispatcher.DispatcherProtocol;
import jatymon.babelprojects.abdquorum.protocols.dispatcher.FaultyDispatcherProtocol;
import jatymon.babelprojects.abdquorum.protocols.dispatcher.MonitoredDispatcherProtocol;
import jatymon.babelprojects.abdquorum.protocols.dispatcher.MonitoredFaultyDispatcherProtocol;
import jatymon.babelprojects.abdquorum.protocols.quorum.*;
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

    /**
     * Registers the correct protocol type according to the configs in the configuration file
     * @param babel Babel instance
     * @param props properties instance
     * @throws ProtocolAlreadyExistsException duplicate protocol exception
     * @throws HandlerRegistrationException registration handler exception
     * @throws IOException IO exception
     */
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

    /**
     * Sets up the execution with the unified (simple) version of the ABD protocol
     * @param babel Babel instance
     * @param props properties instance
     * @throws ProtocolAlreadyExistsException duplicate protocol exception
     * @throws HandlerRegistrationException registration handler exception
     * @throws IOException IO exception
     */
    private static void registerSimple(final Babel babel, final Properties props) throws ProtocolAlreadyExistsException, HandlerRegistrationException, IOException {
        final boolean faultTestOn = Boolean.parseBoolean(props.getProperty(ConfigKeys.FAULT_TEST_ON));
        final boolean useMonitors = Boolean.parseBoolean(props.getProperty(ConfigKeys.USE_MONITORS));

        final ClientProtocol clientProto = useMonitors ? new ClientProtocol() :  new ClientProtocol();
        final ABDProtocol abdProto = useMonitors ? new MonitoredABDProtocol() : new ABDProtocol();
        final DispatcherProtocol dispatcherProto = faultTestOn
                ? (useMonitors ? new MonitoredFaultyDispatcherProtocol() : new FaultyDispatcherProtocol())
                : (useMonitors ? new MonitoredDispatcherProtocol() : new DispatcherProtocol());

        babel.registerProtocol(dispatcherProto);
        babel.registerProtocol(abdProto);
        babel.registerProtocol(clientProto);

        dispatcherProto.init(props);
        abdProto.init(props);
        clientProto.init(props);
    }

    /**
     * Sets up a version of the ABD protocol separated in two roles: sender and receiver
     * @param babel Babel instance
     * @param props properties instance
     * @throws ProtocolAlreadyExistsException duplicate protocol exception
     * @throws HandlerRegistrationException registration handler exception
     * @throws IOException IO exception
     */
    private static void registerSenderReceiver(final Babel babel, final Properties props) throws ProtocolAlreadyExistsException, HandlerRegistrationException, IOException {
        final boolean faultTestOn = Boolean.parseBoolean(props.getProperty(ConfigKeys.FAULT_TEST_ON));
        final boolean useMonitors = Boolean.parseBoolean(props.getProperty(ConfigKeys.USE_MONITORS));

        final ClientProtocol clientProto = useMonitors ? new MonitoredClientProtocol() : new ClientProtocol();
        final ABDSenderProtocol abpSenderProto = useMonitors ? new MonitoredABDSenderProtocol() : new ABDSenderProtocol();
        final ABDReceiverProtocol abpReceiverProto = useMonitors ? new MonitoredABDReceiverProtocol(): new ABDReceiverProtocol();
        final DispatcherProtocol dispatcherProto = faultTestOn
                ? (useMonitors ? new MonitoredFaultyDispatcherProtocol() : new FaultyDispatcherProtocol())
                : (useMonitors ? new MonitoredDispatcherProtocol() : new DispatcherProtocol());

        babel.registerProtocol(dispatcherProto);
        babel.registerProtocol(abpSenderProto);
        babel.registerProtocol(abpReceiverProto);
        babel.registerProtocol(clientProto);

        dispatcherProto.init(props);
        abpSenderProto.init(props);
        abpReceiverProto.init(props);
        clientProto.init(props);
    }
}