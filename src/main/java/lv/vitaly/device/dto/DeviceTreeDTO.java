package lv.vitaly.device.dto;

import lombok.Data;
import lv.vitaly.device.entity.Device;

import java.util.ArrayList;
import java.util.List;

@Data
public class DeviceTreeDTO {
    private Device device;
    private List<DeviceTreeDTO> linkedDevices = new ArrayList<>();

    public DeviceTreeDTO(Device device) {
        this.device = device;
    }

    public void addLinkedDevice(DeviceTreeDTO device) {
        this.linkedDevices.add(device);
    }
}

