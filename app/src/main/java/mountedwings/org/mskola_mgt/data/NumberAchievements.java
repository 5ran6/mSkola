package mountedwings.org.mskola_mgt.data;

public class NumberAchievements {
    private String achievement;
    private String subTitle;
    private byte[] Image;

    public void setImage(byte[] image) {
        Image = image;
    }

    public byte[] getImageFile() {
        return Image;
    }

    public String getachievement() {
        return achievement;
    }

    public void setachievement(String achievement) {
        this.achievement = achievement;
    }

    public String getsubTitle() {
        return subTitle;
    }

    public void setsubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

}