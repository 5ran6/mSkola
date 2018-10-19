package mountedwings.org.mskola_mgt.data;

public class NumberPromoteStudents {
    private String name;
    private String session;
    private String regNo;
    private String class_arm;
    private Boolean isSelected = false;


    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }


    public String getclass_arm() {
        return class_arm;
    }

    public void setclass_arm(String class_arm) {
        this.class_arm = class_arm;
    }


    public String getsession() {
        return session;
    }

    public void setsession(String session) {
        this.session = session;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getregNo() {
        return regNo;
    }

    public void setregNo(String regNo) {
        this.regNo = regNo;
    }

}