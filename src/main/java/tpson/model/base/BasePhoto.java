package tpson.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BasePhoto<M extends BasePhoto<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Long id) {
		set("id", id);
	}

	public java.lang.Long getId() {
		return getLong("id");
	}

	public void setMd5(java.lang.String md5) {
		set("md5", md5);
	}

	public java.lang.String getMd5() {
		return getStr("md5");
	}

	public void setUrl(java.lang.String url) {
		set("url", url);
	}

	public java.lang.String getUrl() {
		return getStr("url");
	}

	public void setType(java.lang.String type) {
		set("type", type);
	}

	public java.lang.String getType() {
		return getStr("type");
	}

	public void setWidth(java.lang.Integer width) {
		set("width", width);
	}

	public java.lang.Integer getWidth() {
		return getInt("width");
	}

	public void setHeight(java.lang.Integer height) {
		set("height", height);
	}

	public java.lang.Integer getHeight() {
		return getInt("height");
	}

	public void setCdn(java.lang.String cdn) {
		set("cdn", cdn);
	}

	public java.lang.String getCdn() {
		return getStr("cdn");
	}

	public void setCreatedTime(java.lang.Integer createdTime) {
		set("created_time", createdTime);
	}

	public java.lang.Integer getCreatedTime() {
		return getInt("created_time");
	}

}
