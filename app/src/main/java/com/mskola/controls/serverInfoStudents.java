package com.mskola.controls;

public class serverInfoStudents {
    private String ip = "192.168.43.75";
    //   public String ip = "169.254.167.77";
    //   public String ip = "10.1.1.7";
    private String port = "9098";

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}