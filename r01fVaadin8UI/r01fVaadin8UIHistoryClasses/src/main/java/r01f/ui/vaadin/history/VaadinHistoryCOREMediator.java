package r01f.ui.vaadin.history;

import java.util.Collection;

import r01f.client.api.history.ClientAPIForHistoryBase;
import r01f.client.api.sub.security.user.UserSubApi;
import r01f.model.facets.HistoryTrackedObjectOID;
import r01f.model.history.HistoryActionAboutObject;
import r01f.model.history.HistoryEntry;
import r01f.model.security.user.User;
import r01f.ui.coremediator.UICOREMediator;

public abstract class VaadinHistoryCOREMediator<A extends HistoryActionAboutObject,
							  					O extends HistoryTrackedObjectOID,H extends HistoryEntry<A,O>,
							  					U extends User,
							  					// apis
							  					HA extends ClientAPIForHistoryBase<A,O,H,?,?,?>> 
		   implements UICOREMediator {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final HA _historyApi;
	private final UserSubApi<U> _usersApi;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinHistoryCOREMediator(final HA historyApi,
									 final UserSubApi<U> usersApi) {
		_historyApi = historyApi;
		_usersApi = usersApi;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public Collection<H> findHistoryEntriesAbout(final O objectOid) {
		return null;
	}
}
