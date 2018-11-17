package mountedwings.org.mskola_mgt.data;

public class NumberChatStaffList {
    public int color = -1;
    private String recipient;
    public Integer image = null;
    private String email;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}