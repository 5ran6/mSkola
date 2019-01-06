package mountedwings.org.mskola_mgt.data;

public class NumberChildrenList {
    private String name;
    private String regNo;
    private String schoolId;
    private String schoolName;
    private String class_name;
    private String arm;


    public String getArm() {
        return arm;
    }

    public void setArm(String arm) {
        this.arm = arm;
    }
    private byte[] Image;

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public String getSchoolId() {
        return schoolId;
    }

    public void setSchoolId(String schoolId) {
        this.schoolId = schoolId;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }


    public void setImage(byte[] image) {
        Image = image;
    }

    private String total;

    public byte[] getImageFile() {
        return Image;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String gettotal() {
        return total;
    }

    public void settotal(String total) {
        this.total = total;
    }

    public String getClass_name() {
        return class_name;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }
}