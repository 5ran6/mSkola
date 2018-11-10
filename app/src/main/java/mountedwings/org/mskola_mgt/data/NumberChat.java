package mountedwings.org.mskola_mgt.data;

public class NumberChat {
    private String date;
    private String recipient;
    private String msg;
    private byte[] Image;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public void setImage(byte[] image) {
        Image = image;
    }

    public byte[] getImageFile() {
        return Image;
    }

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
    }

    public String getmsg() {
        return msg;
    }

    public void setmsg(String msg) {
        this.msg = msg;
    }

}