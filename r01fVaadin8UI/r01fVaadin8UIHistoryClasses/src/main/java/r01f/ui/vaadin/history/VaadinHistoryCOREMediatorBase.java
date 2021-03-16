package r01f.ui.vaadin.history;	

import java.util.Collection;

import r01f.locale.Language;
import r01f.model.facets.HistoryTrackedObjectOID;
import r01f.model.history.HistoryEntry;
import r01f.model.history.filter.HistoryEntryFilter;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.ui.coremediator.UICOREMediator;

public abstract class VaadinHistoryCOREMediatorBase<O extends HistoryTrackedObjectOID,H extends HistoryEntry<?,O>> 
		   implements UICOREMediator {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final VaadinLoadsHistoryEntries<O,H> _historyApi;
	private final VaadinLoadsHistoryTrackedObjectDetails<O> _historyTrackedObjApi;
	private final VaadinLoadsUserDetails _usersApi;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinHistoryCOREMediatorBase(final VaadinLoadsHistoryEntries<O,H> historyApi,
									 	 final VaadinLoadsHistoryTrackedObjectDetails<O> historyTrackedObjApi,
									 	 final VaadinLoadsUserDetails usersApi) {
		_historyApi = historyApi;
		_historyTrackedObjApi = historyTrackedObjApi;
		_usersApi = usersApi;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ALL
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Finds the last history entries about a given object
	 * It returns history entries for the LAST [action] on the [object] by N [users]
	 * (the system stores a fixed number (N) of [users] that have recently worked with the [object])
	 * @param objectOid
	 * @return
	 */
	public Collection<H> findHistoryEntries(final HistoryEntryFilter filter) {
		return _historyApi.findEntriesBy(filter);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BY OBJECT
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Finds the last history entries about a given object
	 * It returns history entries for the LAST [action] on the [object] by N [users]
	 * (the system stores a fixed number (N) of [users] that have recently worked with the [object])
	 * @param objectOid
	 * @return
	 */
	public Collection<H> findHistoryEntriesAboutObject(final O objectOid) {
		return _historyApi.findUserLastObjectHistoryEntries(objectOid);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BY USER
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Finds the last history entries for a given user
	 * It returns history entries for the LAST [action] on N [objects] the [user] has recently worked with
	 * (the system stores a fixed number (N) of [objects] which the [user] has recently worked with)
	 * @param userOid
	 * @return
	 */
	public Collection<H> findHistoryEntriesAboutUser(final UserOID userOid) {
		return _historyApi.findObjectLastUserHistoryEntries(userOid);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	MODEL OBJECT DETAILS (called from presenter to enhance info about user/model obj)
/////////////////////////////////////////////////////////////////////////////////////////
	public String getUserDetails(final UserOID userOid) {
		return _usersApi.loadUserDetails(userOid);
	}
	/**
	 * Called by the presenter for each history entry to get the 
	 * history tracked object's name
	 * @param objOid
	 * @param lang
	 * @return
	 */
	public String getHistoryTrackeObjectDetails(final O objOid,
											 	final Language lang) {
		return _historyTrackedObjApi.loadHistoryTrackedObjectDetails(objOid,
																  	 lang);
	}
}
