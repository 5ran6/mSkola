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