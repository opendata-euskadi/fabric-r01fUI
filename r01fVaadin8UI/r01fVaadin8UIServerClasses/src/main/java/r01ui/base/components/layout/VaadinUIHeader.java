package r01ui.base.components.layout;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

import lombok.experimental.Accessors;
import r01f.locale.I18NKey;
import r01f.locale.Language;
import r01f.securitycontext.SecurityContext;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.notifications.VaadinNotificationsWindow;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01ui.base.components.menu.VaadinMenuBar;
import r01ui.base.components.menu.VaadinUserMenu;
import r01ui.base.components.menu.VaadinUserMenu.R01UILanguageChangeEventListener;
import r01ui.base.components.text.VaadinTranslatableLabel;

/**
 * A header like:
 * <pre>
 * 		++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * 		+ [RightMenu]   Title     [notifications] [user] [left menu] +
 * 		+ |         |             |             |        |         | +
 * 		+ |         |             |             |        |         | + 
 * 		+ |         |             |             |        |         | +
 * 		+ +---------+             |             |        +---------+ +
 * 		+						  |             |                    +
 * 		+						  +-------------+ 					 +
 * 		++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 * Ensure to include the [r01uiheader.scss] styles file:
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
 * 				+ myAppStyles.scss  	<-- the @Theme annotation value
 * 				+ r01uiheader.scss		<-- this file contains the header styles
 * [3] - The [styles.scss] file must contain:
 *			@import "addons.scss";
 *			@import "myAppStyles.scss";
 *			@import "r01uiheader.scss";	
 *
 *			.myAppStyles {
 *				@include addons;
 *				@include myAppStyles;	<-- the @Theme annotation value
 *				@include r01uiheader;		
 *			} 
 */
@Accessors(prefix="_")
public class VaadinUIHeader
	 extends CustomComponent
  implements VaadinViewI18NMessagesCanBeUpdated {
	
	private static final long serialVersionUID = 7806676118570889809L;
/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////   
	private final VaadinTranslatableLabel _title;
	private final VaadinMenuBar _leftMenu;
	private final VaadinNotificationsWindow<?> _notificationsWindow;
	private final VaadinUserMenu _userMenu;
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUIHeader(final UII18NService i18n,
					   	  final VaadinMenuBar leftMenu,
					   	  final VaadinNotificationsWindow<?> notifsWin,
					   	  final SecurityContext securityContext,
					   	  final VaadinMenuBar rightMenu,
					   	  final Language... supportedLangs) {
		this(i18n,
			 null,		// no title
			 leftMenu,
			 notifsWin,
			 securityContext,
			 rightMenu,
			 supportedLangs);
	}
	public VaadinUIHeader(final UII18NService i18n,
					   	  final I18NKey titleI18NKey,
					   	  final VaadinMenuBar leftMenu,
					   	  final VaadinNotificationsWindow<?> notifsWin,
					   	  final SecurityContext securityContext,
					   	  final VaadinMenuBar rightMenu,
					   	  final Language... supportedLangs) {
		// set composition root
		super(new HorizontalLayout());
		
		// style header
		this.getCompositionRoot().setSizeFull();
		this.getCompositionRoot().addStyleName("r01header");
		
		// left menu
		_leftMenu = leftMenu;
		_leftMenu.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
		// title
		_title = new VaadinTranslatableLabel(i18n,
											titleI18NKey);		
		
		HorizontalLayout leftLayout = new HorizontalLayout(leftMenu,_title);	
		leftLayout.setComponentAlignment(leftMenu,Alignment.MIDDLE_LEFT);
		leftLayout.setComponentAlignment(_title,Alignment.MIDDLE_LEFT);		
		this.getCompositionRoot().addComponent(leftLayout);
		this.getCompositionRoot().setComponentAlignment(leftLayout,Alignment.MIDDLE_LEFT);
		// notifications
		_notificationsWindow = notifsWin;
		
		// right user menu
		_userMenu = new VaadinUserMenu(i18n,
									  securityContext,
									  rightMenu,
									  supportedLangs);
		
		
		HorizontalLayout rightLayout = new HorizontalLayout();
		if (_notificationsWindow != null) rightLayout.addComponent(_notificationsWindow);
		if (_userMenu != null) rightLayout.addComponent(_userMenu);
		if (_notificationsWindow != null) rightLayout.setComponentAlignment(_notificationsWindow,Alignment.MIDDLE_RIGHT);
		if (_userMenu != null) rightLayout.setComponentAlignment(_userMenu,Alignment.MIDDLE_RIGHT);
		this.getCompositionRoot().addComponents(rightLayout);
		this.getCompositionRoot().setComponentAlignment(rightLayout,Alignment.MIDDLE_RIGHT);	
		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LANGUAGE CHANGE EVENT LISTENER                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	public void addLanguageChangeEventListener(final R01UILanguageChangeEventListener langEventListener) {
		_userMenu.addLanguageChangeEventListener(langEventListener);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	UIViewI18NMessagesCanBeUpdated                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		_title.updateI18NMessages(i18n);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override 
	protected HorizontalLayout getCompositionRoot() {
		return (HorizontalLayout)super.getCompositionRoot();
	}
}
