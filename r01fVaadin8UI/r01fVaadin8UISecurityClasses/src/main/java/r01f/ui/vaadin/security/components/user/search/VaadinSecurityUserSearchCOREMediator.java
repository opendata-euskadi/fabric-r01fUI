package r01f.ui.vaadin.security.components.user.search;

import java.util.Collection;

import lombok.extern.slf4j.Slf4j;
import r01f.client.api.security.SecurityAPIBase;
import r01f.model.security.login.Login;
import r01f.model.security.user.UserBase;
import r01f.patterns.FactoryFrom;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.UserOID;
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
public abstract class VaadinSecurityUserSearchCOREMediator<U extends UserBase<U>,
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
		if (theSearchDir == VaadinSecurityUserDirectory.LOCAL) {
			outUsers = this.findLocalUsersFilteringByText(text);
		} else {
			outUsers = this.findCorporateUsersFilteringByText(directory,text);
		}
		return outUsers;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	FIND LOCAL & CORPORATE
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<U> findLocalUsersFilteringByText(final String text) {
		if (Strings.isNullOrEmpty(text)) return null;

		Collection<U> users = _securityApi.getUsersApi()
										  .getForFind()
										  .findByText(text);
		return users;
	}
	public abstract Collection<U> findCorporateUsersFilteringByText(final VaadinSecurityUserDirectory directory,final String text);
	
	/**
	 * Tries to find a LOCAL user for a corporate user
	 * @param corporateLoginId
	 * @return
	 */
	public abstract U findLocalUserForCorporateLogin(final LoginID corporateLoginId);
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * When a [corporate] user is picked using the [user picker], the LOCAL user oid is NOT know ([corporate directory] DO NOT returns this oid; it's a LOCAL oid)
	 * ... so the user is tried to be found at the LOCAL db using the XLNets user code.
	 * If the user DOES NOT exists at the LOCAL db (maybe the user has NEVER logged-in using XLNets), the user is CREATED
	 * otherwise, the existing LOCAL user is returned
	 * @param corporatePickedUser
	 * @return
	 */
	public U ensureThereExistsLocalUserForCorporateUser(final U corporatePickedUser) {
		U outUser = null;

		// get the [corporate loginId] from the [corporate-picked] user
		LoginID corporateLoginId = _loginIdFromCorporateUser.from(corporatePickedUser);
		if (corporateLoginId == null) throw new IllegalStateException("Could NOT get the [corporate user]'s [loginId] from the UI-picked [user]; this is a DEVELOPER fault!");

		// try to find the user locally
		log.info("[roles by area core mediator] > ensure there exists a LOCAL user for [corporate] user id={}",corporateLoginId);
		U existingLocalUser = this.findLocalUserForCorporateLogin(corporateLoginId);

		// if the user exists locally, just do nothing
		if (existingLocalUser != null) {
			log.info("\t - there DOES exists a LOCAL user for [corporate] user id={}",corporateLoginId);
			outUser = existingLocalUser;
		}
		// if the user DOES NOT exists locally, create it: create both the [user] and the [login]
		else {
			log.info("\t - there DOES NOT exists a LOCAL user for [corporate] user id={}: create it and it's associated login",corporateLoginId);
			// create the user
			if (corporatePickedUser.getOid() == null) corporatePickedUser.setOid(UserOID.supply());		// ensure the user has an oid
			U createdLocalUser = _securityApi.getUsersApi()
											 .getForCrud()
											 .create(corporatePickedUser);
			outUser = createdLocalUser;
			
			log.info("\t\t... created a LOCAL user with oid={} for [corporate] user id={}",
					 createdLocalUser.getOid(),corporateLoginId);
			
			// ensure the user run-time data
			createdLocalUser.copyRuntimeDataFrom(corporatePickedUser);
			
			// create the login
			Login createdLogin = _createCorporateLoginFor(createdLocalUser);	// BEWARE! hand the created user
			
			log.info("\t\t... created a login with oid={} for [corporate] user id={}",
					 createdLogin.getOid());
			
		}
		return outUser;
	}
	/**
	 * Creates a LOGIN for a created [corporate] user
	 * @param <L>
	 * @param user
	 * @return
	 */
	protected abstract <L extends Login> L _createCorporateLoginFor(final U user);
}
