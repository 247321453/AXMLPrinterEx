package com.kk.android;

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
    private XmlReader createReader(boolean android) throws XmlPullParserException {
        XmlPullParser parser = android ? new AXmlResourceParserEx() : XmlPullParserFactory.newInstance().newPullParser();
        XmlOptions options = new XmlOptions.Builder().dontUseSetMethod().enableSameAsList().useSpace()
                .registerTypeAdapter(Boolean.class, new XmlTextAdapter<Boolean>() {
                    @Override
                    public Boolean toObject(Class<?> tClass, String val, Object parent) throws Exception {
                        return "1".equals(val) || "true".equals("val");
                    }

                    @Override
                    public String toString(Class<Boolean> tClass, Boolean var) {
                        return var == null ? "" : "" + var;
                    }

                }).build();

        return new XmlReader(parser, options);
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
