package com.kk.android;

import org.apache.commons.lang3.StringUtils;

import java.util.logging.Level;

import brut.androlib.res.AndrolibResources;
import brut.androlib.res.data.ResConfigFlags;
import brut.androlib.res.data.ResResSpec;
import brut.androlib.res.data.ResResource;
import brut.androlib.res.data.ResTable;
import brut.androlib.res.data.value.ResStringValue;
import brut.androlib.res.data.value.ResValue;
import brut.androlib.res.util.ExtFile;

public class Test {

	public static void main(String[] args) throws  Exception {
		final String zipFile = "pc4.apk";
		//签名验证
		boolean ok = ZipUtil.validateSign(zipFile);
		if(!ok){
			System.err.println("zip is bad Sign");
			return;
		}
		System.out.println("zip is validate Sign=" + ok);
		ApkPrinter printer = new ApkPrinter();
		Manifest manifest = printer.readManifest(zipFile);
		// System.out.println("" + manifest);
		// 如果是@开头，则需要获取资源字符串
		System.out.println("metadata="
				+ manifest.getApplication().getMetaData("com.mobile.indiapp.glide.CustomGlideMoudle").getValue());
		// 友盟渠道号UMENG_CHANNEL
		System.out.println("umeng_channel=" + manifest.getApplication().getMetaData("UMENG_CHANNEL").getValue());
		//
		AndrolibResources resources = new AndrolibResources();
		resources.setLogLevel(Level.WARNING);
		ResTable resTable = resources.getResTable(new ExtFile(zipFile), true);
		ResResSpec icon = resTable.getResSpec(manifest.getApplication().getIcon());
		if (icon != null) {
			// icon的路径
			if (StringUtils.equals(icon.getPackage().getName(), manifest.packageName)) {
				String path = ZipUtil.findEntryByName(zipFile, "res/drawable", icon.getName());
				if (StringUtils.isEmpty(path)) {
					path = ZipUtil.findEntryByName(zipFile, "res/mipmap", icon.getName());
				}
				System.out.println("find icon:" + icon.getFullName(true, false) + ",path:" + path);
			} else {
				System.out.println("find icon is android default.");
			}
		}
		ResResSpec label = resTable.getResSpec(manifest.getApplication().getLabel());
		if (label != null) {
			ResConfigFlags config = new ResConfigFlags();
			// app名字默认(values)
			ResResource res = label.getResource(config);
			if (res != null) {
				ResValue resValue = res.getValue();
				if (resValue instanceof ResStringValue) {
					System.out.println("find label:" + ((ResStringValue) resValue).getRawValue());
				}
			}

		}
	}
}
