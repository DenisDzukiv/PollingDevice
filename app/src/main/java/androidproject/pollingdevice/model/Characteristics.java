package androidproject.pollingdevice.model;



public class Characteristics {

    private long chId;
    private String chValue;

    public Characteristics(){}

    public Characteristics(long chId, String chValue){
        this.chId = chId;
        this.chValue = chValue;
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

    @Override
    public String toString() {
        return super.toString();
    }
}
