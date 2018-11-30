package com.mskola.controls;

import com.mskola.files.storageFile;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class serverProcess {

    public storageFile requestProcess(storageFile data) {
        try {
            //to connect and send the values to the server
            Socket client;
            int port;
            String host;

            serverInfo serverInfo = new serverInfo();
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