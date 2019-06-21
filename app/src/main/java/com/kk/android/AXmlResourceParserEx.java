package com.kk.android;

import android.content.res.AXmlResourceParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.InputStream;

public class AXmlResourceParserEx extends AXmlResourceParser {

    @Override
    public void setInput(InputStream stream, String inputEncoding) throws XmlPullParserException {
        open(stream);
    }
}
