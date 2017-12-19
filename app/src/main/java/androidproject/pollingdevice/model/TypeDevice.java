package androidproject.pollingdevice.model;


import java.io.Serializable;
import java.util.List;

public class TypeDevice implements Serializable{
    private long typeId;
    private String typeName;
    private List<Characteristics> characteristicsList;

    public TypeDevice(){}
    public TypeDevice(long typeId, String typeName, List<Characteristics> characteristicsList){
        this.typeId = typeId;
        this.typeName = typeName;
        this.characteristicsList = characteristicsList;
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

    public List<Characteristics> getCharacteristicsList() {
        return characteristicsList;
    }

    public void setCharacteristicsList(List<Characteristics> characteristicsList) {
        this.characteristicsList = characteristicsList;
    }
}
