package lv.vitaly.device;

import lv.vitaly.device.dto.DeviceTreeDTO;
import lv.vitaly.device.entity.Device;
import lv.vitaly.device.entity.DeviceType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DeviceServiceTest {

    private DeviceService deviceService;

    @BeforeEach
    void setUp() {
        deviceService = new DeviceService();
    }

    @Test
    void testAddDevice() {
        Device device = new Device("00:1A:2B:3C:4D:5E", DeviceType.GATEWAY, null);
        deviceService.addDevice(device);

        Device fetchedDevice = deviceService.getDeviceByMacAddress("00:1A:2B:3C:4D:5E");
        assertNotNull(fetchedDevice);
        assertEquals("00:1A:2B:3C:4D:5E", fetchedDevice.getMacAddress());
    }

    @Test
    void testAddDevice_withDuplicateMacAddress() {
        Device device = new Device("00:1A:2B:3C:4D:5E", DeviceType.GATEWAY, null);
        Device duplicateMac = new Device("00:1A:2B:3C:4D:5E", DeviceType.SWITCH, null);

        deviceService.addDevice(device);

        // Try to add device with the same MAC address
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            deviceService.addDevice(duplicateMac);
        });

        assertEquals("Device with this MAC address already exists.", exception.getMessage());
    }

    @Test
    void testUpdateDevice() {
        Device device = new Device("00:1A:2B:3C:4D:5E", DeviceType.GATEWAY, null);
        deviceService.addDevice(device);

        Device updatedDevice = new Device("00:1A:2B:3C:4D:5E", DeviceType.SWITCH, "00:1A:2B:3C:4D:6F");
        boolean isUpdated = deviceService.updateDevice("00:1A:2B:3C:4D:5E", updatedDevice);

        assertTrue(isUpdated);

        Device fetchedDevice = deviceService.getDeviceByMacAddress("00:1A:2B:3C:4D:5E");
        assertNotNull(fetchedDevice);
        assertEquals(DeviceType.SWITCH, fetchedDevice.getDeviceType());
        assertEquals("00:1A:2B:3C:4D:6F", fetchedDevice.getUplinkMacAddress());
    }

    @Test
    void testDeleteDevice() {
        Device device = new Device("00:1A:2B:3C:4D:5E", DeviceType.GATEWAY, null);
        deviceService.addDevice(device);

        boolean isDeleted = deviceService.deleteDevice("00:1A:2B:3C:4D:5E");

        assertTrue(isDeleted);
        Device fetchedDevice = deviceService.getDeviceByMacAddress("00:1A:2B:3C:4D:5E");
        assertNull(fetchedDevice);
    }

    @Test
    void testGetDeviceTree() {
        Device gateway = new Device("00:1A:2B:3C:4D:5E", DeviceType.GATEWAY, null);
        Device switchDevice = new Device("00:1A:2B:3C:4D:6F", DeviceType.SWITCH, "00:1A:2B:3C:4D:5E");
        Device ap = new Device("00:1A:2B:3C:4D:7F", DeviceType.ACCESS_POINT, "00:1A:2B:3C:4D:6F");

        deviceService.addDevice(gateway);
        deviceService.addDevice(switchDevice);
        deviceService.addDevice(ap);

        DeviceTreeDTO deviceTree = deviceService.getDeviceTree("00:1A:2B:3C:4D:5E");
        assertNotNull(deviceTree);
        assertEquals("00:1A:2B:3C:4D:5E", deviceTree.getDevice().getMacAddress());
        assertFalse(deviceTree.getLinkedDevices().isEmpty());
    }

    @Test
    void testGetAllDeviceTrees() {
        Device gateway = new Device("00:1A:2B:3C:4D:5E", DeviceType.GATEWAY, null);
        Device switchDevice = new Device("00:1A:2B:3C:4D:6F", DeviceType.SWITCH, "00:1A:2B:3C:4D:5E");

        deviceService.addDevice(gateway);
        deviceService.addDevice(switchDevice);

        List<DeviceTreeDTO> deviceTrees = deviceService.getAllDeviceTrees();
        assertNotNull(deviceTrees);
        assertFalse(deviceTrees.isEmpty());
    }

    @Test
    void testGetDeviceByMacAddress() {
        Device device = new Device("00:1A:2B:3C:4D:5E", DeviceType.GATEWAY, null);
        deviceService.addDevice(device);

        Device fetchedDevice = deviceService.getDeviceByMacAddress("00:1A:2B:3C:4D:5E");
        assertNotNull(fetchedDevice);
        assertEquals("00:1A:2B:3C:4D:5E", fetchedDevice.getMacAddress());
    }
}
