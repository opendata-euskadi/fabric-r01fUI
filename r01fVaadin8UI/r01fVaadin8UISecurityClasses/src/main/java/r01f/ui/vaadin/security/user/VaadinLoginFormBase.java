package r01f.ui.vaadin.security.user;

import com.vaadin.ui.Composite;

import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;

/**
 * An [security provider] (usr/pwd, google, etc) data form that allows the user to configure the [security provider login]
 * ie: link / unlink the user with a [sercurity provider login]
 * 
 * @param <U>
 * @param <L>
 * @param <VU>
 * @param <VL>
 * @param <P>
 */
abstract class VaadinLoginFormBase<P extends VaadinUserEditPresenterBase<?,?,?,?,
													  					 ?,?,?,?,
													  					 ? extends VaadinUserEditCOREMediatorBase<?,?,?,?,?>>> 
       extends Composite
    implements VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = -7239653766778435205L;
/////////////////////////////////////////////////////////////////////////////////////////
//	SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	protected final transient UII18NService _i18n;
	protected final transient P _presenter;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	STATE avoid as much as possible
/////////////////////////////////////////////////////////////////////////////////////////	
	protected UserOID _userOid;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected VaadinLoginFormBase(final UII18NService i18n,
										 	  	  final P presenter) {
		////////// services
		_i18n = i18n;
		_presenter = presenter;
	}
}
