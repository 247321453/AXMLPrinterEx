package com.kk.android;

import java.util.List;

import net.kk.xml.annotations.XmlAttribute;
import net.kk.xml.annotations.XmlElementList;

public class Resources {

	@XmlElementList(value = "public", type = PublicId.class)
	public List<PublicId> publicIds;

	public PublicId findResourceById(long id) {
		if (publicIds != null) {
			for (PublicId publicId : publicIds) {
				if(publicId.id==id){
					return publicId;
				}
			}
		}
		return null;
	}

	public static enum ResType {
		attr,
		drawable,
		layout,
		anim,
		raw,
		array,
		color,
		dimen,
		id,
		string,
		style
	}

	public static class PublicId {
		@XmlAttribute("type")
		public ResType type;
		@XmlAttribute("name")
		public String name;
		@XmlAttribute("id")
		public long id;
	}
}
