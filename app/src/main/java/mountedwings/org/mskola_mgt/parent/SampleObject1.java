package mountedwings.org.mskola_mgt.parent;

public class SampleObject1 {

    String SUBJECT;
    String CA1;
    String EXAM;
    String TOTAL;
    String CLASS_AVERAGE;
    String HIGHEST;
    String LOWEST;
    String GRADE;

    protected SampleObject1(String SUBJECT, String CA1, String EXAM, String TOTAL,
                            String CLASS_AVERAGE, String HIGHEST, String LOWEST, String GRADE) {

        this.SUBJECT = SUBJECT;
        this.CA1 = CA1;
        this.EXAM = EXAM;
        this.TOTAL = TOTAL;
        this.CLASS_AVERAGE = CLASS_AVERAGE;
        this.HIGHEST = HIGHEST;
        this.LOWEST = LOWEST;
        this.GRADE = GRADE;
    }
}