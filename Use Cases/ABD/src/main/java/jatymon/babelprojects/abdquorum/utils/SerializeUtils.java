package jatymon.babelprojects.abdquorum.utils;

import io.netty.buffer.ByteBuf;
import jatymon.babelprojects.abdquorum.data.Tag;
import pt.unl.fct.di.novasys.network.data.Host;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

public class SerializeUtils {

    /**
     * Writes data in the buffer in order to serialize a string
     * @param byteBuf Netty ByteBuf
     * @param bytes Bytes to serialize
     */
    public static void serializeBytes(ByteBuf byteBuf, byte[] bytes) {
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }

    /**
     * Writes data in the buffer in order to serialize a string
     * @param byteBuf Netty ByteBuf
     * @param string String to serialize
     */
    public static void serializeString(ByteBuf byteBuf, String string) {
        byte[] stringBytes = string.getBytes(StandardCharsets.UTF_8);
        byteBuf.writeInt(stringBytes.length);
        byteBuf.writeBytes(stringBytes);
    }

    /**
     * Writes data in the buffer in order to serialize a Host
     * @param byteBuf Netty ByteBuf
     * @param host Host to serialize
     */
    public static void serializeHost(ByteBuf byteBuf, Host host) {
        SerializeUtils.serializeString(byteBuf, host.getAddress().getHostAddress());
        byteBuf.writeInt(host.getPort());
    }


    /**
     * Writes data in the buffer in order to serialize a Host
     * @param byteBuf Netty ByteBuf
     * @param tag Tag to serialize
     */
    public static void serializeTag(ByteBuf byteBuf, Tag tag) {
        byteBuf.writeInt(tag.getSeqNumb());
        byteBuf.writeInt(tag.getVersion());
    }


    /**
     * Reads data in the buffer in order to deserialize a string
     * <p> PRE: Must be called in the correct order of deserialization in the buffer</p>
     * @param byteBuf Netty ByteBuf
     * @return byte[]
     */
    public static byte[] deserializeBytes(ByteBuf byteBuf) {
        final int bytesLen = byteBuf.readInt();
        final byte[] bytes = new byte[bytesLen];
        byteBuf.readBytes(bytes);
        return bytes;
    }

    /**
     * Reads data in the buffer in order to deserialize a string
     * <p> PRE: Must be called in the correct order of deserialization in the buffer</p>
     * @param byteBuf Netty ByteBuf
     * @return String
     */
    public static String deserializeString(ByteBuf byteBuf) {
        int stringBytesLength = byteBuf.readInt();
        byte[] stringBytes = new byte[stringBytesLength];
        byteBuf.readBytes(stringBytes);
        return new String(stringBytes, StandardCharsets.UTF_8);
    }

    /**
     * Reads data in the buffer in order to deserialize a Host
     * <p> PRE: Must be called in the correct order of deserialization in the buffer</p>
     * @param byteBuf Netty ByteBuf
     * @return Host
     */
    public static Host deserializeHost(ByteBuf byteBuf) throws UnknownHostException {
        final InetAddress address = InetAddress.getByName(SerializeUtils.deserializeString(byteBuf));
        final int port = byteBuf.readInt();
        return new Host(address, port);
    }

    /**
     * Reads data in the buffer in order to deserialize a Tag
     * <p> PRE: Must be called in the correct order of deserialization in the buffer</p>
     * @param byteBuf Netty ByteBuf
     * @return Host
     */
    public static Tag deserializeTag(ByteBuf byteBuf) throws UnknownHostException {
        final int seqNumb = byteBuf.readInt();
        final int version = byteBuf.readInt();
        return new Tag(seqNumb, version);
    }
}
