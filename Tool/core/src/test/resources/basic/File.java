package basic;

import jatymon.annotations.Typestate;

@Typestate("File")
public class File {
    public FileStatus open() {
        return FileStatus.OK;
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