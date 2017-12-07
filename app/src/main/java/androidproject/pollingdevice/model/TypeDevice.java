package androidproject.pollingdevice.model;


public class TypeDevice {
    private long typeId;
    private String typeName;

    public TypeDevice(){}
    public TypeDevice(long typeId, String typeName){
        this.typeId = typeId;
        this.typeName = typeName;
    }

    public long getTypeId() {
        return typeId;
    }

    public void setTypeId(long typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
