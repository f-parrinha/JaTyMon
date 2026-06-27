package mixedsessions;

import jatymon.annotations.Typestate;

@Typestate("DroneClient")
public class DroneClient {
    public boolean connect() {
        return true;
    }
    public void disconnect() {
        // Nothing to do here...
    }
    public void turnoff() {
        // Nothing to do here...
    }
    public void manualInput() {
        // Nothing to do here...
    }
    public void target() {
        // Nothing to do here...
    }
    public void cameraRefresh() {
        // Nothing to do here...
    }
}