package r01f.ui.vaadin.history;

import java.util.Collection;

import r01f.model.facets.HistoryTrackedObjectOID;
import r01f.model.history.HistoryEntry;
import r01f.model.history.filter.HistoryEntryFilter;
import r01f.securitycontext.SecurityOIDs.UserOID;

public interface VaadinLoadsHistoryEntries<O extends HistoryTrackedObjectOID,H extends HistoryEntry<?,O>> {
	/**
	 * Finds all history entries filtering by the given filter
	 * @param filter
	 * @return
	 */
	public Collection<H> findEntriesBy(final HistoryEntryFilter filter);
	/**
	 * Finds the last history entries for a given user
	 * It returns history entries for the LAST [action] on N [objects] the [user] has recently worked with
	 * (the system stores a fixed number (N) of [objects] which the [user] has recently worked with)
	 * @param userOid
	 * @return
	 */
	public Collection<H> findObjectLastUserHistoryEntries(final UserOID userOid);
	/**
	 * Finds the last history entries about a given object
	 * It returns history entries for the LAST [action] on the [object] by N [users]
	 * (the system stores a fixed number (N) of [users] that have recently worked with the [object])
	 * @param objectOid
	 * @return
	 */
	public Collection<H> findUserLastObjectHistoryEntries(final O objectOid);
}
