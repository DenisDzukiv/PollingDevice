package androidproject.pollingdevice.model;


import java.io.Serializable;

public class DeviceCharacteristicsValue implements Serializable {

    private long chId;
    private String chValue;
    private Characteristics chName;

    public DeviceCharacteristicsValue(){}

    public DeviceCharacteristicsValue(long chId, String chValue, Characteristics chName){
        this.chId = chId;
        this.chValue = chValue;
        this.chName = chName;
    }

    public long getChId() {
        return chId;
    }

    public void setChId(long chId) {
        this.chId = chId;
    }

    public String getChValue() {
        return chValue;
    }

    public void setChValue(String chValue) {
        this.chValue = chValue;
    }

    public Characteristics getChName() {
        return chName;
    }

    public void setChName(Characteristics chName) {
        this.chName = chName;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
