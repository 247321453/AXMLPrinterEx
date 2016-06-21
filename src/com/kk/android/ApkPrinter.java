package com.kk.android;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import brut.androlib.res.decoder.AXmlResourceParser;
import net.kk.xml.XmlOptions;
import net.kk.xml.XmlReader;
import net.kk.xml.internal.XmlStringAdapter;

public class ApkPrinter {
	private XmlReader createReader() {
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

	public Manifest readManifest(InputStream inputStream) throws Exception {
		XmlReader reader = createReader();
		return reader.from(inputStream, Manifest.class, null);
	}

	public Manifest readManifest(String file) {
		if (file == null)
			return null;
		Manifest manifest = null;
		if (file.endsWith(".xml")) {
			FileInputStream inputStream = null;
			try {
				inputStream = new FileInputStream(file);
				manifest = readManifest(inputStream);
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
					manifest = readManifest(zipFile.getInputStream(zipEntry));
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
