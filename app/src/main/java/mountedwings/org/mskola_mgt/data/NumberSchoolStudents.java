package mountedwings.org.mskola_mgt.data;

public class NumberSchoolStudents {

    private String school_name;
    private String school_id;
    private String address;
    private String student_reg_no;
    private byte[] logo;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStudent_reg_no() {
        return student_reg_no;
    }

    public void setStudent_reg_no(String student_reg_no) {
        this.student_reg_no = student_reg_no;
    }


    public byte[] getLogo() {
        return logo;
    }

    public void setLogo(byte[] logo) {
        this.logo = logo;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public String getSchool_id() {
        return school_id;
    }

    public void setSchool_id(String school_id) {
        this.school_id = school_id;
    }

    public String getSchool_address() {
        return school_address;
    }

    public void setSchool_address(String school_address) {
        this.school_address = school_address;
    }

    private String school_address;

}