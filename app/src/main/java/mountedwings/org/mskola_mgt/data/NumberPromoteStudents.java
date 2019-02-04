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

public class NumberPromoteStudents {
    public int color = -1;
    public Integer image = null;

    private String name;
    private String session;
    private String regNo;
    private String class_arm;
    private Boolean isSelected = false;


    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }


    public String getclass_arm() {
        return class_arm;
    }

    public void setclass_arm(String class_arm) {
        this.class_arm = class_arm;
    }


    public String getsession() {
        return session;
    }

    public void setsession(String session) {
        this.session = session;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getregNo() {
        return regNo;
    }

    public void setregNo(String regNo) {
        this.regNo = regNo;
    }

}