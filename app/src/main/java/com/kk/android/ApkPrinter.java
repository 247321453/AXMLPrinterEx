package com.kk.android;

import android.content.res.AXmlResourceParser;
import com.kk.android.pm.Manifest;
import net.kk.xml.XmlOptions;
import net.kk.xml.XmlReader;
import net.kk.xml.adapter.XmlTextAdapter;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ApkPrinter {
    private static String getPackage(int id) {
        if (id >>> 24 == 1) {
            return "android:";
        }
        return "";
    }

    public static float complexToFloat(int complex) {
        return (complex & 0xFF00) * RADIX_MULTS[(complex >> 4 & 0x3)];
    }
    private static final float[] RADIX_MULTS = { 0.0039063F, 3.051758E-5F, 1.192093E-7F, 4.656613E-10F };
    private static final String[] DIMENSION_UNITS = { "px", "dip", "sp", "pt", "in", "mm", "", "" };
    private static final String[] FRACTION_UNITS = { "%", "%p", "", "", "", "", "", "" };

    private static String getAttributeValue(AXmlResourceParser parser, int index) {
        int type = parser.getAttributeValueType(index);
        int data = parser.getAttributeValueData(index);
        if (type == 3) {
            return parser.getAttributeValue(index);
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

    private XmlReader createReader(boolean android) throws XmlPullParserException {
        XmlPullParser parser = android ? new AXmlResourceParserEx() : XmlPullParserFactory.newInstance().newPullParser();
        XmlOptions.Builder builder = new XmlOptions.Builder().dontUseSetMethod().enableSameAsList().useSpace()
                .registerTypeAdapter(Boolean.class, new XmlTextAdapter<Boolean>() {
                    @Override
                    public Boolean toObject(Class<?> tClass, String val, Object parent) throws Exception {
                        return "1".equals(val) || "true".equals("val");
                    }

                    @Override
                    public String toString(Class<Boolean> tClass, Boolean var) {
                        return var == null ? "" : "" + var;
                    }

                });
        if (android) {
            builder.setXmlAttributeReader((pullParser, index) -> getAttributeValue((AXmlResourceParser) parser, index));
        }

        return new XmlReader(parser, builder.build());
    }

    public Manifest readManifest(InputStream inputStream, boolean android) throws Exception {
        XmlReader reader = createReader(android);
        return reader.fromInputStream(Manifest.class, inputStream, null);
    }

    public Manifest readManifest(String file) {
        if (file == null)
            return null;
        Manifest manifest = null;
        if (file.endsWith(".xml")) {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(file);
                manifest = readManifest(inputStream, false);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            ZipFile zipFile = null;
            try {
                zipFile = new ZipFile(file);
                ZipEntry zipEntry = zipFile.getEntry("AndroidManifest.xml");
                if (zipEntry != null) {
                    manifest = readManifest(zipFile.getInputStream(zipEntry), true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (zipFile != null) {
                    try {
                        zipFile.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return manifest;
    }

}
