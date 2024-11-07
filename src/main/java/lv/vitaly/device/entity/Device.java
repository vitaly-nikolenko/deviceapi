package lv.vitaly.device.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Device {
    private String macAddress;
    private DeviceType deviceType;
    private String uplinkMacAddress;
}




