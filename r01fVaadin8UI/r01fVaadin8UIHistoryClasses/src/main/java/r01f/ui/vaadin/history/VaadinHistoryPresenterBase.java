package r01f.ui.vaadin.history;

import java.util.Collection;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import r01f.locale.Language;
import r01f.model.facets.HistoryTrackedObjectOID;
import r01f.model.history.HistoryEntry;
import r01f.model.history.filter.HistoryEntryFilter;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.ui.presenter.UIPresenter;
import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.util.types.collections.CollectionUtils;

public abstract class VaadinHistoryPresenterBase<O extends HistoryTrackedObjectOID,H extends HistoryEntry<?,O>,
												 V extends VaadinViewHistoryEntry<?,O,H>,
												 CM extends VaadinHistoryCOREMediatorBase<O,H>>
		   implements UIPresenter {

	private static final long serialVersionUID = -1385756726450107565L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final CM _coreMediator;
	private final VaadinViewHistoryEntryFactory<H,V> _viewHistoryEntryFactory;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinHistoryPresenterBase(final CM coreMediator,
									  final VaadinViewHistoryEntryFactory<H,V> viewHistoryEntryFactory) {
		_coreMediator = coreMediator;
		_viewHistoryEntryFactory = viewHistoryEntryFactory;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ALL ENTRIES
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Loads the [user]'s history entries
	 * @param userOid
	 * @param lang
	 * @param subscriber
	 */
	public void onHistoryEntriesLoadRequested(final HistoryEntryFilter filter,final Language lang,
											  final UIPresenterSubscriber<Collection<V>> subscriber) {
		try {
			// [1] - Use the core mediator to load the user's [history entries]
			Collection<H> historyEntries = _coreMediator.findHistoryEntries(filter);
			
			// [2] - Transform to [view history entries]
			Collection<V> viewHistoryEntries = CollectionUtils.hasData(historyEntries)
													? historyEntries.stream()
																	// transform to [view history entry]
																	// BEWARE!! this method issues N * 2 core hits (usually db hits), one for each [history tracked object] 
																	//			just to get the [object]'s details and [user]'s details
																	.map(entry -> {
																			// load the [history tracked object]'s and [user]'s details
																			String objDetails = _coreMediator.getHistoryTrackeObjectDetails(entry.getAbout(),lang);
																			String userDetails = _coreMediator.getUserDetails(entry.getWho());
																			
																			V outViewEntry = _viewHistoryEntryFactory.createHistoryEntryFrom(entry,
																																			 userDetails,objDetails);
																			return outViewEntry;
																		 })
																	.collect(Collectors.toList())
													: Lists.newArrayList();
			// [99] - return
			subscriber.onSuccess(viewHistoryEntries);
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BY USER HISTORY ENTRIES
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Loads the [user]'s history entries
	 * @param userOid
	 * @param lang
	 * @param subscriber
	 */
	public void onUserHistoryEntriesLoadRequested(final UserOID userOid,final Language lang,
												  final UIPresenterSubscriber<Collection<V>> subscriber) {
		try {
			// [1] - Use the core mediator to load the user's [history entries]
			Collection<H> historyEntries = _coreMediator.findHistoryEntriesAboutUser(userOid);
			
			// [2] - Load the [user]'s details
			String userDetails = _coreMediator.getUserDetails(userOid);
			
			// [3] - Transform to [view history entries]
			Collection<V> viewHistoryEntries = CollectionUtils.hasData(historyEntries)
													? historyEntries.stream()
																	// transform to [view history entry]
																	// BEWARE!! this method issues N core hits (usually db hits), one for each [history tracked object]
																	//			just to get the object's details
																	.map(entry -> {
																			// load the [history tracked object]'s details
																			String objDetails = _coreMediator.getHistoryTrackeObjectDetails(entry.getAbout(),lang);
																			
																			V outViewEntry = _viewHistoryEntryFactory.createHistoryEntryFrom(entry,
																																			 userDetails,objDetails);
																			return outViewEntry;
																		 })
																	.collect(Collectors.toList())
													: Lists.newArrayList();
			// [99] - return
			subscriber.onSuccess(viewHistoryEntries);
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BY OBJECT HISTORY ENTRIES
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Loads the [object]'s history entries
	 * @param objOid
	 * @param lang
	 * @param subscriber
	 */
	public void onObjectHistoryEntriesLoadRequested(final O objectOid,final Language lang,
												    final UIPresenterSubscriber<Collection<V>> subscriber) {
		try {
			// [1] - Use the core mediator to load the user's [history entries]
			Collection<H> historyEntries = _coreMediator.findHistoryEntriesAboutObject(objectOid);
			
			// [2] - Load the [object]'s details
			String objDetails = _coreMediator.getHistoryTrackeObjectDetails(objectOid,lang);
			
			// [3] - Transform to [view history entries]
			Collection<V> viewHistoryEntries = CollectionUtils.hasData(historyEntries)
													? historyEntries.stream()
																	// transform to [view history entry]
																	// BEWARE!! this method issues N core hits (usually db hits), one for each [user]
																	//			just to get the user's details
																	.map(entry -> {
																			// load the [user]'s details
																			String userDetails = _coreMediator.getUserDetails(entry.getWho());
																			
																			V outViewEntry = _viewHistoryEntryFactory.createHistoryEntryFrom(entry,
																																			 userDetails,objDetails);
																			return outViewEntry;
																		 })
																	.collect(Collectors.toList())
													: Lists.newArrayList();
			// [99] - return
			subscriber.onSuccess(viewHistoryEntries);
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
}
