package r01f.ui.vaadin.security.components.search;

import java.util.Collection;

import lombok.extern.slf4j.Slf4j;
import r01f.client.api.security.SecurityAPIBase;
import r01f.model.security.user.User;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.ui.coremediator.UICOREMediator;
import r01f.util.types.Strings;
import r01ui.base.components.user.VaadinSecurityUserDirectory;

@Slf4j
public abstract class VaadinSecurityUserSearchCOREMediator<U extends User,
												 		   A extends SecurityAPIBase<U,?,?,?,?,?>>
  implements UICOREMediator {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final A _securityApi;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected VaadinSecurityUserSearchCOREMediator(final A securityApi) {
		_securityApi = securityApi;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	FIND
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<U> findUsersFilteringByText(final String text,final VaadinSecurityUserDirectory directory) {
		if (Strings.isNullOrEmpty(text)) return null;

		VaadinSecurityUserDirectory theSearchDir = directory != null ?  directory
																	: VaadinSecurityUserDirectory.LOCAL;
		Collection<U> outUsers = null;
		switch (theSearchDir) {
		case LOCAL:
			outUsers = this.findLocalUsersFilteringByText(text);
			break;
		case CORPORATE:
			outUsers = this.findCorporateUsersFilteringByText(text);
			break;
		default:
			throw new IllegalArgumentException(directory + " is NOT a supported directory!!");
		}
		return outUsers;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	FIND LOCAL & XLNets
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<U> findLocalUsersFilteringByText(final String text) {
		if (Strings.isNullOrEmpty(text)) return null;

		Collection<U> users = _securityApi.getUsersApi()
												 .getForFind()
												 .findByText(text);
		return users;
	}
	public abstract Collection<U> findCorporateUsersFilteringByText(final String text);
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * When an XLNets user is picked using the [user picker], the LOCAL user oid is NOT know (XLNets DO NOT returns this oid; it's a LOCAL oid)
	 * ... so the user is tried to be found at the LOCAL db using the XLNets user code.
	 * If the user DOES NOT exists at the LOCAL db (maybe the user has NEVER logged-in using XLNets), the user is CREATED
	 * otherwise, the existing LOCAL user is returned
	 * @param xlnetsPickedUser
	 * @return
	 */
	public U ensureThereExistsLocalUserForXLNetsUser(final U xlnetsPickedUser) {
		U outUser = null;

		// remember the DIRTY TRICK:
		// When searching for a [user] at the [xlnets directory], the [xlnets loginId] is set as UserOID (see above)
		LoginID xlnetsLoginId = LoginID.forId(xlnetsPickedUser.getOid().asString());

		log.info("[roles by area core mediator] > ensure there exists a LOCAL user for XLNets user={}",xlnetsLoginId);
		U existingLocalUser = this.findLocalUserForXLNetsUser(xlnetsLoginId);
		if (existingLocalUser != null) {
			log.info("\t - there DOES exists a LOCAL user for XLNets user={}",xlnetsLoginId);
			outUser = existingLocalUser;
		} else {
			log.info("\t - there DOES NOT exists a LOCAL user for XLNets user={}: create it",xlnetsLoginId);
			// create the user
			U createdLocalUser = _securityApi.getUsersApi()
											   .getForCrud()
											   .create(xlnetsPickedUser);

			outUser = createdLocalUser;
		}
		return outUser;
	}
	/**
	 * Tries to find a LOCAL user for an XLNets user
	 * @param xlnetsUserCode
	 * @return
	 */
	public U findLocalUserForXLNetsLogin(final LoginID xlnetsLoginId) {
		U user = _securityApi.getUsersApi()
							 .getForCrud()
							 .loadByOrNull(SecurityProviderID.XLNETS,xlnetsLoginId);
		return user;
	}
	/**
	 * Tries to find a LOCAL user for an XLNets user
	 * @param xlnetsUserCode
	 * @return
	 */
	public U findLocalUserForXLNetsUser(final LoginID xlnetsUserCode) {
		return this.findLocalUserForXLNetsLogin(xlnetsUserCode);
	}
}
