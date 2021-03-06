package tpson.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseRoom<M extends BaseRoom<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Long id) {
		set("id", id);
	}

	public java.lang.Long getId() {
		return getLong("id");
	}

	public void setBuildingId(java.lang.Long buildingId) {
		set("building_id", buildingId);
	}

	public java.lang.Long getBuildingId() {
		return getLong("building_id");
	}

	public void setUnit(java.lang.Integer unit) {
		set("unit", unit);
	}

	public java.lang.Integer getUnit() {
		return getInt("unit");
	}

	public void setNumber(java.lang.Integer number) {
		set("number", number);
	}

	public java.lang.Integer getNumber() {
		return getInt("number");
	}

}
