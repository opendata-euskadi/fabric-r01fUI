package r01ui.base.components.layout;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Component;
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
import r01ui.base.components.menu.VaadinUserMenu;
import r01ui.base.components.menu.VaadinUserMenu.R01UILanguageChangeEventListener;
import r01ui.base.components.text.VaadinTranslatableLabel;

/**
 * A header like:
 * <pre>
 * 		++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * 		+ [Component]   Title     [notifications] [user] [user info    ] +
 * 		+                         |             |        | [component] | +
 * 		+                         |             |        |             | + 
 * 		+                         |             |        | [en] [es]   | +
 *      +                         |             |        | [sign out]  | + 
 * 		+                         |             |        +-------------+ +
 * 		+						  |             |                        +
 * 		+						  +-------------+ 					     +
 * 		++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
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
public abstract class VaadinUIHeaderBase
	 		  extends CustomComponent
	 	   implements VaadinViewI18NMessagesCanBeUpdated {
	
	private static final long serialVersionUID = 7806676118570889809L;
/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final Component _leftComponent;
	protected final VaadinTranslatableLabel _lblTitle;
	protected final VaadinNotificationsWindow<?> _notificationsWindow;
	protected final VaadinUserMenu _userMenu;
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUIHeaderBase(final UII18NService i18n,
					   	  	  final Component leftComponent,
					   	  	  final VaadinNotificationsWindow<?> notifsWin,
					   	  	  final SecurityContext securityContext,
					   	  	  final Component rightComponent,
					   	  	  final Language... supportedLangs) {
		this(i18n,
			 null,		// no title
			 leftComponent,
			 notifsWin,
			 securityContext,
			 rightComponent,
			 supportedLangs);
	}
	public VaadinUIHeaderBase(final UII18NService i18n,
					   	  	  final I18NKey titleI18NKey,
					   	  	  final Component leftComponent,
					   	  	  final VaadinNotificationsWindow<?> notifsWin,
					   	  	  final SecurityContext securityContext,
					   	  	  final Component rightComponent,
					   	  	  final Language... supportedLangs) {
		// set composition root
		super(new HorizontalLayout());
		

		////////// components: [menu] [title]        [notifications] [menu]
		// left menu
		_leftComponent = leftComponent;
		_leftComponent.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
		
		// title
		_lblTitle = new VaadinTranslatableLabel(i18n,
												titleI18NKey);		
		// notifications
		_notificationsWindow = notifsWin;
		
		// right user menu
		_userMenu = new VaadinUserMenu(i18n,
									   securityContext,
									   rightComponent,
									   supportedLangs);
		
		////////// Left [menu] [title]
		HorizontalLayout lyLeft = new HorizontalLayout();
		if (_leftComponent != null) lyLeft.addComponent(_leftComponent);
		if (_lblTitle != null) lyLeft.addComponent(_lblTitle);
		if (_leftComponent != null) lyLeft.setComponentAlignment(_leftComponent,Alignment.MIDDLE_LEFT);
		if (_lblTitle != null) lyLeft.setComponentAlignment(_lblTitle,Alignment.MIDDLE_LEFT);		
		
		////////// Right [notifications] [menu]
		HorizontalLayout lyRight = new HorizontalLayout();
		if (_notificationsWindow != null) lyRight.addComponent(_notificationsWindow);
		if (_userMenu != null) 			  lyRight.addComponent(_userMenu);
		
		if (_notificationsWindow != null) lyRight.setComponentAlignment(_notificationsWindow,Alignment.MIDDLE_RIGHT);
		if (_userMenu != null)			  lyRight.setComponentAlignment(_userMenu,Alignment.MIDDLE_RIGHT);
		
		////////// Add components
		this.getCompositionRoot().addComponents(lyLeft,
												lyRight);
		////////// Style
		this.addStyleName("r01uiheader");
		this.getCompositionRoot().setSizeFull();
		this.getCompositionRoot().addStyleName("r01header");
		this.getCompositionRoot().setComponentAlignment(lyLeft,Alignment.MIDDLE_LEFT);
		this.getCompositionRoot().setComponentAlignment(lyRight,Alignment.MIDDLE_RIGHT);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EVENT LISTENER                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Deprecated
	public void addLanguageChangeEventListener(final R01UILanguageChangeEventListener langEventListener) {
		_userMenu.setLanguageChangeEventListener(langEventListener);
	}
	public void setLanguageChangeEventListener(final R01UILanguageChangeEventListener langEventListener) {
		_userMenu.setLanguageChangeEventListener(langEventListener);
	}
	public void addUserEditButtonClickListener(final ClickListener clickListener) {
		_userMenu.addEditUserButtonClickListener(clickListener);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	UIViewI18NMessagesCanBeUpdated                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		_lblTitle.updateI18NMessages(i18n);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public <C extends Component> C getLeftComponent() {
		return (C)_leftComponent;
	}
	public VaadinTranslatableLabel getTitle() {
		return _lblTitle;
	}
	public VaadinUserMenu getUserMenu() {
		return _userMenu;
	}
	@Override 
	protected HorizontalLayout getCompositionRoot() {
		return (HorizontalLayout)super.getCompositionRoot();
	}
}
