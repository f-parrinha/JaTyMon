package semantic;

import common.Tests;
import common.compilation.CompilationTester;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class SemanticExamplesTests extends CompilationTester {
    @Test
    public void testAlarmDeviceProtocol() throws IOException {
        success(Tests.Examples.ALARM_DEVICE_PROTOCOL);
    }

    @Test
    public void testAnimal() throws IOException {
        success(Tests.Examples.ANIMAL);
    }

    @Test
    public void testAutoDrivingCar() throws IOException {
        success(Tests.Examples.AUTO_DRIVING_CAR);
    }

    @Test
    public void testBaseAccount() throws IOException {
        success(Tests.Examples.BASE_ACCOUNT);
    }

    @Test
    public void testBaseIterator() throws IOException {
        success(Tests.Examples.BASE_ITERATOR);
    }

    @Test
    public void testBulb() throws IOException {
        success(Tests.Examples.BULB);
    }

    @Test
    public void testCar() throws IOException {
        success(Tests.Examples.CAR);
    }

    @Test
    public void testDog() throws IOException {
        success(Tests.Examples.DOG);
    }

    @Test
    public void testDroneGroupProtocol() throws IOException {
        success(Tests.Examples.DRONE_GROUP_PROTOCOL);
    }

    @Test
    public void testDroneNodeProtocol() throws IOException {
        success(Tests.Examples.DRONE_NODE_PROTOCOL);
    }

    @Test
    public void testDroneProtocol() throws IOException {
        success(Tests.Examples.DRONE_PROTOCOL);
    }

    @Test
    public void testFileClient() throws IOException {
        success(Tests.Examples.FILE_CLIENT);
    }

    @Test
    public void testFileClient2() throws IOException {
        success(Tests.Examples.FILE_CLIENT2);
    }

    @Test
    public void testFileServer() throws IOException {
        success(Tests.Examples.FILE_SERVER);
    }

    @Test
    public void testFileProtocol() throws IOException {
        success(Tests.Examples.FILE_PROTOCOL);
    }

    @Test
    public void testFunnyBulb() throws IOException {
        success(Tests.Examples.FUNNY_BULB);
    }

    @Test
    public void testJavaIterator() throws IOException {
        success(Tests.Examples.JAVA_ITERATOR);
    }

    @Test
    public void testLineReader() throws IOException {
        success(Tests.Examples.LINE_READER);
    }

    @Test
    public void testMultiTaskRobotProtocol() throws IOException {
        success(Tests.Examples.MULTI_TASK_ROBOT_PROTOCOL);
    }

    @Test
    public void testObserverProtocol() throws IOException {
        success(Tests.Examples.OBSERVER_PROTOCOL);
    }

    @Test
    public void testPendingDroneProtocol() throws IOException {
        success(Tests.Examples.PENDING_DRONE_PROTOCOL);
    }

    @Test
    public void testPredictiveAlarmDeviceProtocol() throws IOException {
        success(Tests.Examples.PREDICTIVE_ALARM_DEVICE_PROTOCOL);
    }

    @Test
    public void testProAccount() throws IOException {
        success(Tests.Examples.PRO_ACCOUNT);
    }

    @Test
    public void testRemovableIterator() throws IOException {
        success(Tests.Examples.REMOVABLE_ITERATOR);
    }

    @Test
    public void testRobotGroupProtocol() throws IOException {
        success(Tests.Examples.ROBOT_GROUP_PROTOCOL);
    }

    @Test
    public void testRobotNodeProtocol() throws IOException {
        success(Tests.Examples.ROBOT_NODE_PROTOCOL);
    }

    @Test
    public void testRobotProtocol() throws IOException {
        success(Tests.Examples.ROBOT);
    }

    @Test
    public void testSmartDeviceProtocol() throws IOException {
        success(Tests.Examples.SMART_DEVICE_PROTOCOL);
    }

    @Test
    public void testSocketProtocol() throws IOException {
        success(Tests.Examples.SOCKET_PROTOCOL);
    }

    @Test
    public void testSUV() throws IOException {
        success(Tests.Examples.SUV);
    }

    @Test
    public void testTimeoutSocketProtocol() throws IOException {
        success(Tests.Examples.TIMEOUT_SOCKET_PROTOCOL);
    }

    @Test
    public void testXRayDroneProtocol() throws IOException {
        success(Tests.Examples.XRAY_DRONE_PROTOCOL);
    }
}
