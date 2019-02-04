package mountedwings.org.mskola_mgt.data;

import android.graphics.drawable.Drawable;

public class NumberFAQ {

    public int image;
    public Drawable imageDrw;
    public String name;
    public String answer;
    public String question;
    public boolean expanded = false;
    public boolean parent = false;

    // flag when item swiped
    public boolean swiped = false;

    public NumberFAQ() {
    }

    public NumberFAQ(int image, String name, String question, String answer) {
        this.image = image;
        this.name = name;
        this.question = question;
        this.answer = answer;
    }
}
