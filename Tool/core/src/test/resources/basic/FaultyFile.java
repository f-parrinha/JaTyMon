package basic;

import jatymon.annotations.Typestate;

@Typestate("FaultyFile")
public class FaultyFile {
    public FileStatus open() {
        return FileStatus.OK;
    }

    public boolean hasNext() {
        return true;
    }

    public boolean hasNext2() {
        return true;
    }

    public int read() {
        return 0;
    }

    public void close() {
        // Nothing to do here...
    }
}