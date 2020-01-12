package r01ui.base.components.layout;

import lombok.experimental.Accessors;
import r01f.locale.I18NKey;
import r01f.locale.Language;
import r01f.securitycontext.SecurityContext;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.notifications.VaadinNotificationsWindow;
import r01ui.base.components.menu.VaadinMenuBar;

/**
 * A header like:
 * <pre>
 * 		++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * 		+ [Menu     ]   Title     [notifications] [user] [user info] +
 * 		+ |         |             |             |        |[menu]   | +
 * 		+ |         |             |             |        |es eu en | + 
 * 		+ |         |             |             |        |sign out | +
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
	 extends VaadinUIHeaderBase {
	

	private static final long serialVersionUID = 2584139654619115128L;
	
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
		super(i18n,
			  titleI18NKey,
			  leftMenu,
			  notifsWin,
			  securityContext,
			  rightMenu,
			  supportedLangs);
	}
}
