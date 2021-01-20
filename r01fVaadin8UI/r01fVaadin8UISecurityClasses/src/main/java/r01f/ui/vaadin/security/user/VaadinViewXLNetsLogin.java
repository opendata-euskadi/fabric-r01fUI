package r01f.ui.vaadin.security.user;

import lombok.experimental.Accessors;
import r01f.guids.CommonOIDs.WorkPlaceCode;
import r01f.model.security.login.xlnets.XLNetsLogin;

@Accessors(prefix="_")
public abstract class VaadinViewXLNetsLogin<XL extends XLNetsLogin>
	 		  extends VaadinViewLogin<XL> {

	private static final long serialVersionUID = -693687373754829618L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewXLNetsLogin(final XL login) {
		super(login);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String WORKPLACE_CODE_FIELD = "workPlaceCode";
	public WorkPlaceCode getWorkPlaceCode() {
		return _wrappedModelObject.getWorkPlaceCode();
	}
	public void setWorkPlaceCode(final WorkPlaceCode workPlaceCode) {
		_wrappedModelObject.setWorkPlaceCode(workPlaceCode);
	}
}
