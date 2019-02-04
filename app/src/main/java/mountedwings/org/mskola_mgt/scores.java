/*
 * Copyright 2019 Mountedwings Cybersystems LTD. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package mountedwings.org.mskola_mgt;

import java.util.ArrayList;

public class scores {
    private static ArrayList<String> NAMES = new ArrayList<>();
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

    private static int NO_STUDENTS;

    public static int getNoStudents() {
        return NO_STUDENTS;
    }

    public static void setNoStudents(int noStudents) {
        NO_STUDENTS = noStudents;
    }

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
        scores.CA1 = CA1;
    }

    public static ArrayList<String> getCA2() {
        return CA2;
    }

    public static void setCA2(ArrayList<String> CA2) {
        scores.CA2 = CA2;
    }

    public static ArrayList<String> getCA3() {
        return CA3;
    }

    public static void setCA3(ArrayList<String> CA3) {
        scores.CA3 = CA3;
    }

    public static ArrayList<String> getCA4() {
        return CA4;
    }

    public static void setCA4(ArrayList<String> CA4) {
        scores.CA4 = CA4;
    }

    public static ArrayList<String> getCA5() {
        return CA5;
    }

    public static void setCA5(ArrayList<String> CA5) {
        scores.CA5 = CA5;
    }

    public static ArrayList<String> getCA6() {
        return CA6;
    }

    public static void setCA6(ArrayList<String> CA6) {
        scores.CA6 = CA6;
    }

    public static ArrayList<String> getCA7() {
        return CA7;
    }

    public static void setCA7(ArrayList<String> CA7) {
        scores.CA7 = CA7;
    }

    public static ArrayList<String> getCA8() {
        return CA8;
    }

    public static void setCA8(ArrayList<String> CA8) {
        scores.CA8 = CA8;
    }

    public static ArrayList<String> getCA9() {
        return CA9;
    }

    public static void setCA9(ArrayList<String> CA9) {
        scores.CA9 = CA9;
    }

    public static ArrayList<String> getCA10() {
        return CA10;
    }

    public static void setCA10(ArrayList<String> CA10) {
        scores.CA10 = CA10;
    }

    public static ArrayList<String> getEXAM() {
        return EXAM;
    }

    public static void setEXAM(ArrayList<String> EXAM) {
        scores.EXAM = EXAM;
    }

    public static ArrayList<String> getTOTAL() {
        return TOTAL;
    }

    public static void setTOTAL(ArrayList<String> TOTAL) {
        scores.TOTAL = TOTAL;
    }

    private static int NO_CAS;

    public static ArrayList<String> getNAMES() {
        return NAMES;
    }

    public static void setNAMES(ArrayList<String> NAMES) {
        scores.NAMES = NAMES;
    }


    public static ArrayList<String> getHEADERS() {
        return HEADERS;
    }

    public static void setHEADERS(ArrayList<String> HEADERS) {
        scores.HEADERS = HEADERS;
    }

    private static ArrayList<String> HEADERS;

}
