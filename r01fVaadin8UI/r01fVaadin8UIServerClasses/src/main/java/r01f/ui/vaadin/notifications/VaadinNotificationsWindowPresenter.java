package r01f.ui.vaadin.notifications;

import java.util.Collection;

import r01f.locale.Language;
import r01f.ui.presenter.UIPresenter;
import r01f.ui.presenter.UIPresenterSubscriber;

public interface VaadinNotificationsWindowPresenter<N extends VaadinNotificationViewObject>
         extends UIPresenter {
	
	public void onNotificationsLoadNeeded(final Language lang,
										  final UIPresenterSubscriber<Collection<N>> subscriber);
	public void onNotificationsCountNeeded(final UIPresenterSubscriber<Integer> subscriber);
}