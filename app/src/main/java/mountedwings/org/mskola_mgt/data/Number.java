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

package mountedwings.org.mskola_mgt.data;

public class Number {
    private String ONEs;
    private String textONEs;
    private boolean isSelected;
    private boolean isSelected1;
    private boolean isAllSelectedM = false;
    private boolean isAllSelectedA = false;

    public boolean isAllSelectedM() {
        return isAllSelectedM;
    }

    public void setAllSelectedM(boolean allSelectedM) {
        isAllSelectedM = allSelectedM;
    }

    public boolean isAllSelectedA() {
        return isAllSelectedA;
    }

    public void setAllSelectedA(boolean allSelectedA) {
        isAllSelectedA = allSelectedA;
    }

    public String getTextONEs() {
        return textONEs;
    }

    public void setTextONEs(String textONEs) {
        this.textONEs = textONEs;
    }

    public String getONEs() {
        return ONEs;
    }

    public void setONEs(String ONEs) {
        this.ONEs = ONEs;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected1() {
        return isSelected1;
    }

    public void setSelected1(boolean selected1) {
        isSelected1 = selected1;
    }
}