package mountedwings.org.mskola_mgt.parent;

public class SampleObject2 {

    String SUBJECT;
    String CA1;
    String CA2;

    String EXAM;
    String TOTAL;
    String CLASS_AVERAGE;
    String HIGHEST;
    String LOWEST;
    String GRADE;

    protected SampleObject2(String SUBJECT, String CA1, String CA2, String EXAM, String TOTAL,
                            String CLASS_AVERAGE, String HIGHEST, String LOWEST, String GRADE) {

        this.SUBJECT = SUBJECT;
        this.CA1 = CA1;
        this.CA2 = CA2;

        this.EXAM = EXAM;
        this.TOTAL = TOTAL;
        this.CLASS_AVERAGE = CLASS_AVERAGE;
        this.HIGHEST = HIGHEST;
        this.LOWEST = LOWEST;
        this.GRADE = GRADE;
    }
}