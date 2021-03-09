package r01f.ui.vaadin.history;

import r01f.securitycontext.SecurityOIDs.UserOID;

public interface VaadinLoadsUserDetails {
	/**
	 * Loads user details
	 * @param userOid
	 * @return
	 */
	public String loadUserDetails(final UserOID userOid);
}
