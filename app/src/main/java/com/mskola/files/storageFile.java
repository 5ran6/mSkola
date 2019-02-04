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

package com.mskola.files;

import java.io.Serializable;
import java.util.ArrayList;

public class storageFile implements Serializable {
    String strData = null;
    private ArrayList<byte[]> allImages = new ArrayList<byte[]>();
    private String operation;

    public void setStrData(String data) {
        strData = data;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }


    public void addImageFile(byte[] imageFile) {
        this.allImages.add(imageFile);
    }


    public String getStrData() {
        return strData;
    }

    public String getOperation() {
        return operation;
    }


    public ArrayList<byte[]> getImageFiles() {
        return allImages;
    }
}
