package jatymon.babelprojects.abdquorum.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pt.unl.fct.di.novasys.network.data.Host;

import java.net.*;
import java.security.InvalidParameterException;
import java.util.*;

public class NetworkUtils {
    public static final String EMPTY_HOST_STRING = "EMPTY";

    private static final Logger logger = LogManager.getLogger(NetworkUtils.class);

    public static Set<Host> getHosts(String hostString) {
        final Set<Host> hosts = new HashSet<>();
        if(hostString == null || hostString.isBlank() || hostString.equals(EMPTY_HOST_STRING)) {
            return hosts;
        }

        final String[] hostsInStr = hostString.split(",");
        try {
            for(var host : hostsInStr) {
                final String[] address_port = host.split(":");
                final InetAddress address = InetAddress.getByName(address_port[0]);
                final int port = Integer.parseInt(address_port[1]);
                hosts.add(new Host(address, port));
            }
            return hosts;
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Returns the ipv4 address of the given interface
     * @param inter name of the interface
     * @return ipv4 address of the interface
     */
    public static String getAddress(String inter) {
        try {
            final NetworkInterface byName = NetworkInterface.getByName(inter);
            if (byName == null) {
                logger.error("No interface named " + inter);
                return null;
            }

            final Enumeration<InetAddress> addresses = byName.getInetAddresses();
            InetAddress currentAddress;
            while (addresses.hasMoreElements()) {
                currentAddress = addresses.nextElement();
                if (currentAddress instanceof Inet4Address)
                    return currentAddress.getHostAddress();
            }

            logger.error("No ipv4 found for interface " + inter);
            return null;
        } catch (SocketException e) {
            logger.error(e.fillInStackTrace());
            throw new RuntimeException(e);
        }
    }

    public static void addInterfaceIp(Properties props) throws InvalidParameterException {
        final String interfaceName;
        if ((interfaceName = props.getProperty("interface")) != null && !interfaceName.trim().isEmpty()) {
            String ip = getAddress(interfaceName);
            if (ip != null)
                props.setProperty("address", ip);
            else {
                throw new InvalidParameterException("Property interface is set to " + interfaceName + ", but has no ip");
            }
        }
    }
}
