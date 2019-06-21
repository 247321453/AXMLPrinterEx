package com.kk.android;

import java.util.List;

import net.kk.xml.annotations.XmlAttribute;
import net.kk.xml.annotations.XmlElement;

/***
 * 然后再去解析values的id 再根据id获取字符串和图片
 *
 * @author keyongyu
 *
 */
@XmlElement("manifest")
public class Manifest {
    public static final String NAMESPACE = "http://schemas.android.com/apk/res/android";
    @XmlAttribute("package")
    public String packageName;
    @XmlAttribute(value = "versionCode", namespace = NAMESPACE)
    public int versionCode;

    @XmlAttribute(value = "versionName", namespace = NAMESPACE)
    public String versionName;

    @XmlElement("uses-sdk")
    public UsesSdk usesSdk;
    @XmlElement(value = "permission")
    public List<Permission> permissions;

    @XmlElement(value = "uses-permission")
    public List<UsePermission> usePermissions;

    @XmlElement("application")
    public Application application;

    @Override
    public String toString() {
        return "Manifest [\n packageName=" + packageName + ", versionCode=" + versionCode + ", versionName="
                + versionName + ",\n usesSdk=" + usesSdk + "\n    permissions=\n\t" + permissions
                + "\n    usePermissions=\n\t" + usePermissions + "\n application=" + application + "\n]";
    }

    public static class UsesSdk {
        @XmlAttribute(value = "minSdkVersion", namespace = NAMESPACE)
        public int minSdkVersion;
        @XmlAttribute(value = "maxSdkVersion", namespace = NAMESPACE)
        public int maxSdkVersion;
        @XmlAttribute(value = "targetSdkVersion", namespace = NAMESPACE)
        public int targetSdkVersion;

        @Override
        public String toString() {
            return "[minSdkVersion=" + minSdkVersion + ", maxSdkVersion=" + maxSdkVersion + ", targetSdkVersion="
                    + targetSdkVersion + "]";
        }

    }

    private static class NameBase {
        @XmlAttribute(value = "name", namespace = NAMESPACE)
        public String name;

        @Override
        public String toString() {
            return "\n\t" + name;
        }
    }

    public static class UsePermission extends NameBase {
    }

    public static class Permission extends NameBase {
        @XmlAttribute(value = "protectionLevel", namespace = NAMESPACE)
        public String protectionLevel;

        @Override
        public String toString() {
            return "name=" + name + ",protectionLevel=" + protectionLevel;
        }
    }

    public static class MetaData {
        @XmlAttribute(value = "name", namespace = NAMESPACE)
        public String name;
        @XmlAttribute(value = "value", namespace = NAMESPACE)
        public String value;

        public MetaData() {

        }

        public MetaData(String name, String value) {
            super();
            this.name = name;
            this.value = value;
        }

        @Override
        public String toString() {
            return "\n\t\t" + name + "=" + value;
        }

        public String getValue() {
            return value;
        }
    }

    public static class AndroidBase {
        @XmlAttribute(value = "theme", namespace = NAMESPACE)
        public String theme;
        @XmlAttribute(value = "label", namespace = NAMESPACE)
        public String label;
        @XmlAttribute(value = "icon", namespace = NAMESPACE)
        public String icon;
        @XmlAttribute(value = "name", namespace = NAMESPACE)
        public String name;

        @XmlElement(value = "meta-data")
        public List<MetaData> metadatas;

        public int getTheme() {
            return getInteger(theme);
        }

        public int getIcon() {
            return getInteger(icon);
        }

        public int getLabel() {
            return getInteger(label);
        }

        private int getInteger(String str) {
            if (str == null || str.trim().length() == 0) {
                return 0;
            }
            return Integer.parseInt(str.replace("@", ""));
        }

        public MetaData getMetaData(String name) {
            if (metadatas != null) {
                for (MetaData data : metadatas) {
                    if (name.equals(data.name)) {
                        return data;
                    }
                }
            }
            return new MetaData(name, null);
        }

        public List<MetaData> getMetaDatas() {
            return metadatas;
        }
    }

    public static class Application extends AndroidBase {
        @XmlAttribute(value = "hardwareAccelerated", namespace = NAMESPACE)
        public boolean hardwareAccelerated;
        @XmlAttribute(value = "allowBackup", namespace = NAMESPACE)
        public boolean allowBackup;

        @XmlElement(value = "activity")
        public List<Activity> activitys;
        @XmlElement(value = "provider")
        public List<Provider> providers;
        @XmlElement(value = "receiver")
        public List<Receiver> receivers;
        @XmlElement(value = "service")
        public List<Service> services;

        @Override
        public String toString() {
            return "[name=" + name + ",\n\tallowBackup=" + allowBackup + ",hardwareAccelerated=" + hardwareAccelerated
                    + ",\n\t" + "theme=" + theme + ",label=" + label + ",icon=" + icon + ",\n\tmetadata=" + metadatas
                    + "\n\tactivitys=" + activitys + "\n\tproviders=" + providers + "\n\treceivers=" + receivers
                    + "\n\tservices=" + services + "\n\t]";
        }
    }

    public static class IntentFilter {
        @XmlElement("action")
        public List<NameBase> actions;
        @XmlElement("category")
        public List<NameBase> categorys;
        @XmlAttribute(value = "priority", namespace = NAMESPACE)
        public long priority;
        @XmlElement("data")
        public List<IntentData> datas;

        private String getDatas() {
            if (datas == null) {
                return null;
            }
            int count = datas.size();
            int i = 0;
            StringBuffer stringBuffer = new StringBuffer("[\n\t\t\t");
            for (IntentData data : datas) {
                i++;
                stringBuffer.append(data);
                if (i != count) {
                    stringBuffer.append(",\n\t\t\t");
                }
            }
            stringBuffer.append("]");
            return stringBuffer.toString();
        }

        @Override
        public String toString() {
            return "\n\t\tIntentFilter [priority=" + priority + ",\n\t\tactions=" + actions + ",\n\t\tcategorys="
                    + categorys + ",\n\t\tdatas=" + getDatas() + "\n\t\t]";
        }

    }

    public static class IntentData {
        @XmlAttribute(value = "scheme", namespace = NAMESPACE)
        public String scheme;
        @XmlAttribute(value = "host", namespace = NAMESPACE)
        public String host;
        @XmlAttribute(value = "path", namespace = NAMESPACE)
        public String path;

        @Override
        public String toString() {
            return "data[scheme=" + scheme + ",host=" + host + ",path=" + path + "]";
        }
    }

    public static class ContextBase extends AndroidBase {
        @XmlAttribute(value = "exported", namespace = NAMESPACE)
        public boolean exported = false;
        @XmlAttribute(value = "enabled", namespace = NAMESPACE)
        public boolean enabled = true;
        @XmlAttribute(value = "permission", namespace = NAMESPACE)
        public String permission;
        @XmlAttribute(value = "process", namespace = NAMESPACE)
        public String process;
        @XmlElement(value = "intent-filter")
        public List<IntentFilter> intentFilters;
    }

    public static class Service extends ContextBase {
        @Override
        public String toString() {
            return "\n\tService[name=" + name + ",enabled=" + enabled + ",exported=" + exported + ",\n\tpermission="
                    + permission + ",\n\t" + "process=" + process + ", theme=" + theme + ",label=" + label + ",icon=" + icon + ",\n\tmetadata="
                    + metadatas + "\n\t]";
        }
    }

    public static class Receiver extends ContextBase {
        @Override
        public String toString() {
            return "\n\tReceiver[name=" + name + ",enabled=" + enabled + ",exported=" + exported + ",\n\tpermission="
                    + permission + ",\n\t" + "process=" + process + ", theme=" + theme + ",label=" + label + ",icon=" + icon + ",\n\tmetadata="
                    + metadatas + "\n\t]";
        }
    }

    public static class Provider extends ContextBase {
        @Override
        public String toString() {
            return "\n\tProvider[name=" + name + ",enabled=" + enabled + ",exported=" + exported + ",\n\tpermission="
                    + permission + ",\n\t" + "process=" + process + ", theme=" + theme + ",label=" + label + ",icon=" + icon + ",\n\tmetadata="
                    + metadatas + "\n\t]";
        }
    }

    public static class Activity extends ContextBase {
        @XmlAttribute(value = "launchMode", namespace = NAMESPACE)
        public String launchMode;
        @XmlAttribute(value = "screenOrientation", namespace = NAMESPACE)
        public String screenOrientation;
        @XmlAttribute(value = "configChanges", namespace = NAMESPACE)
        public String configChanges;

        @Override
        public String toString() {
            return "\n\tActivity[name=" + name + ",enabled=" + enabled + ",exported=" + exported + ",\n\tpermission="
                    + ",launchMode=" + launchMode + ",permission=" + permission + ",enabled=" + enabled + ",exported="
                    + exported + ",configChanges=" + configChanges + ",screenOrientation=" + screenOrientation + ",\n\t"
                    + "process=" + process + ", theme=" + theme + ",label=" + label + ",icon=" + icon + ",\n\tmetadata=" + metadatas
                    + ",\n\tintentFilters=" + intentFilters + "\n\t]";
        }

    }
}
