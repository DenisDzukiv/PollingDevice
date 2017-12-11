package androidproject.pollingdevice.Validation;


import java.util.List;

import androidproject.pollingdevice.model.DeviceCharacteristicsValue;
import androidproject.pollingdevice.model.Device;

public class Validation {

    public static Boolean validDevice(Device device) {
        boolean result = true;
        if (device.getDevName().length() == 0) {
            result = false;
        }
        for(DeviceCharacteristicsValue ch : device.getCharacteristicsList()){
            if(ch.getChValue().length() == 0) result = false;
        }

        return result;
    }
}
