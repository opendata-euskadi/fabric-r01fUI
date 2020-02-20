package r01f.ui.vaadin.notifications;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.navigator.View;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import r01f.patterns.FactoryFrom;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinView;
import r01f.util.types.collections.CollectionUtils;

/**
 * A window that shows application [notifications]
 * 
 * Ensure to include the [r01uinotifications.scss] styles file:
 * [1] - Go to the UI type and check the @Theme annotation
 * 		 It should be something like:
 *		        @Theme("myAppStyles")	
 *		        public class MyAppVaadinUI
 *		        	 extends UI {
 *		        	...
 *		        }
 * [2] - There must exist this folder: [WebContent]/VAADIN/themes/myAppStyles  <-- the @Theme annotation value
 * 		 The folder's contents should be:
 * 			[WebContent]/VAADIN/themes/myAppStyles
 * 				+ styles.scss
 * 				+ addons.scss
 * 				+ favicon.ico
 * 				+ myAppStyles.scss  		<-- the @Theme annotation value
 * 				+ r01uinotifications.scss	<-- this file contains the header styles
 * [3] - The [styles.scss] file must contain:
 *			@import "addons.scss";
 *			@import "myAppStyles.scss";
 *			@import "r01uinotifications.scss";	
 *
 *			.myAppStyles {
 *				@include addons;
 *				@include myAppStyles;	<-- the @Theme annotation value
 *				@include r01uiheader;		
 *			} 
 */
public abstract class VaadinNotificationsWindow<N extends VaadinNotificationViewObject> 
	 		  extends CustomComponent
	 	   implements VaadinView,
	 	   			  View {

	private static final long serialVersionUID = 5257147074747528270L;
/////////////////////////////////////////////////////////////////////////////////////////
//	SERVICES																		  
/////////////////////////////////////////////////////////////////////////////////////////
	private final transient VaadinNotificationsWindowPresenter<N> _notifWinPresenter;
	private final transient FactoryFrom<N,Component> _notifViewComponentFactory;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	UI
/////////////////////////////////////////////////////////////////////////////////////////	
	private Window _notifsWin;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR														  
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinNotificationsWindow(final UII18NService i18n,
									 final VaadinNotificationsWindowPresenter<N> notifWinPresenter,
									 final FactoryFrom<N,Component> notifViewComponentFactory) {
		super(new CssLayout());
		
		_notifWinPresenter = notifWinPresenter;
		_notifViewComponentFactory = notifViewComponentFactory;
		
		Button btn = _createNotificationsButton(i18n);
		this.getCompositionRoot()
			.addComponent(btn);
	}
	@Override
	public void detach() {
		this.close();		// ensure the window is closed
		super.detach();
	}
	@Override 
	public CssLayout getCompositionRoot() {
		return (CssLayout)super.getCompositionRoot();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PUBLIC METHODS																		  
/////////////////////////////////////////////////////////////////////////////////////////
	public void close() {
		if (_notifsWin != null
		 && _notifsWin.isAttached()) {
			_notifsWin.close();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BUTTON																		  
/////////////////////////////////////////////////////////////////////////////////////////
	private Button _createNotificationsButton(final UII18NService i18n) {
		Button btn = new Button(VaadinIcons.BELL);
		_notifWinPresenter.onNotificationsCountNeeded(count -> btn.setCaption(Integer.toString(count)));
		btn.addStyleNames("notifications",
						  ValoTheme.BUTTON_LINK,
						  "unread");				
		btn.addClickListener(event -> _openNotificationsWindow(i18n,
															   event.getClientX(),event.getRelativeX(),
															   event.getClientY(),event.getRelativeY()));
		return btn;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	NOTIFICATIONS WINDOW																		  
/////////////////////////////////////////////////////////////////////////////////////////
	private void _openNotificationsWindow(final UII18NService i18n,
										  final int cliX,final int relX,
										  final int cliY,final int relY) {
		if (_notifsWin == null) {
			_notifsWin = _createNotificationsWindow(i18n);		  
		}
		if (!_notifsWin.isAttached()) {
			_notifsWin.setPositionX(cliX - relX - 200);   
			_notifsWin.setPositionY(cliY - relY + 40);		 
			this.getUI().addWindow(_notifsWin);
			_notifsWin.focus();
		} else {
			_notifsWin.close();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	NOTIFICATIONS CONTENT																		  
/////////////////////////////////////////////////////////////////////////////////////////
	private Window _createNotificationsWindow(final UII18NService i18n) {
		Window outWin = new Window();
		outWin.setWidth(300.0f,Unit.PIXELS);
		outWin.addStyleName("notifications");
		outWin.setClosable(false);
		outWin.setResizable(false);
		outWin.setDraggable(false);
		
		Component notifs = _createNotificationsWindowContent(i18n);
		outWin.setContent(notifs);
		
		return outWin;
	}
	private Component _createNotificationsWindowContent(final UII18NService i18n) {
		VerticalLayout vLayoutWin = new VerticalLayout();
		
		// title
		//TODO: implements "VaadinViewI18NMessagesCanBeUpdated"
		final Label lblTitle = new Label(i18n.getMessage("notifications.title"));
		lblTitle.addStyleName(ValoTheme.LABEL_H3);
		lblTitle.addStyleName(ValoTheme.LABEL_NO_MARGIN);
		vLayoutWin.addComponent(lblTitle);

		// notifications
		_notifWinPresenter.onNotificationsLoadNeeded(i18n.getCurrentLanguage(),
													 notifs -> {
														if (CollectionUtils.isNullOrEmpty(notifs)) {
															// TODO print a message
														}
														else {
															// Create view components for each notification view object
															Component[] compNotifs = notifs.stream()
																						  .map(notifViewObj -> {// crate a view component with the view object data
																												Component notifViewComp = _notifViewComponentFactory.from(notifViewObj);
																													
																												// style
																												notifViewComp.addStyleName("notification-item");
																												return notifViewComp;
																						  					   })
																						  .toArray(Component[]::new);
															// put all view components in a VerticalLayout
															VerticalLayout vlayoutNotifs = new VerticalLayout(compNotifs);
															
															// Add to the window layout
															vLayoutWin.addComponent(vlayoutNotifs);
														} 
													});
		// footer
		final HorizontalLayout hLayoutFooter = new HorizontalLayout();
		hLayoutFooter.addStyleName(ValoTheme.WINDOW_BOTTOM_TOOLBAR);
		hLayoutFooter.setWidth("100%");
		hLayoutFooter.setSpacing(false);
		
		Button btnShowAll = _buildShowAllButton(i18n);
		hLayoutFooter.addComponent(btnShowAll);
		hLayoutFooter.setComponentAlignment(btnShowAll,
											Alignment.TOP_CENTER);
		
		// return 
		return vLayoutWin;
	}
	private Button _buildShowAllButton(final UII18NService i18n) {
		final Button btnShowAll = new Button(i18n.getMessage("notifs.seeAll"),
										  	 e -> Notification.show("NOT available (to be implemented)"));
		btnShowAll.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		btnShowAll.addStyleName(ValoTheme.BUTTON_SMALL);
		return btnShowAll;
	}
}
