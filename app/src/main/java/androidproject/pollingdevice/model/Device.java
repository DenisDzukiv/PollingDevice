package androidproject.pollingdevice.model;

import java.io.Serializable;
import java.util.List;

public class Device implements Serializable {
    private long devId;
    private String devName;
    private long typeId;
    private TypeDevice typeDevice;
    private List<DeviceCharacteristicsValue> characteristicsList;
    private boolean box;

    public Device(){}

    public Device(long devId, String devName, long typeId, List<DeviceCharacteristicsValue> characteristicsList, TypeDevice typeDevice, boolean box){
        this.devId = devId;
        this.devName = devName;
        this.typeId = typeId;
        this.characteristicsList = characteristicsList;
        this.typeDevice = typeDevice;
        this.box = box;
    }

    public long getDevId() {
        return devId;
    }

    public void setDevId(long devId) {
        this.devId = devId;
    }

    public String getDevName() {
        return devName;
    }

    public void setDevName(String devName) {
        this.devName = devName;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public List<DeviceCharacteristicsValue> getCharacteristicsList() {
        return characteristicsList;
    }

    public void setCharacteristicsList(List<DeviceCharacteristicsValue> characteristicsList) {
        this.characteristicsList = characteristicsList;
    }

    public TypeDevice getTypeDevice() {
        return typeDevice;
    }

    public void setTypeDevice(TypeDevice typeDevice) {
        this.typeDevice = typeDevice;
    }

    public boolean getBox() {
        return box;
    }

    public void setBox(boolean box) {
        this.box = box;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
