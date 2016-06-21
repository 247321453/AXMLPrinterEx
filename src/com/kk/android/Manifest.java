package com.kk.android;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.kk.xml.annotations.XmlAttribute;
import net.kk.xml.annotations.XmlElement;
import net.kk.xml.annotations.XmlElementList;

/***
 * 然后再去解析values的id 再根据id获取字符串和图片
 * 
 * @author keyongyu
 *
 */
@XmlElement("manifest")
public class Manifest {

	@XmlAttribute("package")
	public String packageName;
	@XmlAttribute(value = "versionCode", namespace = "http://schemas.android.com/apk/res/android")
	public int versionCode;

	@XmlAttribute(value = "versionName", namespace = "http://schemas.android.com/apk/res/android")
	public String versionName;

	@XmlElement("uses-sdk")
	public UsesSdk usesSdk;
	@XmlElementList(value = "permission", type = Permission.class)
	public List<Permission> permissions;

	@XmlElementList(value = "uses-permission", type = UsePermission.class)
	public List<UsePermission> usePermissions;

	@XmlElement("application")
	private Application application;

	public Application getApplication(){
		if(application == null){
			application=new Application();
		}
		return application;
	}
	@Override
	public String toString() {
		return "Manifest [\n packageName=" + packageName + ", versionCode=" + versionCode + ", versionName="
				+ versionName + ",\n usesSdk=" + usesSdk + "\n    permissions=\n\t" + permissions
				+ "\n    usePermissions=\n\t" + usePermissions + "\n application=" + application + "\n]";
	}

	public static class UsesSdk {
		@XmlAttribute(value = "minSdkVersion", namespace = "http://schemas.android.com/apk/res/android")
		public int minSdkVersion;
		@XmlAttribute(value = "maxSdkVersion", namespace = "http://schemas.android.com/apk/res/android")
		public int maxSdkVersion;
		@XmlAttribute(value = "targetSdkVersion", namespace = "http://schemas.android.com/apk/res/android")
		public int targetSdkVersion;

		@Override
		public String toString() {
			return "[minSdkVersion=" + minSdkVersion + ", maxSdkVersion=" + maxSdkVersion + ", targetSdkVersion="
					+ targetSdkVersion + "]";
		}

	}

	private static class NameBase {
		@XmlAttribute(value = "name", namespace = "http://schemas.android.com/apk/res/android")
		public String name;

		@Override
		public String toString() {
			return "\n\t" + name;
		}
	}

	public static class UsePermission extends NameBase {
	}

	public static class Permission extends NameBase {
		@XmlAttribute(value = "protectionLevel", namespace = "http://schemas.android.com/apk/res/android")
		public long protectionLevel;

		@Override
		public String toString() {
			return "name=" + name + ",protectionLevel=" + protectionLevel;
		}
	}

	public static class MetaData {
		@XmlAttribute(value = "name", namespace = "http://schemas.android.com/apk/res/android")
		public String name;
		@XmlAttribute(value = "value", namespace = "http://schemas.android.com/apk/res/android")
		public String value;

		@Override
		public String toString() {
			return "\n\t\t" + name + "=" + value;
		}

		public String getValue(){
			return value;
		}
	}

	public static class AndroidBase {
		@XmlAttribute(value = "theme", namespace = "http://schemas.android.com/apk/res/android")
		public String theme;
		@XmlAttribute(value = "label", namespace = "http://schemas.android.com/apk/res/android")
		public String label;
		@XmlAttribute(value = "icon", namespace = "http://schemas.android.com/apk/res/android")
		public String icon;
		@XmlAttribute(value = "name", namespace = "http://schemas.android.com/apk/res/android")
		public String name;

		@XmlElementList(value = "meta-data", type = MetaData.class)
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
		public MetaData getMetaData(String name){
			if(metadatas!=null){
				for(MetaData data:metadatas){
					if(StringUtils.equals(data.name, name)){
						return data;
					}
				}
			}
			return null;
		}
		public List<MetaData> getMetaDatas(){
			return metadatas;
		}
	}

	public static class Application extends AndroidBase {
		@XmlAttribute(value = "hardwareAccelerated", namespace = "http://schemas.android.com/apk/res/android")
		public boolean hardwareAccelerated;
		@XmlAttribute(value = "allowBackup", namespace = "http://schemas.android.com/apk/res/android")
		public boolean allowBackup;

		@XmlElementList(value = "activity", type = Activity.class)
		public List<Activity> activitys;
		@XmlElementList(value = "provider", type = Provider.class)
		public List<Provider> providers;
		@XmlElementList(value = "receiver", type = Receiver.class)
		public List<Receiver> receivers;
		@XmlElementList(value = "service", type = Service.class)
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
		@XmlElementList(value = "action", type = NameBase.class)
		public List<NameBase> actions;
		@XmlElementList(value = "category", type = NameBase.class)
		public List<NameBase> categorys;
		@XmlAttribute(value = "priority", namespace = "http://schemas.android.com/apk/res/android")
		public long priority;
		@XmlElementList(value = "data", type = IntentData.class)
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
		@XmlAttribute(value = "scheme", namespace = "http://schemas.android.com/apk/res/android")
		public String scheme;
		@XmlAttribute(value = "host", namespace = "http://schemas.android.com/apk/res/android")
		public String host;
		@XmlAttribute(value = "path", namespace = "http://schemas.android.com/apk/res/android")
		public String path;

		@Override
		public String toString() {
			return "data[scheme=" + scheme + ",host=" + host + ",path=" + path + "]";
		}
	}

	public static class ContextBase extends AndroidBase {
		@XmlAttribute(value = "exported", namespace = "http://schemas.android.com/apk/res/android")
		public boolean exported = false;
		@XmlAttribute(value = "enabled", namespace = "http://schemas.android.com/apk/res/android")
		public boolean enabled = true;
		@XmlAttribute(value = "permission", namespace = "http://schemas.android.com/apk/res/android")
		public String permission;
		@XmlElementList(value = "intent-filter", type = IntentFilter.class)
		public List<IntentFilter> intentFilters;
	}

	public static class Service extends ContextBase {
		@Override
		public String toString() {
			return "\n\tService[name=" + name + ",enabled=" + enabled + ",exported=" + exported + ",\n\tpermission="
					+ permission + ",\n\t" + "theme=" + theme + ",label=" + label + ",icon=" + icon + ",\n\tmetadata="
					+ metadatas + "\n\t]";
		}
	}

	public static class Receiver extends ContextBase {
		@Override
		public String toString() {
			return "\n\tReceiver[name=" + name + ",enabled=" + enabled + ",exported=" + exported + ",\n\tpermission="
					+ permission + ",\n\t" + "theme=" + theme + ",label=" + label + ",icon=" + icon + ",\n\tmetadata="
					+ metadatas + "\n\t]";
		}
	}

	public static class Provider extends ContextBase {
		@Override
		public String toString() {
			return "\n\tProvider[name=" + name + ",enabled=" + enabled + ",exported=" + exported + ",\n\tpermission="
					+ permission + ",\n\t" + "theme=" + theme + ",label=" + label + ",icon=" + icon + ",\n\tmetadata="
					+ metadatas + "\n\t]";
		}
	}

	public static class Activity extends ContextBase {
		@XmlAttribute(value = "launchMode", namespace = "http://schemas.android.com/apk/res/android")
		public int launchMode;
		@XmlAttribute(value = "screenOrientation", namespace = "http://schemas.android.com/apk/res/android")
		public int screenOrientation;
		@XmlAttribute(value = "configChanges", namespace = "http://schemas.android.com/apk/res/android")
		public long configChanges;

		@Override
		public String toString() {
			return "\n\tActivity[name=" + name + ",enabled=" + enabled + ",exported=" + exported + ",\n\tpermission="
					+ ",launchMode=" + launchMode + ",permission=" + permission + ",enabled=" + enabled + ",exported="
					+ exported + ",configChanges=" + configChanges + ",screenOrientation=" + screenOrientation + ",\n\t"
					+ "theme=" + theme + ",label=" + label + ",icon=" + icon + ",\n\tmetadata=" + metadatas
					+ ",\n\tintentFilters=" + intentFilters + "\n\t]";
		}
	}
}
