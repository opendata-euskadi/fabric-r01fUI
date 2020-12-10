package r01f.ui.vaadin.security.components.user.search;

import java.util.Collection;
import java.util.stream.Collectors;

import r01f.client.api.security.SecurityAPIBase;
import r01f.model.security.user.User;
import r01f.patterns.FactoryFrom;
import r01f.ui.presenter.UIPresenter;
import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.ui.vaadin.security.user.VaadinSecurityUserDirectory;
import r01f.ui.vaadin.security.user.VaadinViewUser;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;

public abstract class VaadinSecurityUserSearchPresenter<U extends User,V extends VaadinViewUser<U>,
														C extends VaadinSecurityUserSearchCOREMediator<U,
																									   ? extends SecurityAPIBase<U,?,?,?,?,?>>>
  		   implements UIPresenter {

	private static final long serialVersionUID = -4624456885123354572L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final C _coreMediator;
	protected final FactoryFrom<U,V> _viewUserFactory;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinSecurityUserSearchPresenter(final C coreMediator,
											 final FactoryFrom<U,V> viewUserFactory) {
		_coreMediator = coreMediator;
		_viewUserFactory = viewUserFactory;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SEARCH
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Searches users at the local [users] table
	 * @param textFilter
	 * @param subscriber
	 */
	public void onSearchUserRequested(final String textFilter,final VaadinSecurityUserDirectory directory,
									  final UIPresenterSubscriber<Collection<V>> subscriber) {
		try {
			Collection<U> users = _coreMediator.findUsersFilteringByText(textFilter,directory);
			Collection<V> viewUsers = CollectionUtils.hasData(users) ? users.stream()
																			 .map(user -> _viewUserFactory.from(user))
																			 .collect(Collectors.toList())
																	 : Lists.newArrayList();
			subscriber.onSuccess(viewUsers);
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
	/**
	 * Searches users at xlnets
	 * @param textFilter
	 * @param subscriber
	 */
	public void onSearchCorporateUserRequested(final String textFilter,
									  		   final UIPresenterSubscriber<Collection<V>> subscriber) {
		try {
			Collection<U> users = _coreMediator.findCorporateUsersFilteringByText(textFilter);
			Collection<V> viewUsers = CollectionUtils.hasData(users) ? users.stream()
																			 .map(user -> _viewUserFactory.from(user))
																			 .collect(Collectors.toList())
																	 : Lists.newArrayList();
			subscriber.onSuccess(viewUsers);
		} catch (Throwable th) {
			subscriber.onError(th);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public V ensureThereExistsLocalUserForCorporateDirectoryUser(final V xlnetsPickedUser) {
		U outUser = _coreMediator.ensureThereExistsLocalUserForCorporateUser(xlnetsPickedUser.getWrappedModelObject());
		return outUser != null ? _viewUserFactory.from(outUser)
							   : null;
	}
}
