package r01f.ui.vaadin.security.components.user.search;

import java.util.Collection;

import lombok.extern.slf4j.Slf4j;
import r01f.client.api.security.SecurityAPIBase;
import r01f.model.security.user.User;
import r01f.patterns.FactoryFrom;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.ui.coremediator.UICOREMediator;
import r01f.ui.vaadin.security.user.VaadinSecurityUserDirectory;
import r01f.util.types.Strings;

/**
 * A core mediator for [users]
 * Finds users at the LOCAL user db or at a CORPORATE user directory (ie XLNets)
 * @param <U>
 * @param <A>
 */
@Slf4j
public abstract class VaadinSecurityUserSearchCOREMediator<U extends User,
												 		   A extends SecurityAPIBase<U,?,?,?,?,?,?,?>>
  		   implements UICOREMediator {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final A _securityApi;
	protected final FactoryFrom<U,LoginID> _loginIdFromCorporateUser;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected VaadinSecurityUserSearchCOREMediator(final A securityApi,
												   final FactoryFrom<U,LoginID> loginIdFromCorporateUser) {
		_securityApi = securityApi;
		_loginIdFromCorporateUser = loginIdFromCorporateUser;
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
	 * @param corporatePickedUser
	 * @return
	 */
	public U ensureThereExistsLocalUserForCorporateUser(final U corporatePickedUser) {
		U outUser = null;

		// get the [coporate loginId] from the [corporate-picked] user
		LoginID corporateLoginId = _loginIdFromCorporateUser.from(corporatePickedUser);

		// try to find the user locally
		log.info("[roles by area core mediator] > ensure there exists a LOCAL user for XLNets user={}",corporateLoginId);
		U existingLocalUser = this.findLocalUserForCorporateLogin(corporateLoginId);

		// if the user exists locally, just do nothing
		if (existingLocalUser != null) {
			log.info("\t - there DOES exists a LOCAL user for XLNets user={}",corporateLoginId);
			outUser = existingLocalUser;
		}
		// if the user DOES NOT exists locally, create it
		else {
			log.info("\t - there DOES NOT exists a LOCAL user for XLNets user={}: create it",corporateLoginId);
			// create the user
			U createdLocalUser = _securityApi.getUsersApi()
											 .getForCrud()
											 .create(corporatePickedUser);

			outUser = createdLocalUser;
		}
		return outUser;
	}
	/**
	 * Tries to find a LOCAL user for a corporate user
	 * @param corporateLoginId
	 * @return
	 */
	public abstract U findLocalUserForCorporateLogin(final LoginID corporateLoginId);
}
