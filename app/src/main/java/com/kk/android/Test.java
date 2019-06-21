package com.kk.android;

import java.io.File;
import java.io.FilenameFilter;
import java.util.*;

public class Test {

    public static void main(String[] args) throws Exception {
        final String dir = "C:\\code\\apk";

        File[] files = new File(dir).listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase(Locale.CHINA).endsWith(".apk");
            }
        });
        ApkPrinter printer = new ApkPrinter();
        if (files == null) {
            System.out.println("not found apk in " + dir);
            return;
        }

        for (File file : files) {
            parseApk(printer, file);
            System.out.println("===========================");
        }
        //签名验证
//        boolean ok = ZipUtil.validateSign(zipFile);
//        if (!ok) {
//            System.err.println("zip is bad Sign");
//            return;
//        }


        // System.out.println("" + manifest);
        // 如果是@开头，则需要获取资源字符串
        //System.out.println("metadata="
        //	+ manifest.getApplication().getMetaData("com.mobile.indiapp.glide.CustomGlideMoudle").getValue());
        // 友盟渠道号UMENG_CHANNEL
        //System.out.println("umeng_channel=" + manifest.getApplication().getMetaData("UMENG_CHANNEL").getValue());
        //
//		AndrolibResources resources = new AndrolibResources();
//		resources.setLogLevel(Level.WARNING);
//		ResTable resTable = resources.getResTable(new ExtFile(zipFile), true);
//		ResResSpec icon = resTable.getResSpec(manifest.getApplication().getIcon());
//		if (icon != null) {
//			// icon的路径
//			if (StringUtils.equals(icon.getPackage().getName(), manifest.packageName)) {
//				String path = ZipUtil.findEntryByName(zipFile, "res/drawable", icon.getName());
//				if (StringUtils.isEmpty(path)) {
//					path = ZipUtil.findEntryByName(zipFile, "res/mipmap", icon.getName());
//				}
//				System.out.println("find icon:" + icon.getFullName(true, false) + ",path:" + path);
//			} else {
//				System.out.println("find icon is android default.");
//			}
//		}
//		ResResSpec label = resTable.getResSpec(manifest.getApplication().getLabel());
//		if (label != null) {
//			ResConfigFlags config = new ResConfigFlags();
//			// app名字默认(values)
//			ResResource res = label.getResource(config);
//			if (res != null) {
//				ResValue resValue = res.getValue();
//				if (resValue instanceof ResStringValue) {
//					System.out.println("find label:" + ((ResStringValue) resValue).getRawValue());
//				}
//			}
//		}
    }

    private static void parseApk(ApkPrinter printer, File zipFile) throws Exception {
//        String xml = "c:\\code\\apk\\AndroidManifest.xml";
        Manifest manifest = printer.readManifest(zipFile.getAbsolutePath());
        //printer.readManifest(new FileInputStream(xml), false);
        String pkg = manifest.packageName;
        //printer.readManifest(new FileInputStream(xml));
//        System.out.println(manifest);
        List<Manifest.Receiver> receivers = manifest.getApplication().receivers;
        if (receivers != null) {
            System.out.println(pkg + " " + manifest.versionName + "(" + manifest.versionCode + ")" + ", 全部广播接收者:" + receivers.size());
            Map<String, List<Manifest.Receiver>> map = new HashMap<>();
            for (Manifest.Receiver receiver : receivers) {
                String key;
                if (receiver.process == null) {
                    key = pkg;
                } else {
                    key = receiver.process;
                }
                if (key.startsWith(":")) {
                    key = pkg + key;
                }
                List<Manifest.Receiver> list = map.get(key);
                if (list == null) {
                    list = new ArrayList<>();
                    map.put(key, list);
                }
                list.add(receiver);
            }
            List<String> keys = new ArrayList<String>(map.keySet());
            Collections.sort(keys, new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareTo(o2);
                }
            });
            for (String process : keys) {
                System.out.println("进程名：" + process + "，广播数量:" + map.get(process).size());
            }
        } else {
            System.out.println(pkg + ", 未发现广播接收者");
        }
    }
}
