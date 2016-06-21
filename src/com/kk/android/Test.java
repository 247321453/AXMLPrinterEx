package com.kk.android;

import java.io.FileNotFoundException;
import java.util.logging.Level;

import org.apache.commons.lang3.StringUtils;

import brut.androlib.res.AndrolibResources;
import brut.androlib.res.data.ResConfigFlags;
import brut.androlib.res.data.ResResSpec;
import brut.androlib.res.data.ResResource;
import brut.androlib.res.data.ResTable;
import brut.androlib.res.data.value.ResStringValue;
import brut.androlib.res.data.value.ResValue;
import brut.androlib.res.util.ExtFile;

public class Test {

	public static void main(String[] args) throws FileNotFoundException, Exception {
		ApkPrinter printer = new ApkPrinter();
		Manifest manifest = printer.readManifest("pc4.apk");
		System.out.println("" + manifest);
		AndrolibResources resources = new AndrolibResources();
		resources.setLogLevel(Level.WARNING);
		ResTable resTable = resources.getResTable(new ExtFile("pc4.apk"), true);
		ResResSpec icon = resTable.getResSpec(manifest.application.getIcon());
		if (icon != null) {
			// icon的路径
			if (StringUtils.equals(icon.getPackage().getName(), manifest.packageName)) {
				System.out.println("find icon:" + icon.getName());
			}else{
				System.out.println("find icon is android default.");
			}
		}
		ResResSpec label = resTable.getResSpec(manifest.application.getLabel());
		if (label != null) {
			ResConfigFlags config = new ResConfigFlags();
			// 默认
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
