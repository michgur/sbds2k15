package com.cyhim.sbds2k15;

import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.support.annotation.XmlRes;
import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by Michael on 7/2/2017.
 */
public class SBDOption
{
    private static List<SBDOption> options = new ArrayList<>();
    private String mk;
    private String sb;
    private int value;

    private SBDOption(String mk, String sb, int value) {
        this.mk = mk;
        this.sb = sb;
        this.value = value;
    }

    public static List<SBDOption> getRandomOptions(int n) {
        Collections.shuffle(options);
        return options.subList(0, n);
    }

    public static void setOptions(@XmlRes int resid, Resources resources) {
        XmlResourceParser parser = resources.getXml(resid);

        try {
            int event = parser.getEventType();
            while (event != XmlPullParser.END_DOCUMENT) {
                if ((event == XmlPullParser.START_TAG) && (parser.getName().equalsIgnoreCase("item")))
                    options.add(new SBDOption(
                            parser.getAttributeValue(null, "mk"),
                            parser.getAttributeValue(null, "sb"),
                            parser.getAttributeIntValue(null, "value", 0)));
                event = parser.next();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("An Error Occurred While Parsing Option Library");
        }
    }

    public String getMk() { return mk; }
    public String getSb() { return sb; }
    public int getValue() { return value; }

    public String toString() {
        return "Option{mk='" + this.mk + '\'' + ", sb='" + this.sb + '\'' + ", value=" + this.value + '}';
    }
}
