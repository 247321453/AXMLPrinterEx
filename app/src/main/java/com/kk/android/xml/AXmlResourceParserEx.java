package com.kk.android.xml;

import android.content.res.AXmlResourceParser;

import java.io.InputStream;

public class AXmlResourceParserEx extends AXmlResourceParser {
    private static String getPackage(int id) {
        if (id >>> 24 == 1) {
            return "android:";
        }
        return "";
    }

    private static float complexToFloat(int complex) {
        return (complex & 0xFF00) * RADIX_MULTS[(complex >> 4 & 0x3)];
    }
    private static final float[] RADIX_MULTS = { 0.0039063F, 3.051758E-5F, 1.192093E-7F, 4.656613E-10F };
    private static final String[] DIMENSION_UNITS = { "px", "dip", "sp", "pt", "in", "mm", "", "" };
    private static final String[] FRACTION_UNITS = { "%", "%p", "", "", "", "", "", "" };

    @Override
    public void setInput(InputStream stream, String inputEncoding) {
        open(stream);
    }

    @Override
    public String getAttributeValue(int index) {
        return getAttributeValueInner( index);
    }


    private String getAttributeValueInner(int index) {
        int type = getAttributeValueType(index);
        int data = getAttributeValueData(index);
        if (type == 3) {
            return super.getAttributeValue(index);
        }
        if (type == 2) {
            return String.format("?%s%08X", getPackage(data), data);
        }
        if (type == 1) {
            return String.format("@%s%08X", getPackage(data), data);
        }
        if (type == 4) {
            return String.valueOf(Float.intBitsToFloat(data));
        }
        if (type == 17) {
            return String.format("0x%08X", data);
        }
        if (type == 18) {
            return data != 0 ? "true" : "false";
        }
        if (type == 5) {
            return Float.toString(complexToFloat(data)) + DIMENSION_UNITS[(data & 0xF)];
        }
        if (type == 6) {
            return Float.toString(complexToFloat(data)) + FRACTION_UNITS[(data & 0xF)];
        }
        if ((type >= 28) && (type <= 31)) {
            return String.format("#%08X", data);
        }
        if ((type >= 16) && (type <= 31)) {
            return String.valueOf(data);
        }
        return String.format("<0x%X, type 0x%02X>", data, type);
    }

}
