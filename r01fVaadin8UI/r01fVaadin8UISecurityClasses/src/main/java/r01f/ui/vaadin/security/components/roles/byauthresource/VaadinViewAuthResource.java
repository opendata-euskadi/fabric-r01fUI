package r01f.ui.vaadin.security.components.roles.byauthresource;

import java.util.Collection;

import r01f.model.security.summaries.SummarizedAuthorizationResourceBase;
import r01f.model.security.summaries.UserAuthWithRoleCount;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.ui.viewobject.UIViewObjectWrappedBase;

public abstract class VaadinViewAuthResource<RS extends SummarizedAuthorizationResourceBase<?>>
	          extends UIViewObjectWrappedBase<RS> {

	private static final long serialVersionUID = -51649472571221508L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewAuthResource(final RS authRes) {
		super(authRes);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	OID & NAME
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String OID_FIELD = "oid";

	public AuthorizationTargetResourceOID getOid() {
		return _wrappedModelObject.getOid();
	}

	public static final String NAME_FIELD = "name";

	public String getName() {
		return _wrappedModelObject.getName();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String RESOURCES_FIELD = "resources";

	public String getResources() {
		return _wrappedModelObject.getResources();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String USER_AUTHS_COUNT_BY_ROLE_FIELD = "userAuthsCountByRole";

	public Collection<UserAuthWithRoleCount> getUserAuthsCountByRole() {
		return _wrappedModelObject.getUserAuthsCountByRole();
	}

	public long getUserAuthsCountWithRole(final UserRole userRole) {
		return _wrappedModelObject.getUserAuthWithRoleCount(userRole);
	}
}
