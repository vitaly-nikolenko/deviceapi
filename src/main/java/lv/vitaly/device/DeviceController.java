package lv.vitaly.device;

import lv.vitaly.device.dto.DeviceTreeDTO;
import lv.vitaly.device.entity.Device;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
public class DeviceController {

    @Autowired
    private DeviceService deviceService;


    @GetMapping
    public List<Device> getAllDevicesSorted() {
        return deviceService.getAllDevicesSorted();
    }

    @GetMapping("/topology")
    public List<DeviceTreeDTO> getAllDevices() {
        return deviceService.getAllDeviceTrees();
    }

    @GetMapping("/topology/{macAddress}")
    public ResponseEntity<DeviceTreeDTO> getDeviceTreeByMacAddress(@PathVariable String macAddress) {
        DeviceTreeDTO tree = deviceService.getDeviceTree(macAddress);
        return tree != null ? ResponseEntity.ok(tree) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Device> addDevice(@RequestBody Device device) {
        try {
            Device createdDevice = deviceService.addDevice(device);
            return ResponseEntity.ok(createdDevice);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(null, HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/{macAddress}")
    public ResponseEntity<Device> getDeviceByMacAddress(@PathVariable String macAddress) {
        Device device = deviceService.getDeviceByMacAddress(macAddress);
        return (device != null) ? ResponseEntity.ok(device) : ResponseEntity.notFound().build();
    }


    @PutMapping("/{macAddress}")
    public ResponseEntity<Device> updateDevice(@PathVariable String macAddress, @RequestBody Device updatedDevice) {
        boolean updated = deviceService.updateDevice(macAddress, updatedDevice);
        return updated ? ResponseEntity.ok(updatedDevice) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{macAddress}")
    public ResponseEntity<Void> deleteDevice(@PathVariable String macAddress) {
        return deviceService.deleteDevice(macAddress) ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }
}

