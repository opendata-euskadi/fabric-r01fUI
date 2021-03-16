package r01f.ui.vaadin.history;

import r01f.locale.Language;
import r01f.model.facets.HistoryTrackedObjectOID;

public interface VaadinLoadsHistoryTrackedObjectDetails<O extends HistoryTrackedObjectOID> {
	/**
	 * Loads a history tracked object's name
	 * @param objOid
	 * @param lang
	 * @return
	 */
	public String loadHistoryTrackedObjectDetails(final O objOid,
										   	   	  final Language lang);

}
