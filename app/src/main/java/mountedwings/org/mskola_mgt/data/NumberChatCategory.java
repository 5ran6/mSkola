package mountedwings.org.mskola_mgt.data;

public class NumberChatCategory {
    private String names;
    private String email;
    private boolean isSelected;
    private boolean isAllSelectedA = false;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public boolean isAllSelectedA() {
        return isAllSelectedA;
    }

    public void setAllSelectedA(boolean allSelectedA) {
        isAllSelectedA = allSelectedA;
    }

    public String getnames() {
        return names;
    }

    public void setnames(String names) {
        this.names = names;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}