package com.hugboga.custom.data.bean;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;
import java.io.Serializable;

@Table(name = "country")
public class AreaCodeBean implements Serializable {
	@Column(name = "country_id", isId=true)
	public int  countryId;
	@Column(name = "cn_name")
	private String name;   //显示的名字
	@Column(name = "en_name")
	private String nameEn;   //显示的名字 英文
	@Column(name = "initial")
	private String sortLetters ="A";  //显示数据拼音的首字母
	@Column(name = "area_code")
	private String code; //显示的区号


	private boolean isFirst =false; //是否是第一个

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSortLetters() {
		return sortLetters;
	}
	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}

	public boolean isFirst() {
		return isFirst;
	}

	public void setIsFirst(boolean isFirst) {
		this.isFirst = isFirst;
	}
}
