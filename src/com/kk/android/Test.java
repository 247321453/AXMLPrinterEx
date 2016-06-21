package com.kk.android;

import java.io.FileNotFoundException;

import brut.androlib.res.AndrolibResources;
import brut.androlib.res.data.ResConfigFlags;
import brut.androlib.res.data.ResResSpec;
import brut.androlib.res.data.ResTable;
import brut.androlib.res.util.ExtFile;

public class Test {

	public static void main(String[] args) throws FileNotFoundException, Exception {
		ApkPrinter printer = new ApkPrinter();
		Manifest manifest = printer.readManifest("pc4.apk");
		System.out.println("" + manifest);
		AndrolibResources resources = new AndrolibResources();
		ResTable resTable = resources.getResTable(new ExtFile("pc4.apk"), true);
		ResResSpec icon = resTable.getResSpec(manifest.application.getIcon());
		if (icon != null) {
			//icon的路径
			System.out.println("find icon:" + icon.getName());
		}
		ResResSpec label = resTable.getResSpec(manifest.application.getLabel());
		if (label != null) {
			ResConfigFlags config = new ResConfigFlags();
			//默认
			System.out.println("find label:" + label.getResource(config).getValue());
		}
	}
}
