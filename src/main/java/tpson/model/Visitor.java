package tpson.model;

import tpson.model.base.BaseVisitor;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Visitor extends BaseVisitor<Visitor> {
	public static final Visitor dao = new Visitor().dao();
}
