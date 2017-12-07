package androidproject.pollingdevice.model;

import java.util.List;

public class Device {
    private long devId;
    private String devName;
    private long typeId;
    private List<Characteristics> characteristicsList;
    public Device(){}

    public Device(long devId, String devName, long typeId, List<Characteristics> characteristicsList){
        this.devId = devId;
        this.devName = devName;
        this.typeId = typeId;
        this.characteristicsList = characteristicsList;
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

    public List<Characteristics> getCharacteristicsList() {
        return characteristicsList;
    }

    public void setCharacteristicsList(List<Characteristics> characteristicsList) {
        this.characteristicsList = characteristicsList;
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
