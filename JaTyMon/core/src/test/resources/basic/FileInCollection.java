package basic;

import jatymon.annotations.Typestate;

@Typestate("FileInCollection.protocol")
public class FileInCollection {
    public FileStatus open() {
        return FileStatus.OK;
    }
    public FileState init() {
        return FileState.INIT;
    }
    public FileState state() {
        return FileState.INIT;
    }
    public boolean hasNext() {
        return true;
    }

    public int read() {
        return 0;
    }

    public void close() {
        // Nothing to do here...
    }
}