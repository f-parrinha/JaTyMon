package jatymon.babelprojects.abp;

import jatymon.babelprojects.abp.protocols.*;
import jatymon.babelprojects.abp.utils.NetworkUtils;
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
            logger.info("Defaulting to dual protocol types (Sender and Receiver)");
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
        final ABPProtocol abpProto = useMonitors ? new MonitoredABPProtocol() : new ABPProtocol();
        final DispatcherProtocol dispatcherProto = faultTestOn
                ? (useMonitors ? new MonitoredFaultyDispatcherProtocol() : new FaultyDispatcherProtocol())
                : (useMonitors ? new MonitoredDispatcherProtocol() : new DispatcherProtocol());

        babel.registerProtocol(dispatcherProto);
        babel.registerProtocol(abpProto);

        dispatcherProto.init(props);
        abpProto.init(props);
    }

    private static void registerSenderReceiver(final Babel babel, final Properties props) throws ProtocolAlreadyExistsException, HandlerRegistrationException, IOException {
        final boolean faultTestOn = Boolean.parseBoolean(props.getProperty(ConfigKeys.FAULT_TEST_ON));
        final boolean useMonitors = Boolean.parseBoolean(props.getProperty(ConfigKeys.USE_MONITORS));
        final ABPSenderProtocol abpSenderProto = useMonitors ? new MonitoredABPSenderProtocol() : new ABPSenderProtocol();
        final ABPReceiverProtocol abpReceiverProto = useMonitors ? new MonitoredABPReceiverProtocol(): new ABPReceiverProtocol();
        final DispatcherProtocol dispatcherProto = faultTestOn
                ? (useMonitors ? new MonitoredFaultyDispatcherProtocol() : new FaultyDispatcherProtocol())
                : (useMonitors ? new MonitoredDispatcherProtocol() : new DispatcherProtocol());

        babel.registerProtocol(dispatcherProto);
        babel.registerProtocol(abpSenderProto);
        babel.registerProtocol(abpReceiverProto);

        dispatcherProto.init(props);
        abpSenderProto.init(props);
        abpReceiverProto.init(props);
    }
}