package com.kk.android;

import android.content.res.AXmlResourceParser;
import net.kk.xml.XmlOptions;
import net.kk.xml.XmlReader;
import net.kk.xml.internal.XmlStringAdapter;

public class MainPrinter {
	public static XmlReader createReader() {
		AXmlResourceParser parser = new AXmlResourceParser();
		XmlOptions options = new XmlOptions.Builder().dontUseSetMethod().enableSameAsList().useSpace()
				.registerTypeAdapter(Boolean.class, new XmlStringAdapter<Boolean>() {

					@Override
					public Boolean toObject(Class<?> tClass, String val) throws Exception {
						return "1".equals(val) || "true".equals("val");
					}

					@Override
					public String toString(Class<Boolean> tClass, Boolean var) {
						return var == null ? "" : "" + var;
					}

				}).build();

		return new XmlReader(parser, options);
	}
}
