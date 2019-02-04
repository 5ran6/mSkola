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

package mountedwings.org.mskola_mgt.parent;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mountedwings.org.mskola_mgt.R;
import mountedwings.org.mskola_mgt.data.result;

public class TableMainLayout extends RelativeLayout {

    public final String TAG = "TableMainLayout.java";

    // set the header titles
    String headers[] = new String[result.getHEADERS().size()];

    TableLayout tableA;
    TableLayout tableB;
    TableLayout tableC;
    TableLayout tableD;

    HorizontalScrollView horizontalScrollViewB;
    HorizontalScrollView horizontalScrollViewD;

    ScrollView scrollViewC;
    ScrollView scrollViewD;

    Context context;

    List<SampleObject1> sampleObjects1;
    List<SampleObject2> sampleObjects2;
    List<SampleObject3> sampleObjects3;
    List<SampleObject4> sampleObjects4;
    List<SampleObject5> sampleObjects5;
    List<SampleObject6> sampleObjects6;
    List<SampleObject7> sampleObjects7;
    List<SampleObject8> sampleObjects8;
    List<SampleObject9> sampleObjects9;
    List<SampleObject> sampleObjects;

    int[] headerCellsWidth;

    public TableMainLayout(Context context) {

        super(context);
        this.context = context;

        for (int i = 0; i < result.getHEADERS().size(); i++) {
            headers[i] = result.getHEADERS().get(i);
        }

        headerCellsWidth = new int[headers.length];


        switch (result.getNoCas() - 6) {
            case 1:
                sampleObjects1 = this.sampleObjects1();
                break;
            case 2:
                sampleObjects2 = this.sampleObjects2();
                break;
            case 3:
                sampleObjects3 = this.sampleObjects3();
                break;
            case 4:
                sampleObjects4 = this.sampleObjects4();
                break;
            case 5:
                sampleObjects5 = this.sampleObjects5();
                break;
            case 6:
                sampleObjects6 = this.sampleObjects6();
                break;
            case 7:
                sampleObjects7 = this.sampleObjects7();
                break;
            case 8:
                sampleObjects8 = this.sampleObjects8();
                break;
            case 9:
                sampleObjects9 = this.sampleObjects9();
                break;
            case 10:
                sampleObjects = this.sampleObjects();
                break;
        }


        // initialize the main components (TableLayouts, HorizontalScrollView, ScrollView)
        this.initComponents();
        this.setComponentsId();
        this.setScrollViewAndHorizontalScrollViewTag();


        // no need to assemble component A, since it is just a table
        this.horizontalScrollViewB.addView(this.tableB);

        this.scrollViewC.addView(this.tableC);

        this.scrollViewD.addView(this.horizontalScrollViewD);
        this.horizontalScrollViewD.addView(this.tableD);

        // add the components to be part of the main layout
        this.addComponentToMainLayout();
        this.setBackgroundColor(Color.WHITE);

        // add some table rows
        this.addTableRowToTableA();
        this.addTableRowToTableB();

        this.resizeHeaderHeight();

        this.getTableRowHeaderCellWidth();

        this.generateTableC_AndTable_B();

        this.resizeBodyTableRowHeight();

    }

    // this is the data
    List<SampleObject1> sampleObjects1() {

        List<SampleObject1> sampleObjects = new ArrayList<>();
        //     try {
        for (int x = 0; x < result.getNoSubjects(); x++) {
            SampleObject1 sampleObject = new SampleObject1(
                    result.getSUBJECTS().get(x),
                    result.getCA1().get(x),
                    result.getEXAM().get(x),
                    result.getTOTAL().get(x),
                    result.getClassAverage().get(x),
                    result.getHIGHEST().get(x),
                    result.getLOWEST().get(x),
                    result.getGRADE().get(x)
            );
            sampleObjects.add(sampleObject);
        }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        return sampleObjects;

    }

    List<SampleObject2> sampleObjects2() {

        List<SampleObject2> sampleObjects = new ArrayList<>();
        //     try {
        for (int x = 0; x < result.getNoSubjects(); x++) {
            SampleObject2 sampleObject = new SampleObject2(
                    result.getSUBJECTS().get(x),
                    result.getCA1().get(x),
                    result.getCA2().get(x),
                    result.getEXAM().get(x),
                    result.getTOTAL().get(x),
                    result.getClassAverage().get(x),
                    result.getHIGHEST().get(x),
                    result.getLOWEST().get(x),
                    result.getGRADE().get(x)
            );
            sampleObjects.add(sampleObject);
        }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        return sampleObjects;

    }


    List<SampleObject3> sampleObjects3() {

        List<SampleObject3> sampleObjects = new ArrayList<>();
        //     try {
        for (int x = 0; x < result.getNoSubjects(); x++) {
            SampleObject3 sampleObject = new SampleObject3(
                    result.getSUBJECTS().get(x),
                    result.getCA1().get(x),
                    result.getCA2().get(x),
                    result.getCA3().get(x),
                    result.getEXAM().get(x),
                    result.getTOTAL().get(x),
                    result.getClassAverage().get(x),
                    result.getHIGHEST().get(x),
                    result.getLOWEST().get(x),
                    result.getGRADE().get(x)
            );
            sampleObjects.add(sampleObject);
        }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        return sampleObjects;
    }


    List<SampleObject4> sampleObjects4() {

        List<SampleObject4> sampleObjects = new ArrayList<>();
        //     try {
        for (int x = 0; x < result.getNoSubjects(); x++) {
            SampleObject4 sampleObject = new SampleObject4(
                    result.getSUBJECTS().get(x),
                    result.getCA1().get(x),
                    result.getCA2().get(x),
                    result.getCA3().get(x),
                    result.getCA4().get(x),
                    result.getEXAM().get(x),
                    result.getTOTAL().get(x),
                    result.getClassAverage().get(x),
                    result.getHIGHEST().get(x),
                    result.getLOWEST().get(x),
                    result.getGRADE().get(x)
            );
            sampleObjects.add(sampleObject);
        }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        return sampleObjects;
    }


    List<SampleObject5> sampleObjects5() {

        List<SampleObject5> sampleObjects = new ArrayList<>();
        //     try {
        for (int x = 0; x < result.getNoSubjects(); x++) {
            SampleObject5 sampleObject = new SampleObject5(
                    result.getSUBJECTS().get(x),
                    result.getCA1().get(x),
                    result.getCA2().get(x),
                    result.getCA3().get(x),
                    result.getCA4().get(x),
                    result.getCA5().get(x),
                    result.getEXAM().get(x),
                    result.getTOTAL().get(x),
                    result.getClassAverage().get(x),
                    result.getHIGHEST().get(x),
                    result.getLOWEST().get(x),
                    result.getGRADE().get(x)
            );
            sampleObjects.add(sampleObject);
        }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        return sampleObjects;

    }

    List<SampleObject6> sampleObjects6() {

        List<SampleObject6> sampleObjects = new ArrayList<>();
        //     try {
        for (int x = 0; x < result.getNoSubjects(); x++) {
            SampleObject6 sampleObject = new SampleObject6(
                    result.getSUBJECTS().get(x),
                    result.getCA1().get(x),
                    result.getCA2().get(x),
                    result.getCA3().get(x),
                    result.getCA4().get(x),
                    result.getCA5().get(x),
                    result.getCA6().get(x),
                    result.getEXAM().get(x),
                    result.getTOTAL().get(x),
                    result.getClassAverage().get(x),
                    result.getHIGHEST().get(x),
                    result.getLOWEST().get(x),
                    result.getGRADE().get(x)
            );
            sampleObjects.add(sampleObject);
        }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        return sampleObjects;

    }


    List<SampleObject7> sampleObjects7() {

        List<SampleObject7> sampleObjects = new ArrayList<>();
        //     try {
        for (int x = 0; x < result.getNoSubjects(); x++) {
            SampleObject7 sampleObject = new SampleObject7(
                    result.getSUBJECTS().get(x),
                    result.getCA1().get(x),
                    result.getCA2().get(x),
                    result.getCA3().get(x),
                    result.getCA4().get(x),
                    result.getCA5().get(x),
                    result.getCA6().get(x),
                    result.getCA7().get(x),
                    result.getEXAM().get(x),
                    result.getTOTAL().get(x),
                    result.getClassAverage().get(x),
                    result.getHIGHEST().get(x),
                    result.getLOWEST().get(x),
                    result.getGRADE().get(x)
            );
            sampleObjects.add(sampleObject);
        }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        return sampleObjects;

    }

    List<SampleObject8> sampleObjects8() {

        List<SampleObject8> sampleObjects = new ArrayList<>();
        //     try {
        for (int x = 0; x < result.getNoSubjects(); x++) {
            SampleObject8 sampleObject = new SampleObject8(
                    result.getSUBJECTS().get(x),
                    result.getCA1().get(x),
                    result.getCA2().get(x),
                    result.getCA3().get(x),
                    result.getCA4().get(x),
                    result.getCA5().get(x),
                    result.getCA6().get(x),
                    result.getCA7().get(x),
                    result.getCA8().get(x),
                    result.getEXAM().get(x),
                    result.getTOTAL().get(x),
                    result.getClassAverage().get(x),
                    result.getHIGHEST().get(x),
                    result.getLOWEST().get(x),
                    result.getGRADE().get(x)
            );
            sampleObjects.add(sampleObject);
        }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        return sampleObjects;

    }

    List<SampleObject9> sampleObjects9() {

        List<SampleObject9> sampleObjects = new ArrayList<>();
        //     try {
        for (int x = 0; x < result.getNoSubjects(); x++) {
            SampleObject9 sampleObject = new SampleObject9(
                    result.getSUBJECTS().get(x),
                    result.getCA1().get(x),
                    result.getCA2().get(x),
                    result.getCA3().get(x),
                    result.getCA4().get(x),
                    result.getCA5().get(x),
                    result.getCA6().get(x),
                    result.getCA7().get(x),
                    result.getCA8().get(x),
                    result.getCA9().get(x),
                    result.getEXAM().get(x),
                    result.getTOTAL().get(x),
                    result.getClassAverage().get(x),
                    result.getHIGHEST().get(x),
                    result.getLOWEST().get(x),
                    result.getGRADE().get(x)
            );
            sampleObjects.add(sampleObject);
        }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        return sampleObjects;

    }

    List<SampleObject> sampleObjects() {

        List<SampleObject> sampleObjects = new ArrayList<>();
        //     try {
        for (int x = 0; x < result.getNoSubjects(); x++) {
            SampleObject sampleObject = new SampleObject(
                    result.getSUBJECTS().get(x),
                    result.getCA1().get(x),
                    result.getCA2().get(x),
                    result.getCA3().get(x),
                    result.getCA4().get(x),
                    result.getCA5().get(x),
                    result.getCA6().get(x),
                    result.getCA7().get(x),
                    result.getCA8().get(x),
                    result.getCA9().get(x),
                    result.getCA10().get(x),
                    result.getEXAM().get(x),
                    result.getTOTAL().get(x),
                    result.getClassAverage().get(x),
                    result.getHIGHEST().get(x),
                    result.getLOWEST().get(x),
                    result.getGRADE().get(x)
            );
            sampleObjects.add(sampleObject);
        }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
        return sampleObjects;

    }


    // initialized components
    private void initComponents() {

        this.tableA = new TableLayout(this.context);
        this.tableB = new TableLayout(this.context);
        this.tableC = new TableLayout(this.context);
        this.tableD = new TableLayout(this.context);

        this.horizontalScrollViewB = new MyHorizontalScrollView(this.context);
        this.horizontalScrollViewD = new MyHorizontalScrollView(this.context);

        this.scrollViewC = new MyScrollView(this.context);
        this.scrollViewD = new MyScrollView(this.context);

        this.tableA.setBackgroundColor(Color.GREEN);
        this.horizontalScrollViewB.setBackgroundColor(Color.LTGRAY);
    }

    // set essential component IDs
    @SuppressLint("ResourceType")
    private void setComponentsId() {
        this.tableA.setId(1);
        this.horizontalScrollViewB.setId(2);
        this.scrollViewC.setId(3);
        this.scrollViewD.setId(4);
    }

    // set tags for some horizontal and vertical scroll view
    private void setScrollViewAndHorizontalScrollViewTag() {

        this.horizontalScrollViewB.setTag("horizontal scroll view b");
        this.horizontalScrollViewD.setTag("horizontal scroll view d");

        this.scrollViewC.setTag("scroll view c");
        this.scrollViewD.setTag("scroll view d");
    }

    // we add the components here in our TableMainLayout
    private void addComponentToMainLayout() {

        // RelativeLayout params were very useful here
        // the addRule method is the key to arrange the components properly
        LayoutParams componentB_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentB_Params.addRule(RelativeLayout.RIGHT_OF, this.tableA.getId());

        LayoutParams componentC_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentC_Params.addRule(RelativeLayout.BELOW, this.tableA.getId());

        LayoutParams componentD_Params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        componentD_Params.addRule(RelativeLayout.RIGHT_OF, this.scrollViewC.getId());
        componentD_Params.addRule(RelativeLayout.BELOW, this.horizontalScrollViewB.getId());

        // 'this' is a relative layout,
        // we extend this table layout as relative layout as seen during the creation of this class
        this.addView(this.tableA);
        this.addView(this.horizontalScrollViewB, componentB_Params);
        this.addView(this.scrollViewC, componentC_Params);
        this.addView(this.scrollViewD, componentD_Params);

    }


    private void addTableRowToTableA() {
        this.tableA.addView(this.componentATableRow());
    }

    private void addTableRowToTableB() {
        this.tableB.addView(this.componentBTableRow());
    }

    // generate table row of table A
    TableRow componentATableRow() {

        TableRow componentATableRow = new TableRow(this.context);
        TextView textView = this.headerTextView(this.headers[0]);

        componentATableRow.addView(textView);

        return componentATableRow;
    }

    // generate table row of table B
    TableRow componentBTableRow() {

        TableRow componentBTableRow = new TableRow(this.context);
        int headerFieldCount = this.headers.length;

        TableRow.LayoutParams params = new TableRow.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        params.setMargins(2, 0, 0, 0);

//        for (int x = 0; x < (headerFieldCount - 1); x++) {
        for (int x = 0; x < result.getNoCas(); x++) {
            TextView textView = this.headerTextView(this.headers[x + 1]);
            textView.setLayoutParams(params);
            componentBTableRow.addView(textView);
        }

        return componentBTableRow;
    }

    // generate table row of table C and table D
    private void generateTableC_AndTable_B() {
        switch (result.getNoCas() - 6) {
            case 1:
                for (SampleObject1 sampleObject : this.sampleObjects1) {

                    TableRow tableRowForTableC = this.tableRowForTableC1(sampleObject);
                    TableRow taleRowForTableD = this.taleRowForTableD1(sampleObject);

                    tableRowForTableC.setBackgroundColor(Color.LTGRAY);
                    taleRowForTableD.setBackgroundColor(Color.LTGRAY);

                    this.tableC.addView(tableRowForTableC);
                    this.tableD.addView(taleRowForTableD);

                }

                break;

            case 2:
                for (SampleObject2 sampleObject : this.sampleObjects2) {

                    TableRow tableRowForTableC = this.tableRowForTableC2(sampleObject);
                    TableRow taleRowForTableD = this.taleRowForTableD2(sampleObject);

                    tableRowForTableC.setBackgroundColor(Color.LTGRAY);
                    taleRowForTableD.setBackgroundColor(Color.LTGRAY);

                    this.tableC.addView(tableRowForTableC);
                    this.tableD.addView(taleRowForTableD);

                }

                break;

            case 3:
                for (SampleObject3 sampleObject : this.sampleObjects3) {

                    TableRow tableRowForTableC = this.tableRowForTableC3(sampleObject);
                    TableRow taleRowForTableD = this.taleRowForTableD3(sampleObject);

                    tableRowForTableC.setBackgroundColor(Color.LTGRAY);
                    taleRowForTableD.setBackgroundColor(Color.LTGRAY);

                    this.tableC.addView(tableRowForTableC);
                    this.tableD.addView(taleRowForTableD);

                }

                break;

            case 4:
                for (SampleObject4 sampleObject : this.sampleObjects4) {

                    TableRow tableRowForTableC = this.tableRowForTableC4(sampleObject);
                    TableRow taleRowForTableD = this.taleRowForTableD4(sampleObject);

                    tableRowForTableC.setBackgroundColor(Color.LTGRAY);
                    taleRowForTableD.setBackgroundColor(Color.LTGRAY);

                    this.tableC.addView(tableRowForTableC);
                    this.tableD.addView(taleRowForTableD);

                }

                break;

            case 5:
                for (SampleObject5 sampleObject : this.sampleObjects5) {

                    TableRow tableRowForTableC = this.tableRowForTableC5(sampleObject);
                    TableRow taleRowForTableD = this.taleRowForTableD5(sampleObject);

                    tableRowForTableC.setBackgroundColor(Color.LTGRAY);
                    taleRowForTableD.setBackgroundColor(Color.LTGRAY);

                    this.tableC.addView(tableRowForTableC);
                    this.tableD.addView(taleRowForTableD);

                }

                break;

            case 6:
                for (SampleObject6 sampleObject : this.sampleObjects6) {

                    TableRow tableRowForTableC = this.tableRowForTableC6(sampleObject);
                    TableRow taleRowForTableD = this.taleRowForTableD6(sampleObject);

                    tableRowForTableC.setBackgroundColor(Color.LTGRAY);
                    taleRowForTableD.setBackgroundColor(Color.LTGRAY);

                    this.tableC.addView(tableRowForTableC);
                    this.tableD.addView(taleRowForTableD);

                }

                break;

            case 7:
                for (SampleObject7 sampleObject : this.sampleObjects7) {

                    TableRow tableRowForTableC = this.tableRowForTableC7(sampleObject);
                    TableRow taleRowForTableD = this.taleRowForTableD7(sampleObject);

                    tableRowForTableC.setBackgroundColor(Color.LTGRAY);
                    taleRowForTableD.setBackgroundColor(Color.LTGRAY);

                    this.tableC.addView(tableRowForTableC);
                    this.tableD.addView(taleRowForTableD);

                }

                break;

            case 8:
                for (SampleObject8 sampleObject : this.sampleObjects8) {

                    TableRow tableRowForTableC = this.tableRowForTableC8(sampleObject);
                    TableRow taleRowForTableD = this.taleRowForTableD8(sampleObject);

                    tableRowForTableC.setBackgroundColor(Color.LTGRAY);
                    taleRowForTableD.setBackgroundColor(Color.LTGRAY);

                    this.tableC.addView(tableRowForTableC);
                    this.tableD.addView(taleRowForTableD);

                }

                break;

            case 9:
                for (SampleObject9 sampleObject : this.sampleObjects9) {

                    TableRow tableRowForTableC = this.tableRowForTableC9(sampleObject);
                    TableRow taleRowForTableD = this.taleRowForTableD9(sampleObject);

                    tableRowForTableC.setBackgroundColor(Color.LTGRAY);
                    taleRowForTableD.setBackgroundColor(Color.LTGRAY);

                    this.tableC.addView(tableRowForTableC);
                    this.tableD.addView(taleRowForTableD);

                }

                break;

            case 10:
                for (SampleObject sampleObject : this.sampleObjects) {

                    TableRow tableRowForTableC = this.tableRowForTableC(sampleObject);
                    TableRow taleRowForTableD = this.taleRowForTableD(sampleObject);

                    tableRowForTableC.setBackgroundColor(Color.LTGRAY);
                    taleRowForTableD.setBackgroundColor(Color.LTGRAY);

                    this.tableC.addView(tableRowForTableC);
                    this.tableD.addView(taleRowForTableD);

                }
                break;

        }


    }

    // a TableRow for table C
    TableRow tableRowForTableC1(SampleObject1 sampleObject) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);
        TextView textView = this.namesTextView(sampleObject.SUBJECT);
        tableRowForTableC.addView(textView, params);

        return tableRowForTableC;
    }

    TableRow tableRowForTableC2(SampleObject2 sampleObject) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);
        TextView textView = this.namesTextView(sampleObject.SUBJECT);
        tableRowForTableC.addView(textView, params);

        return tableRowForTableC;
    }

    TableRow tableRowForTableC3(SampleObject3 sampleObject) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);
        TextView textView = this.namesTextView(sampleObject.SUBJECT);
        tableRowForTableC.addView(textView, params);

        return tableRowForTableC;
    }

    TableRow tableRowForTableC4(SampleObject4 sampleObject) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);
        TextView textView = this.namesTextView(sampleObject.SUBJECT);
        tableRowForTableC.addView(textView, params);

        return tableRowForTableC;
    }

    TableRow tableRowForTableC5(SampleObject5 sampleObject) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);
        TextView textView = this.namesTextView(sampleObject.SUBJECT);
        tableRowForTableC.addView(textView, params);

        return tableRowForTableC;
    }

    TableRow tableRowForTableC6(SampleObject6 sampleObject) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);
        TextView textView = this.namesTextView(sampleObject.SUBJECT);
        tableRowForTableC.addView(textView, params);

        return tableRowForTableC;
    }

    TableRow tableRowForTableC7(SampleObject7 sampleObject) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);
        TextView textView = this.namesTextView(sampleObject.SUBJECT);
        tableRowForTableC.addView(textView, params);

        return tableRowForTableC;
    }

    TableRow tableRowForTableC8(SampleObject8 sampleObject) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);
        TextView textView = this.namesTextView(sampleObject.SUBJECT);
        tableRowForTableC.addView(textView, params);

        return tableRowForTableC;
    }

    TableRow tableRowForTableC9(SampleObject9 sampleObject) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);
        TextView textView = this.namesTextView(sampleObject.SUBJECT);
        tableRowForTableC.addView(textView, params);

        return tableRowForTableC;
    }

    TableRow tableRowForTableC(SampleObject sampleObject) {

        TableRow.LayoutParams params = new TableRow.LayoutParams(this.headerCellsWidth[0], LayoutParams.MATCH_PARENT);
        params.setMargins(0, 2, 0, 0);

        TableRow tableRowForTableC = new TableRow(this.context);
        TextView textView = this.namesTextView(sampleObject.SUBJECT);
        tableRowForTableC.addView(textView, params);

        return tableRowForTableC;
    }


    TableRow taleRowForTableD1(SampleObject1 sampleObject) {

        TableRow taleRowForTableD = new TableRow(this.context);
        String info[] = {
                sampleObject.CA1,
                sampleObject.EXAM,
                sampleObject.TOTAL,
                sampleObject.CLASS_AVERAGE,
                sampleObject.HIGHEST,
                sampleObject.LOWEST,
                sampleObject.GRADE
        };
        for (int x = 0; x < result.getNoCas(); x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);

            TextView textViewB = this.bodyTextView(info[x]);
            taleRowForTableD.addView(textViewB, params);
        }


        //    int loopCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();

        return taleRowForTableD;

    }

    TableRow taleRowForTableD2(SampleObject2 sampleObject) {

        TableRow taleRowForTableD = new TableRow(this.context);
        String info[] = {
                sampleObject.CA1,
                sampleObject.CA2,
                sampleObject.EXAM,
                sampleObject.TOTAL,
                sampleObject.CLASS_AVERAGE,
                sampleObject.HIGHEST,
                sampleObject.LOWEST,
                sampleObject.GRADE
        };
        for (int x = 0; x < result.getNoCas(); x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);

            TextView textViewB = this.bodyTextView(info[x]);
            taleRowForTableD.addView(textViewB, params);
        }


        return taleRowForTableD;

    }

    TableRow taleRowForTableD3(SampleObject3 sampleObject) {

        TableRow taleRowForTableD = new TableRow(this.context);
        String info[] = {
                sampleObject.CA1,
                sampleObject.CA2,
                sampleObject.CA3,
                sampleObject.EXAM,
                sampleObject.TOTAL,
                sampleObject.CLASS_AVERAGE,
                sampleObject.HIGHEST,
                sampleObject.LOWEST,
                sampleObject.GRADE
        };
        for (int x = 0; x < result.getNoCas(); x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);

            TextView textViewB = this.bodyTextView(info[x]);
            taleRowForTableD.addView(textViewB, params);
        }

        Log.i(TAG, "reached here");

        return taleRowForTableD;

    }

    TableRow taleRowForTableD4(SampleObject4 sampleObject) {

        TableRow taleRowForTableD = new TableRow(this.context);
        String info[] = {
                sampleObject.CA1,
                sampleObject.CA2,
                sampleObject.CA3,
                sampleObject.CA4,
                sampleObject.EXAM,
                sampleObject.TOTAL,
                sampleObject.CLASS_AVERAGE,
                sampleObject.HIGHEST,
                sampleObject.LOWEST,
                sampleObject.GRADE
        };
        for (int x = 0; x < result.getNoCas(); x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);

            TextView textViewB = this.bodyTextView(info[x]);
            taleRowForTableD.addView(textViewB, params);
        }


        return taleRowForTableD;

    }

    TableRow taleRowForTableD5(SampleObject5 sampleObject) {

        TableRow taleRowForTableD = new TableRow(this.context);
        String info[] = {
                sampleObject.CA1,
                sampleObject.CA2,
                sampleObject.CA3,
                sampleObject.CA4,
                sampleObject.CA5,
                sampleObject.EXAM,
                sampleObject.TOTAL,
                sampleObject.CLASS_AVERAGE,
                sampleObject.HIGHEST,
                sampleObject.LOWEST,
                sampleObject.GRADE
        };
        for (int x = 0; x < result.getNoCas(); x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);

            TextView textViewB = this.bodyTextView(info[x]);
            taleRowForTableD.addView(textViewB, params);
        }


        return taleRowForTableD;

    }

    TableRow taleRowForTableD6(SampleObject6 sampleObject) {

        TableRow taleRowForTableD = new TableRow(this.context);
        String info[] = {
                sampleObject.CA1,
                sampleObject.CA2,
                sampleObject.CA3,
                sampleObject.CA4,
                sampleObject.CA5,
                sampleObject.CA6,
                sampleObject.EXAM,
                sampleObject.TOTAL,
                sampleObject.CLASS_AVERAGE,
                sampleObject.HIGHEST,
                sampleObject.LOWEST,
                sampleObject.GRADE
        };
        for (int x = 0; x < result.getNoCas(); x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);

            TextView textViewB = this.bodyTextView(info[x]);
            taleRowForTableD.addView(textViewB, params);
        }


        return taleRowForTableD;

    }

    TableRow taleRowForTableD7(SampleObject7 sampleObject) {

        TableRow taleRowForTableD = new TableRow(this.context);
        String info[] = {
                sampleObject.CA1,
                sampleObject.CA2,
                sampleObject.CA3,
                sampleObject.CA4,
                sampleObject.CA5,
                sampleObject.CA6,
                sampleObject.CA7,
                sampleObject.EXAM,
                sampleObject.TOTAL,
                sampleObject.CLASS_AVERAGE,
                sampleObject.HIGHEST,
                sampleObject.LOWEST,
                sampleObject.GRADE
        };
        for (int x = 0; x < result.getNoCas(); x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);

            TextView textViewB = this.bodyTextView(info[x]);
            taleRowForTableD.addView(textViewB, params);
        }


        return taleRowForTableD;

    }

    TableRow taleRowForTableD8(SampleObject8 sampleObject) {

        TableRow taleRowForTableD = new TableRow(this.context);
        String info[] = {
                sampleObject.CA1,
                sampleObject.CA2,
                sampleObject.CA3,
                sampleObject.CA4,
                sampleObject.CA5,
                sampleObject.CA6,
                sampleObject.CA7,
                sampleObject.CA8,
                sampleObject.EXAM,
                sampleObject.TOTAL,
                sampleObject.CLASS_AVERAGE,
                sampleObject.HIGHEST,
                sampleObject.LOWEST,
                sampleObject.GRADE
        };
        for (int x = 0; x < result.getNoCas(); x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);

            TextView textViewB = this.bodyTextView(info[x]);
            taleRowForTableD.addView(textViewB, params);
        }

        return taleRowForTableD;

    }

    TableRow taleRowForTableD9(SampleObject9 sampleObject) {

        TableRow taleRowForTableD = new TableRow(this.context);
        String info[] = {
                sampleObject.CA1,
                sampleObject.CA2,
                sampleObject.CA3,
                sampleObject.CA4,
                sampleObject.CA5,
                sampleObject.CA6,
                sampleObject.CA7,
                sampleObject.CA8,
                sampleObject.CA9,
                sampleObject.EXAM,
                sampleObject.TOTAL,
                sampleObject.CLASS_AVERAGE,
                sampleObject.HIGHEST,
                sampleObject.LOWEST,
                sampleObject.GRADE
        };
        for (int x = 0; x < result.getNoCas(); x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);

            TextView textViewB = this.bodyTextView(info[x]);
            taleRowForTableD.addView(textViewB, params);
        }

        return taleRowForTableD;

    }

    TableRow taleRowForTableD(SampleObject sampleObject) {

        TableRow taleRowForTableD = new TableRow(this.context);
        String info[] = {
                sampleObject.CA1,
                sampleObject.CA2,
                sampleObject.CA3,
                sampleObject.CA4,
                sampleObject.CA5,
                sampleObject.CA6,
                sampleObject.CA7,
                sampleObject.CA8,
                sampleObject.CA9,
                sampleObject.CA10,
                sampleObject.EXAM,
                sampleObject.TOTAL,
                sampleObject.CLASS_AVERAGE,
                sampleObject.HIGHEST,
                sampleObject.LOWEST,
                sampleObject.GRADE
        };
        for (int x = 0; x < result.getNoCas(); x++) {
            TableRow.LayoutParams params = new TableRow.LayoutParams(headerCellsWidth[x + 1], LayoutParams.MATCH_PARENT);
            params.setMargins(2, 2, 0, 0);

            TextView textViewB = this.bodyTextView(info[x]);
            taleRowForTableD.addView(textViewB, params);
        }

        return taleRowForTableD;

    }


    // table cell standard TextView
    TextView bodyTextView(String label) {

        TextView bodyTextView = new TextView(this.context);
        bodyTextView.setBackgroundColor(Color.WHITE);
        bodyTextView.setTextColor(getResources().getColor(R.color.grey_800));
        bodyTextView.setText(label);
        bodyTextView.setGravity(Gravity.CENTER);
        bodyTextView.setPadding(5, 5, 5, 5);

        return bodyTextView;
    }

    // table cell custom TextView
    TextView namesTextView(String label) {

        TextView bodyTextView = new TextView(this.context);
        bodyTextView.setBackgroundColor(Color.WHITE);
        bodyTextView.setTextColor(getResources().getColor(R.color.grey_800));
        bodyTextView.setText(label);
        bodyTextView.setGravity(Gravity.LEFT);
        bodyTextView.setPadding(5, 5, 2, 5);

        return bodyTextView;
    }

    // header custom TextView
    TextView headerTextView(String label) {

        TextView headerTextView = new TextView(this.context);
        headerTextView.setBackgroundColor(Color.WHITE);
        headerTextView.setText(label);
        headerTextView.setTextColor(getResources().getColor(R.color.black));
        headerTextView.setGravity(Gravity.CENTER);
        headerTextView.setPadding(5, 5, 5, 5);

        return headerTextView;
    }

    // resizing TableRow height starts here
    void resizeHeaderHeight() {

        TableRow productNameHeaderTableRow = (TableRow) this.tableA.getChildAt(0);
        TableRow productInfoTableRow = (TableRow) this.tableB.getChildAt(0);

        int rowAHeight = this.viewHeight(productNameHeaderTableRow);
        int rowBHeight = this.viewHeight(productInfoTableRow);

        TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
        int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

        this.matchLayoutHeight(tableRow, finalHeight);
    }

    void getTableRowHeaderCellWidth() {

        int tableAChildCount = ((TableRow) this.tableA.getChildAt(0)).getChildCount();
        int tableBChildCount = ((TableRow) this.tableB.getChildAt(0)).getChildCount();

        for (int x = 0; x < (tableAChildCount + tableBChildCount); x++) {

            if (x == 0) {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.tableA.getChildAt(0)).getChildAt(x));
            } else {
                this.headerCellsWidth[x] = this.viewWidth(((TableRow) this.tableB.getChildAt(0)).getChildAt(x - 1));
            }

        }
    }

    // resize body table row height
    void resizeBodyTableRowHeight() {

        int tableC_ChildCount = this.tableC.getChildCount();

        for (int x = 0; x < tableC_ChildCount; x++) {

            TableRow productNameHeaderTableRow = (TableRow) this.tableC.getChildAt(x);
            TableRow productInfoTableRow = (TableRow) this.tableD.getChildAt(x);

            int rowAHeight = this.viewHeight(productNameHeaderTableRow);
            int rowBHeight = this.viewHeight(productInfoTableRow);

            TableRow tableRow = rowAHeight < rowBHeight ? productNameHeaderTableRow : productInfoTableRow;
            int finalHeight = rowAHeight > rowBHeight ? rowAHeight : rowBHeight;

            this.matchLayoutHeight(tableRow, finalHeight);
        }

    }

    // match all height in a table row
    // to make a standard TableRow height
    private void matchLayoutHeight(TableRow tableRow, int height) {

        int tableRowChildCount = tableRow.getChildCount();

        // if a TableRow has only 1 child
        if (tableRow.getChildCount() == 1) {

            View view = tableRow.getChildAt(0);
            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();
            params.height = height - (params.bottomMargin + params.topMargin);

            return;
        }

        // if a TableRow has more than 1 child
        for (int x = 0; x < tableRowChildCount; x++) {

            View view = tableRow.getChildAt(x);

            TableRow.LayoutParams params = (TableRow.LayoutParams) view.getLayoutParams();

            if (!isTheHeighestLayout(tableRow, x)) {
                params.height = height - (params.bottomMargin + params.topMargin);
                return;
            }
        }

    }

    // check if the view has the highest height in a TableRow
    private boolean isTheHeighestLayout(TableRow tableRow, int layoutPosition) {

        int tableRowChildCount = tableRow.getChildCount();
        int heighestViewPosition = -1;
        int viewHeight = 0;

        for (int x = 0; x < tableRowChildCount; x++) {
            View view = tableRow.getChildAt(x);
            int height = this.viewHeight(view);

            if (viewHeight < height) {
                heighestViewPosition = x;
                viewHeight = height;
            }
        }

        return heighestViewPosition == layoutPosition;
    }

    // read a view's height
    private int viewHeight(View view) {
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        return view.getMeasuredHeight();
    }

    // read a view's width
    private int viewWidth(View view) {
        view.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        return view.getMeasuredWidth();
    }

    // horizontal scroll view custom class
    class MyHorizontalScrollView extends HorizontalScrollView {

        public MyHorizontalScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {
            String tag = (String) this.getTag();

            if (tag.equalsIgnoreCase("horizontal scroll view b")) {
                horizontalScrollViewD.scrollTo(l, 0);
            } else {
                horizontalScrollViewB.scrollTo(l, 0);
            }
        }

    }

    // scroll view custom class
    class MyScrollView extends ScrollView {

        public MyScrollView(Context context) {
            super(context);
        }

        @Override
        protected void onScrollChanged(int l, int t, int oldl, int oldt) {

            String tag = (String) this.getTag();

            if (tag.equalsIgnoreCase("scroll view c")) {
                scrollViewD.scrollTo(0, t);
            } else {
                scrollViewC.scrollTo(0, t);
            }
        }
    }


}