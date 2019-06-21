package com.kk.android;

import com.kk.android.pm.Manifest;

import java.io.*;
import java.util.*;

public class Test {
    private static FileOutputStream mLogOut;

    public static void main(String[] args) throws Exception {
        final String dir = (args.length > 0) ? args[0] : "./apk";
        final String log = (args.length > 1) ? args[1] : "./apk_" + System.currentTimeMillis() + ".log";

        File[] files = new File(dir).listFiles((dir1, name) -> name.toLowerCase(Locale.CHINA).endsWith(".apk"));
        AXmlPrinter3 printer = new AXmlPrinter3();
        if (files == null) {
            System.out.println("not found apk in " + dir);
            return;
        }
        mLogOut = new FileOutputStream(log);
        int count = 0;
        int pcount = 0;
        int rcount = 0;
        List<String> pkgs = new ArrayList<>();
        for (File file : files) {
            log("===========================");
            try {
                Manifest manifest = printer.readManifest(file.getAbsolutePath());
                int p = getAllProcess(manifest);
                pcount += p;
                int rc = print(manifest);
                rcount += rc;
                log(manifest.packageName + "含有" + p + "进程" + ",静态广播进程数：" + rc);
                pkgs.add(manifest.packageName);
                if (manifest.application.receivers != null) {
                    count += manifest.application.receivers.size();
                }
//                System.out.println(manifest);
//                break;
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        log("===========================");
        log("应用包名：" + pkgs);
        log("===========================");
        log("共" + files.length + "应用，共" + pcount + "进程，共" + rcount + "静态广播进程，共" + count + "静态广播接收者");
        mLogOut.flush();
        mLogOut.close();
    }

    private static void log(String text) throws IOException {
        System.out.println(text);
        mLogOut.write(text.getBytes("utf-8"));
        mLogOut.write('\n');
    }

    private static int print(Manifest manifest) throws IOException {
        String pkg = manifest.packageName;
        List<Manifest.Receiver> receivers = manifest.application.receivers;
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
            return map.keySet().size();
        } else {
            log(pkg + ", 未发现广播接收者");
            return 0;
        }
    }

    private static int getAllProcess(Manifest manifest) {
        String pkg = manifest.packageName;
        List<String> list = new ArrayList<>();
        List<Manifest.ComponentInfo> alls = new ArrayList<>();
        if (manifest.application.activitys != null) {
            alls.addAll(manifest.application.activitys);
        }
        if (manifest.application.services != null) {
            alls.addAll(manifest.application.services);
        }
        if (manifest.application.receivers != null) {
            alls.addAll(manifest.application.receivers);
        }
        if (manifest.application.providers != null) {
            alls.addAll(manifest.application.providers);
        }
        for (Manifest.ComponentInfo info : alls) {
            String key;
            if (info.process == null) {
                key = pkg;
            } else {
                key = info.process;
            }
            if (key.startsWith(":")) {
                key = pkg + key;
            }
            if (!list.contains(key)) {
                list.add(key);
            }
        }
        return list.size();
    }
}
