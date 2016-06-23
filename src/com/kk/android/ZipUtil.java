package com.kk.android;

import java.io.IOException;
import java.util.Enumeration;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtil {

	public static String findEntryByName(String zipFile, String dirPrefix, String name) {
		ZipFile zip = null;
		String path = null;
		try {
			zip = new ZipFile(zipFile);
			Enumeration<? extends ZipEntry> es = zip.entries();
			while (es.hasMoreElements()) {
				ZipEntry e = es.nextElement();
				if (Pattern.matches(dirPrefix + "[-]{0,1}[-a-zA-Z0-9]*/" + name + "\\.[a-zA-Z]+", e.getName())) {
					path = e.getName();
					break;
				}
			}
		} catch (Exception e) {
		} finally {
			if (zip != null) {
				try {
					zip.close();
				} catch (IOException e) {
				}
			}
		}
		return path;
	}

	public static boolean exits(String zipFile, String name) {
		boolean has = false;
		ZipFile zip = null;
		try {
			zip = new ZipFile(zipFile);
			has = zip.getEntry(name) != null;
		} catch (Exception e) {
		} finally {
			if (zip != null) {
				try {
					zip.close();
				} catch (IOException e) {
				}
			}
		}
		return has;
	}
}
