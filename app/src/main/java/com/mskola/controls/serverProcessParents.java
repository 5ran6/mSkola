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

package com.mskola.controls;

import com.mskola.files.storageFile;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class serverProcessParents {

    public storageFile requestProcess(storageFile data) {
        try {
            //to connect and send the values to the server
            Socket client;
            int port;
            String host;

            serverInfoParents serverInfo = new serverInfoParents();
            host = serverInfo.getIp();
            port = Integer.parseInt(serverInfo.getPort());

            client = new Socket(host, port);

            ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
            oos.writeObject(data);

            ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
            storageFile sentData = null;

            try {
                sentData = (storageFile) (ois.readObject());

            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }

            return sentData;

        } catch (Exception ex) {
            return null;
        }
    }
}