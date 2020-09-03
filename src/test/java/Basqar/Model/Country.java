package Basqar.Model;

import java.util.List;

public class Country {
    private String id;
    private String name;
    private String shortName;
    private List<Object> translateName;
    private String code;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public List<Object> getTranslateName() {
        return translateName;
    }

    public void setTranslateName(List<Object> translateName) {
        this.translateName = translateName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Country{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", shortName='" + shortName + '\'' +
                ", translateName=" + translateName +
                ", code='" + code + '\'' +
                '}';
    }
}
