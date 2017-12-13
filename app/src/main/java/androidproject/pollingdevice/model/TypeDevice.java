package androidproject.pollingdevice.model;


import java.io.Serializable;

public class TypeDevice implements Serializable{
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
