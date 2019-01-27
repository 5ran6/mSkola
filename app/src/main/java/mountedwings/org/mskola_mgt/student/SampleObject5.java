package mountedwings.org.mskola_mgt.student;

public class SampleObject5 {

    String SUBJECT;
    String CA1;
    String CA2;
    String CA3;
    String CA4;
    String CA5;

    String EXAM;
    String TOTAL;
    String CLASS_AVERAGE;
    String HIGHEST;
    String LOWEST;
    String GRADE;

    protected SampleObject5(String SUBJECT, String CA1, String CA2, String CA3,
                            String CA4, String CA5, String EXAM, String TOTAL,
                            String CLASS_AVERAGE, String HIGHEST, String LOWEST, String GRADE) {

        this.SUBJECT = SUBJECT;
        this.CA1 = CA1;
        this.CA2 = CA2;
        this.CA3 = CA3;
        this.CA4 = CA4;
        this.CA5 = CA5;

        this.EXAM = EXAM;
        this.TOTAL = TOTAL;
        this.CLASS_AVERAGE = CLASS_AVERAGE;
        this.HIGHEST = HIGHEST;
        this.LOWEST = LOWEST;
        this.GRADE = GRADE;
    }
}