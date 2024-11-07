package lv.vitaly.device;

import lv.vitaly.device.dto.DeviceTreeDTO;
import lv.vitaly.device.entity.Device;
import lv.vitaly.device.entity.DeviceType;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class DeviceService {

    // In-memory store for devices
    private final Map<String, Device> deviceStore = new ConcurrentHashMap<>();

    private static final List<DeviceType> DEVICE_TYPE_ORDER = List.of(DeviceType.GATEWAY, DeviceType.SWITCH, DeviceType.ACCESS_POINT);

    /**
     * Retrieve all devices sorted by the custom order: Gateway > Switch > Access Point.
     */
    public List<Device> getAllDevicesSorted() {
        return new ArrayList<>(deviceStore.values()).stream()
                .sorted(Comparator.comparingInt(device -> DEVICE_TYPE_ORDER.indexOf(device.getDeviceType())))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves all device trees, one for each root device (device with no uplinkMacAddress).
     * @return List of DeviceTreeDTO representing all root device trees.
     */
    public List<DeviceTreeDTO> getAllDeviceTrees() {
        return deviceStore.values().stream()
                .filter(device -> device.getUplinkMacAddress() == null)
                .map(this::buildDeviceTree)
                .collect(Collectors.toList());
    }

    /**
     * Retrieve a device by its MAC address.
     * @param macAddress MAC address of the device to retrieve
     * @return Device if found, otherwise null
     */
    public Device getDeviceByMacAddress(String macAddress) {
        return deviceStore.get(macAddress);
    }

    /**
     * Add a new device to the store.
     * @param device Device to add
     */
    public Device addDevice(Device device) {
        if (deviceStore.containsKey(device.getMacAddress())) {
            throw new IllegalArgumentException("Device with this MAC address already exists.");
        }
        deviceStore.put(device.getMacAddress(), device);
        return device;
    }

    /**
     * Update an existing device in the store.
     * @param macAddress MAC address of the device to update
     * @param updatedDevice Updated device information
     * @return true if the device was updated, false if not found
     */
    public boolean updateDevice(String macAddress, Device updatedDevice) {
        if (deviceStore.containsKey(macAddress)) {
            deviceStore.put(macAddress, updatedDevice);
            return true;
        }
        return false;
    }

    /**
     * Delete a device from the store.
     * @param macAddress MAC address of the device to delete
     * @return true if the device was deleted, false if not found
     */
    public boolean deleteDevice(String macAddress) {
        return deviceStore.remove(macAddress) != null;
    }

    /**
     * Get the hierarchical device tree starting from a specific MAC address.
     * @param macAddress MAC address to start the tree from
     * @return DeviceTreeDTO representing the device tree, or null if not found
     */
    public DeviceTreeDTO getDeviceTree(String macAddress) {
        Device rootDevice = deviceStore.get(macAddress);
        if (rootDevice == null) {
            return null;
        }
        return buildDeviceTree(rootDevice);
    }

    /**
     * Build the hierarchical device tree recursively.
     * @param device Root device of the tree
     * @return DeviceTreeDTO representing the tree structure
     */
    private DeviceTreeDTO buildDeviceTree(Device device) {
        DeviceTreeDTO deviceTreeDTO = new DeviceTreeDTO(device);

        // Find and add all devices linked to this device (children)
        deviceStore.values().stream()
                .filter(d -> d.getUplinkMacAddress() != null && d.getUplinkMacAddress().equals(device.getMacAddress()))
                .forEach(linkedDevice -> deviceTreeDTO.addLinkedDevice(buildDeviceTree(linkedDevice)));

        return deviceTreeDTO;
    }
}

