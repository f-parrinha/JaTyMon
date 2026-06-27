package jatymon.typestate.file;

import java.io.InputStream;
import java.nio.file.Path;

public record TypestateFile(InputStream stream, Path filePath) implements AutoCloseable {
    @Override
    public void close() throws Exception {
        stream.close();
    }
}
