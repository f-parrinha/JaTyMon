package basic;

import jatymon.annotations.Typestate;


@Typestate("FaultyFile2")
public class FaultyFile2 {
    public FileStatus open() {
        return FileStatus.OK;
    }

    public boolean hasNext() {
        return true;
    }

    public boolean hasNext2() {
        return true;
    }

    public void close() {
        // Nothing to do here...
    }
}