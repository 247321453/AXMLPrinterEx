package com.kk.android.pm;

import com.kk.android.util.TextUtils;
import net.kk.xml.annotations.XmlAttribute;
import net.kk.xml.annotations.XmlElement;

import java.util.ArrayList;
import java.util.List;

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

    @XmlAttribute(value = "sharedUserId", namespace = NAMESPACE)
    public String sharedUserId;

    @XmlAttribute(value = "versionCode", namespace = NAMESPACE)
    public int versionCode;

    @XmlAttribute(value = "versionName", namespace = NAMESPACE)
    public String versionName;

    @XmlElement("uses-sdk")
    public AndroidSdk androidSdk;

    @XmlElement(value = "permission")
    public List<PermissionInfo> permissions;

    @XmlElement(value = "uses-permission")
    public List<UserPermission> usePermissions;

    @XmlElement("application")
    public Application application;

    @Override
    public String toString() {
        return "Manifest [\n packageName=" + packageName + ", sharedUserId=" + sharedUserId + ", versionCode=" + versionCode + ", versionName="
                + versionName + ",\n androidSdk=" + androidSdk + "\n    permissions=\n\t" + permissions
                + "\n    usePermissions=\n\t" + usePermissions + "\n application=" + application + "\n]";
    }

    public static class AndroidSdk {
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

    public static class Application extends PackageItemInfo {
        @XmlAttribute(value = "hardwareAccelerated", namespace = Manifest.NAMESPACE)
        public boolean hardwareAccelerated = false;
        @XmlAttribute(value = "allowBackup", namespace = Manifest.NAMESPACE)
        public boolean allowBackup = true;

        @XmlElement(value = "activity")
        public List<Manifest.Activity> activitys;
        @XmlElement(value = "provider")
        public List<Manifest.Provider> providers;
        @XmlElement(value = "receiver")
        public List<Manifest.Receiver> receivers;
        @XmlElement(value = "service")
        public List<Manifest.Service> services;

        @Override
        public String toString() {
            String str = "\n\tApplication[" + super.toString();
            str += "\n\thardwareAccelerated=" + hardwareAccelerated + ", ";
            str += "\n\tallowBackup=" + allowBackup + ", ";
            str += "\n\tactivitys=" + activitys + ", ";
            str += "\n\tproviders=" + providers + ", ";
            str += "\n\treceivers=" + receivers + ", ";
            str += "\n\tservices=" + services + ", ";
            return str + "\n]";
        }
    }

    public static class Service extends IntentInfo {
        @Override
        public String toString() {
            return "\n\tService[" + super.toString() + "]";
        }
    }

    public static class Receiver extends IntentInfo {
        @Override
        public String toString() {
            return "\n\tReceiver[" + super.toString() + "]";
        }
    }

    public static class Provider extends IntentInfo {
        @Override
        public String toString() {
            return "\n\tProvider[" + super.toString() + "]";
        }
    }

    public static class Activity extends IntentInfo {
        @XmlAttribute(value = "launchMode", namespace = NAMESPACE)
        public String launchMode;
        @XmlAttribute(value = "screenOrientation", namespace = NAMESPACE)
        public String screenOrientation;
        @XmlAttribute(value = "configChanges", namespace = NAMESPACE)
        public String configChanges;

        @Override
        public String toString() {
            String str = "\n\tActivity[" + super.toString();
            str += ",\n\t";
            if (launchMode != null) {
                str += "launchMode=" + launchMode + ", \n\t";
            }
            if (screenOrientation != null) {
                str += "screenOrientation=" + screenOrientation + ", \n\t";
            }
            if (configChanges != null) {
                str += "configChanges=" + configChanges + ", \n\t";
            }
            return str + "]";
        }
    }

    public static class NameInfo {
        @XmlAttribute(value = "name", namespace = Manifest.NAMESPACE)
        public String name;

        @Override
        public String toString() {
            return name;
        }
    }

    public static class UserPermission extends NameInfo {
        @Override
        public String toString() {
            return "\n\t\t" + name;
        }
    }

    public static class MetaData {
        @XmlAttribute(value = "name", namespace = Manifest.NAMESPACE)
        public String name;
        @XmlAttribute(value = "value", namespace = Manifest.NAMESPACE)
        public String value;

        @Override
        public String toString() {
            return "\n\t\t" + name + "=" + value;
        }
    }

    public static class PackageItemInfo extends NameInfo {
        /**
         * A string resource identifier (in the package's resources) of this
         * component's label.  From the "label" attribute or, if not set, 0.
         */
        @XmlAttribute(value = "label", namespace = Manifest.NAMESPACE)
        public String label;

        @XmlAttribute(value = "theme", namespace = Manifest.NAMESPACE)
        public String theme;
        /**
         * A drawable resource identifier (in the package's resources) of this
         * component's icon.  From the "icon" attribute or, if not set, 0.
         */
        @XmlAttribute(value = "icon", namespace = Manifest.NAMESPACE)
        public String icon;

        /**
         * Additional meta-data associated with this component.  This field
         * will only be filled in if you set the
         */
        @XmlElement("meta-data")
        public List<MetaData> metaData;

        @Override
        public String toString() {
            String str = "";
            if (!TextUtils.isEmpty(name)) {
                str += "name=" + name + ", ";
            }
            if (!TextUtils.isEmpty(theme)) {
                str += "theme=" + theme + ", ";
            }
            if (!TextUtils.isEmpty(label)) {
                str += "label=" + label + ", ";
            }
            if (!TextUtils.isEmpty(icon)) {
                str += "icon=" + icon + ", ";
            }
            if (metaData != null) {
                str += "\n\tmetaData=" + metaData + "";
            }
            return str;
        }

    }

    public static class ComponentInfo extends PackageItemInfo {
        @XmlAttribute(value = "process", namespace = Manifest.NAMESPACE)
        public String process;

        /**
         * Indicates whether or not this component may be instantiated.  Note that this value can be
         */
        @XmlAttribute(value = "enabled", namespace = Manifest.NAMESPACE)
        public boolean enabled = true;

        /**
         * Set to true if this component is available for use by other applications.
         * &lt;activity&gt;, &lt;receiver&gt;, &lt;service&gt;, or
         * &lt;provider&gt; tag.
         */
        @XmlAttribute(value = "exported", namespace = Manifest.NAMESPACE)
        public boolean exported = false;

        @Override
        public String toString() {
            String str = super.toString();
            if (!str.endsWith(", ") && !str.endsWith(",")) {
                str += ",";
            }
            str += "enabled=" + enabled + ", exported=" + exported;
            if (process != null) {
                str += ",\n\tprocess=" + process;
            }
            return str;
        }

    }

    public static class IntentInfo extends ComponentInfo {
        @XmlAttribute(value = "permission", namespace = Manifest.NAMESPACE)
        public String permission;

        @XmlElement(value = "intent-filter")
        public List<IntentFilter> intentFilters;

        @Override
        public String toString() {
            String str = super.toString();
            if (!TextUtils.isEmpty(permission)) {
                str += ",\n\tpermission=" + permission;
            }
            if (intentFilters != null) {
                str += ",\n\tintentFilters=" + intentFilters;
            }
            return str;
        }
    }

    public static class IntentData {
        @XmlAttribute(value = "scheme", namespace = Manifest.NAMESPACE)
        public String scheme;
        @XmlAttribute(value = "host", namespace = Manifest.NAMESPACE)
        public String host;
        @XmlAttribute(value = "path", namespace = Manifest.NAMESPACE)
        public String path;

        @XmlAttribute(value = "mimeType", namespace = Manifest.NAMESPACE)
        public String mimeType;

        @Override
        public String toString() {
            String str = "data[";
            if (!TextUtils.isEmpty(scheme)) {
                str += "scheme=" + scheme + ",";
            }
            if (!TextUtils.isEmpty(host)) {
                str += "host=" + host + ",";
            }
            if (!TextUtils.isEmpty(path)) {
                str += "path=" + path + ",";
            }
            if (!TextUtils.isEmpty(mimeType)) {
                str += "mimeType=" + mimeType + ",";
            }
            return str;
        }
    }

    public static class IntentFilter {
        @XmlElement("action")
        public List<NameInfo> actions;
        @XmlElement("category")
        public List<NameInfo> categorys;
        @XmlAttribute(value = "priority", namespace = Manifest.NAMESPACE)
        public long priority;
        @XmlElement("data")
        public List<IntentData> datas;

        @Override
        public String toString() {

            String str = "\n\t\tIntentFilter [\n\t\t\tpriority=" + priority + ",\n\t\t\tactions=" + actions;
            if (categorys != null) {
                str += ",\n\t\t\tcategorys=" + categorys;
            }
            if (datas != null) {
                str += ",\n\t\t\tdatas=" + datas;
            }
            str += "\n\t\t]";
            return str;
        }

    }

    public static class PermissionInfo extends NameInfo {
        @XmlAttribute(value = "protectionLevel", namespace = Manifest.NAMESPACE)
        public String protectionLevel;

        @Override
        public String toString() {
            return "name=" + name + ",protectionLevel=" + protectionLevel;
        }
    }

}
