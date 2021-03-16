package r01f.ui.vaadin.history;

import r01f.model.history.HistoryEntry;

/**
 * Creates a [view history entry] from the [history entry] and the [user]'s & [history tracked object]'s details
 * @param <H>
 * @param <V>
 */
public interface VaadinViewHistoryEntryFactory<H extends HistoryEntry<?,?>,
											   V extends VaadinViewHistoryEntry<?,?,H>> {
	public V createHistoryEntryFrom(final H historyEntry,
								  	final String aboutDetails,final String whoDetails);
}
