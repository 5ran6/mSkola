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

public class NumberViewResult {
    private String no_subjects;
    private String name;
    private String average;
    private String position;
    private byte[] Image;

    public void setImage(byte[] image) {
        Image = image;
    }

    private String total;

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }


    public byte[] getImageFile() {
        return Image;
    }


    public String getno_subjects() {
        return no_subjects;
    }

    public void setno_subjects(String no_subjects) {
        this.no_subjects = no_subjects;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getaverage() {
        return average;
    }

    public void setaverage(String average) {
        this.average = average;
    }

    public String gettotal() {
        return total;
    }

    public void settotal(String total) {
        this.total = total;
    }
}