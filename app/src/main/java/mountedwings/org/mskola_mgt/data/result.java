package mountedwings.org.mskola_mgt.data;

import java.util.ArrayList;

public class result {
    private static ArrayList<String> SUBJECTS = new ArrayList<>();
    private static ArrayList<String> CA1 = new ArrayList<>();
    private static ArrayList<String> CA2 = new ArrayList<>();
    private static ArrayList<String> CA3 = new ArrayList<>();
    private static ArrayList<String> CA4 = new ArrayList<>();
    private static ArrayList<String> CA5 = new ArrayList<>();
    private static ArrayList<String> CA6 = new ArrayList<>();
    private static ArrayList<String> CA7 = new ArrayList<>();
    private static ArrayList<String> CA8 = new ArrayList<>();
    private static ArrayList<String> CA9 = new ArrayList<>();
    private static ArrayList<String> CA10 = new ArrayList<>();
    private static ArrayList<String> EXAM = new ArrayList<>();
    private static ArrayList<String> TOTAL = new ArrayList<>();
    private static ArrayList<String> CLASS_AVERAGE = new ArrayList<>();
    private static ArrayList<String> HIGHEST = new ArrayList<>();
    private static ArrayList<String> LOWEST = new ArrayList<>();
    private static ArrayList<String> GRADE = new ArrayList<>();

    public static ArrayList<String> getClassAverage() {
        return CLASS_AVERAGE;
    }

    public static void setClassAverage(ArrayList<String> classAverage) {
        CLASS_AVERAGE = classAverage;
    }

    public static ArrayList<String> getHIGHEST() {
        return HIGHEST;
    }

    public static void setHIGHEST(ArrayList<String> HIGHEST) {
        result.HIGHEST = HIGHEST;
    }

    public static ArrayList<String> getLOWEST() {
        return LOWEST;
    }

    public static void setLOWEST(ArrayList<String> LOWEST) {
        result.LOWEST = LOWEST;
    }

    public static ArrayList<String> getGRADE() {
        return GRADE;
    }

    public static void setGRADE(ArrayList<String> GRADE) {
        result.GRADE = GRADE;
    }


    private static int NO_SUBJECTS;


    //CA's are without exam and total
    public static int getNoCas() {
        return NO_CAS;
    }

    public static void setNoCas(int noCas) {
        NO_CAS = noCas;
    }

    public static ArrayList<String> getCA1() {
        return CA1;
    }

    public static void setCA1(ArrayList<String> CA1) {
        result.CA1 = CA1;
    }

    public static ArrayList<String> getCA2() {
        return CA2;
    }

    public static void setCA2(ArrayList<String> CA2) {
        result.CA2 = CA2;
    }

    public static ArrayList<String> getCA3() {
        return CA3;
    }

    public static void setCA3(ArrayList<String> CA3) {
        result.CA3 = CA3;
    }

    public static ArrayList<String> getCA4() {
        return CA4;
    }

    public static void setCA4(ArrayList<String> CA4) {
        result.CA4 = CA4;
    }

    public static ArrayList<String> getCA5() {
        return CA5;
    }

    public static void setCA5(ArrayList<String> CA5) {
        result.CA5 = CA5;
    }

    public static ArrayList<String> getCA6() {
        return CA6;
    }

    public static void setCA6(ArrayList<String> CA6) {
        result.CA6 = CA6;
    }

    public static ArrayList<String> getCA7() {
        return CA7;
    }

    public static void setCA7(ArrayList<String> CA7) {
        result.CA7 = CA7;
    }

    public static ArrayList<String> getCA8() {
        return CA8;
    }

    public static void setCA8(ArrayList<String> CA8) {
        result.CA8 = CA8;
    }

    public static ArrayList<String> getCA9() {
        return CA9;
    }

    public static void setCA9(ArrayList<String> CA9) {
        result.CA9 = CA9;
    }

    public static ArrayList<String> getCA10() {
        return CA10;
    }

    public static void setCA10(ArrayList<String> CA10) {
        result.CA10 = CA10;
    }

    public static ArrayList<String> getEXAM() {
        return EXAM;
    }

    public static void setEXAM(ArrayList<String> EXAM) {
        result.EXAM = EXAM;
    }

    public static ArrayList<String> getTOTAL() {
        return TOTAL;
    }

    public static void setTOTAL(ArrayList<String> TOTAL) {
        result.TOTAL = TOTAL;
    }

    private static int NO_CAS;

    public static ArrayList<String> getHEADERS() {
        return HEADERS;
    }

    public static void setHEADERS(ArrayList<String> HEADERS) {
        result.HEADERS = HEADERS;
    }

    private static ArrayList<String> HEADERS;

    public static ArrayList<String> getSUBJECTS() {
        return SUBJECTS;
    }

    public static void setSUBJECTS(ArrayList<String> SUBJECTS) {
        result.SUBJECTS = SUBJECTS;
    }

    public static int getNoSubjects() {
        return NO_SUBJECTS;
    }

    public static void setNoSubjects(int noSubjects) {
        NO_SUBJECTS = noSubjects;
    }
}
