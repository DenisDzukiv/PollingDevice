package androidproject.pollingdevice.model;



public class Characteristics {

    private long chId;
    private String chName;

    public Characteristics(){}

    public Characteristics(long chId, String chName){
        this.chId = chId;
        this.chName = chName;
    }

    public long getChId() {
        return chId;
    }

    public void setChId(long chId) {
        this.chId = chId;
    }

    public String getChName() {
        return chName;
    }

    public void setChName(String chName) {
        this.chName = chName;
    }
}
