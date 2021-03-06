package tpson.model;

import com.jfinal.model.MergeConfig;
import com.jfinal.model.iAssociation;
import tpson.model.base.BaseUserRole;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class UserRole extends BaseUserRole<UserRole> implements iAssociation {
	public static final UserRole dao = new UserRole().dao();

	@Override
	public MergeConfig association(String fk) {
		return new MergeConfig(fk).setAlias("name", "role_name");
	}
}
