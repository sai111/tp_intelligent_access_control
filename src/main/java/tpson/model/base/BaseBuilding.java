package tpson.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings("serial")
public abstract class BaseBuilding<M extends BaseBuilding<M>> extends Model<M> implements IBean {

	public void setId(java.lang.Long id) {
		set("id", id);
	}

	public java.lang.Long getId() {
		return getLong("id");
	}

	public void setCellId(java.lang.Integer cellId) {
		set("cell_id", cellId);
	}

	public java.lang.Integer getCellId() {
		return getInt("cell_id");
	}

	public void setName(java.lang.Integer name) {
		set("name", name);
	}

	public java.lang.Integer getName() {
		return getInt("name");
	}

}
