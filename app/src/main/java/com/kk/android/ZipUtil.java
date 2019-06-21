package com.kk.android;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Enumeration;
import java.util.Map;
import java.util.jar.Attributes;
import java.util.jar.Manifest;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtil {
	public static boolean validateSign(String jarFile) {
		// Pattern pattern = Pattern.compile("Name: ([\\s\\S]+?)SHA1-Digest:
		// ([\\S]+)");
		// 读取为字符串，然后获取全部
		ZipFile zip = null;
		boolean error = false;
		try {
			zip = new ZipFile(jarFile);
			ZipEntry entry = zip.getEntry("META-INF/MANIFEST.MF");
			if (entry != null) {
				java.util.jar.Manifest manifest = new Manifest(zip.getInputStream(entry));
				Map<String, Attributes> entries = manifest.getEntries();
				// for (Entry<String, Attributes> e : entries.entrySet()) {
				// System.out.println(e.getKey());
				// System.out.println(e.getValue().getValue("SHA1-Digest"));
				// }
				Enumeration<? extends ZipEntry> enumerations = zip.entries();
			
				while (enumerations.hasMoreElements()) {
					ZipEntry zipEntry = enumerations.nextElement();
					if (zipEntry.isDirectory()) {
					} else {
						if (zipEntry.getName().startsWith("META-INF/")) {

						} else {
							Attributes attributes = entries.get(zipEntry.getName());
							if (attributes == null) {
								System.err.println(zipEntry.getName() + ":no sha1");
								error = true;
								break;
							}
							String sha1 = attributes.getValue("SHA1-Digest");
							// 计算sha1，如果不对，则是签名不对
							if (sha1 == null || sha1.length() == 0) {
								System.err.println(zipEntry.getName() + ":" + sha1 + "=null");
								error = true;
								break;
							}
							String eSha1 = sha1(zip.getInputStream(zipEntry));
							if (!sha1.equals(eSha1)) {
								System.err.println(zipEntry.getName() + ":" + sha1 + "=" + eSha1);
								error = true;
								break;
							}
						}
					}
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
		return !error;
	}

	public static String sha1(InputStream inputStream) {
		byte[] data = new byte[0];
		try {
			MessageDigest messagedigest = MessageDigest.getInstance("SHA-1");
			byte[] buffer = new byte[1024 * 10];
			int len = 0;
			while ((len = inputStream.read(buffer)) > 0) {
				// 该对象通过使用 update（）方法处理数据
				messagedigest.update(buffer, 0, len);
			}
			data = messagedigest.digest();
		} catch (Exception e) {

		}
		return Base64.getEncoder().encodeToString(data);
	}

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
