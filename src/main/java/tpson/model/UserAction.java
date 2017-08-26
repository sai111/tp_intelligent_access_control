package tpson.model;

import com.jfinal.model.MergeConfig;
import com.jfinal.model.iAssociation;
import tpson.model.base.BaseUserAction;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class UserAction extends BaseUserAction<UserAction> implements iAssociation {
	public static final UserAction dao = new UserAction().dao();


	@Override
	public MergeConfig association(String fk) {
		return new MergeConfig(fk).setColumns("name", "group_id").setAlias("name","action_name");
	}
}