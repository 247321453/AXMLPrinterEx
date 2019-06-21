package com.kk.android;

import java.io.*;
import java.util.*;

public class Test {
    private static FileOutputStream mLogOut;

    public static void main(String[] args) throws Exception {
        final String dir = (args.length > 0) ? args[0] : "C:\\code\\apk";
        final String log = (args.length > 1) ? args[1] : "C:\\code\\apk\\apk_" + System.currentTimeMillis() + ".log";

        File[] files = new File(dir).listFiles((dir1, name) -> name.toLowerCase(Locale.CHINA).endsWith(".apk"));
        ApkPrinter printer = new ApkPrinter();
        if (files == null) {
            System.out.println("not found apk in " + dir);
            return;
        }
        mLogOut = new FileOutputStream(log);
        int count = 0;
        for (File file : files) {
            log("===========================");
            try {
                Manifest manifest = printer.readManifest(file.getAbsolutePath());
                print(manifest);
                if (manifest.getApplication().receivers != null) {
                    count += manifest.getApplication().receivers.size();
                }
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        log("共" + files.length + "应用，共" + count + "静态广播接收者");
        mLogOut.flush();
        mLogOut.close();
    }

    private static void log(String text) throws IOException {
        System.out.println(text);
        mLogOut.write(text.getBytes("utf-8"));
        mLogOut.write('\n');
    }

    private static void print(Manifest manifest) throws IOException {
        String pkg = manifest.packageName;
        List<Manifest.Receiver> receivers = manifest.getApplication().receivers;
        if (receivers != null) {
            log(pkg + " " + manifest.versionName + "(" + manifest.versionCode + ")" + ", 全部广播接收者:" + receivers.size());
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
                List<Manifest.Receiver> list = map.computeIfAbsent(key, k -> new ArrayList<>());
                list.add(receiver);
            }
            List<String> keys = new ArrayList<>(map.keySet());
            keys.sort(String::compareTo);
            for (String process : keys) {
                log("进程名：" + process + "，广播数量:" + map.get(process).size());
            }
        } else {
            log(pkg + ", 未发现广播接收者");
        }
    }
}
