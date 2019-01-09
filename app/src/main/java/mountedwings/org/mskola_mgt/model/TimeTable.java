package mountedwings.org.mskola_mgt.model;

public class TimeTable {

    public String subject;
    public String time;
    public boolean section = false;

    public TimeTable() {
    }

    public TimeTable(String subject, String time, boolean section) {
        this.subject = subject;
        this.time = time;
        this.section = section;
    }
}
