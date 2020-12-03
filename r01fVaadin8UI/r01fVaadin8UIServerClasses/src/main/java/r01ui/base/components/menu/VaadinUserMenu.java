package r01ui.base.components.menu;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;

import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Page;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.locale.Language;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityContextStoreAtThreadLocalStorage;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.util.types.Dates;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.locale.Languages;

/**
 * Creates a user menu like
 * <pre>
 *    +-------------+
 *    [user info    ]
 *    +-------------+ 
 *    | [component] | 
 *    |             | 
 *    | [en] [es]   |
 *    |             | 
 *    | [sign out]  | 
 *    +-------------+ 
 * 
 * 
 * 
 * 
 * 
 * 
 * </pre>
 */
@RequiredArgsConstructor
public class VaadinUserMenu
	 extends CustomComponent
  implements VaadinViewI18NMessagesCanBeUpdated {
	
	private static final long serialVersionUID = 1927903598410137281L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	private final Button _btnUser;
	private final Component _component;
	private final VaadinLangButtons _langChangeButtons;
	private final Button _btnSignOut; 
	private final Label  _lblLanguageChange;
	private final Label _lblLastConnection;
	
	private R01UILanguageChangeEventListener _langEventListener;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUserMenu(final UII18NService i18n,
						  final SecurityContext securityContext,
						  final Component component,
						  final Language... supportedLangs) {
		super(new HorizontalLayout());
		
		this.getCompositionRoot()
			.setMargin(false);

		////////// Popup content
		VerticalLayout ly = new VerticalLayout();
		ly.setSizeFull();	
		
		// menu
		_component = component;
		if (_component != null) {
			_component.addStyleName(ValoTheme.MENUBAR_BORDERLESS);
			ly.addComponent(_component);
		}
		// change language
		Collection<R01UILangButton> langButtons = _createLanguageChangeButtons(supportedLangs);
		_langChangeButtons = new VaadinLangButtons(langButtons);
		HorizontalLayout hlLangs = new HorizontalLayout();
		_lblLastConnection = new Label();
		_lblLastConnection.addStyleName(ValoTheme.LABEL_SMALL);			
		
		_lblLastConnection.setValue(Strings.customized(i18n.getMessage("security.user.connexion.last"),
								  					  securityContext != null ?	Dates.formatterFor(Languages.of(i18n.getCurrentLocale()))
								  							  					     .formatDateWithTimeToSeconds(securityContext.getCreateDate())  
										  						   	    	  : "not known"));
		ly.addComponent(_lblLastConnection);
		_lblLanguageChange = new Label(i18n.getMessage("language.change"));
		_lblLanguageChange.addStyleName(ValoTheme.LABEL_BOLD);
		hlLangs.addComponent(_lblLanguageChange);
		hlLangs.setExpandRatio(_lblLanguageChange,1);
		if (_langChangeButtons.hasData()) {
			FluentIterable.from(_langChangeButtons)
						  .forEach(btn -> hlLangs.addComponent(btn.getButton()));
			_langChangeButtons.setCurrent(Languages.of(i18n.getCurrentLocale()));
			ly.addComponent(hlLangs);
		}
		// sign out
		_btnSignOut = new Button(i18n.getMessage("session.close"),
									   VaadinIcons.SIGN_OUT);
		
		_btnSignOut.addStyleNames(ValoTheme.BUTTON_LINK,
								 ValoTheme.BUTTON_ICON_ALIGN_RIGHT);
		_btnSignOut.addClickListener(event -> {
												// the redir to the login page MUST done BEFORE closing the session, 
												// as the UI or page are not available after that
												// see https://vaadin.com/docs/v8/framework/application/application-lifecycle.html#application.lifecycle.session-closing
												if (securityContext.getLoginUrl() != null) Page.getCurrent()
																			  				   .setLocation(securityContext.getLoginUrl().asString());
												// close the session & remove the security data
												// BEWARE!! close & invalidate the session
												UI.getCurrent()
												  .getSession().close();
												VaadinService.getCurrentRequest()
															 .getWrappedSession()
															 .invalidate();		
												// remove the user context from the thread-local
												SecurityContextStoreAtThreadLocalStorage.remove();
									});
		ly.addComponent(_btnSignOut);
		
		
		////////// Popup
		PopupView popup = new PopupView(null,
        								ly);
		popup.setStyleName("r01ui-userMenu");
		popup.setHideOnMouseOut(false);
		
		this.getCompositionRoot()
			.addComponent(popup);
		
		////////// User
		_btnUser = _createUserInfo(i18n,
								   securityContext);
		if (_btnUser != null) {			
			_btnUser.addClickListener(click -> popup.setPopupVisible(true));
			this.getCompositionRoot()
				.addComponent(_btnUser);
		}
	}
	@Override
	public HorizontalLayout getCompositionRoot() {
		return (HorizontalLayout)super.getCompositionRoot();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	private static Button _createUserInfo(final UII18NService i18n,
								   		  final SecurityContext securityContext) {
		String userName = securityContext != null ? securityContext.getLoginId() != null 
														? securityContext.getLoginId().asString()
														: null
												  : null;
		userName = userName != null ? userName
									: i18n.getMessage("unknown_user");
		Button btnUser = new Button();
		btnUser.addStyleName(ValoTheme.BUTTON_LINK);
		btnUser.setIcon(VaadinIcons.USER);
		btnUser.setCaption(userName);
		return btnUser;
	}
	private Collection<R01UILangButton> _createLanguageChangeButtons(final Language... supportedLangs) {
		if (CollectionUtils.isNullOrEmpty(supportedLangs)) return null;
		
		return FluentIterable.from(supportedLangs)
					.transform(lang -> {
										Button btnLang = new Button();
										btnLang.setCaption(Languages.languageLowerCase(lang));
										btnLang.addClickListener(e -> {	// fire a lang changed event
																		if (_langEventListener != null) {
																			// current locale (before change)
																			Language currLang = Languages.of(UI.getCurrent().getLocale());
																			
																			// build the event
																			R01UILanguageChangeEvent evt = new R01UILanguageChangeEvent(VaadinUserMenu.this,
																																		currLang,lang);
																			// change the ui locale
																			UI.getCurrent().setLocale(Languages.getLocale(lang));
																			
																			// update the lang buttons
																			_langChangeButtons.setCurrent(lang);
																			
																			// raise an event
																			_langEventListener.languageChanged(evt);
																			

																		}
																	  });
										return new R01UILangButton(lang,btnLang);
									   })
					.toList();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	private class VaadinLangButtons
	   implements Iterable<R01UILangButton>, 
				  Serializable {

		private static final long serialVersionUID = 2256163414191531302L;
		
		private final Collection<R01UILangButton> _langButtons;
		
		@Override
		public Iterator<R01UILangButton> iterator() {
			return _langButtons != null ? _langButtons.iterator()
										: Lists.<R01UILangButton>newArrayList().iterator();
		}
		public boolean hasData() {
			return CollectionUtils.hasData(_langButtons);
		}
		public void setCurrent(final Language lang) {
			if (CollectionUtils.isNullOrEmpty(_langButtons)) return;
			for (R01UILangButton langBtn : _langButtons) {
				if (langBtn.getLang().is(lang)) {
					langBtn.setCurrent();
				} else {
					langBtn.unsetCurrent();
				}
			}
		}
	}
	@Accessors(prefix="_")
	private class R01UILangButton 
	   implements Serializable {
		
		private static final long serialVersionUID = -998382762234341948L;
				
		@Getter private final Language _lang;
		@Getter private final Button _button;
		
		public R01UILangButton(final Language lang, final Button button) {			
			_lang = lang;
			_button = button;
			_button.addStyleNames(ValoTheme.BUTTON_PRIMARY,ValoTheme.BUTTON_SMALL);
		}		
		public void setCurrent() {
			_button.setEnabled(false);
		}
		public void unsetCurrent() {
			_button.setEnabled(true);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LANGUAGE CHANGE EVENT LISTENER                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	public void addLanguageChangeEventListener(final R01UILanguageChangeEventListener langEventListener) {
		_langEventListener = langEventListener;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	I18N                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		// tell the menu to update the messages
		if (_component != null
		 && _component instanceof VaadinViewI18NMessagesCanBeUpdated) {
			VaadinViewI18NMessagesCanBeUpdated i18nAwareComp = (VaadinViewI18NMessagesCanBeUpdated)_component;
			i18nAwareComp.updateI18NMessages(i18n);
		}
		_btnSignOut.setCaption(i18n.getMessage("session.close"));
		_lblLanguageChange.setValue(i18n.getMessage("language.change"));
		_lblLastConnection.setValue(Strings.customized(i18n.getMessage("security.user.connexion.last"),
								  					  SecurityContextStoreAtThreadLocalStorage.get() != null ?	Dates.formatterFor(Languages.of(i18n.getCurrentLocale()))
								  							  					     								 .formatDate(SecurityContextStoreAtThreadLocalStorage.get()
								  							  					     										 											 .getCreateDate()
								  							  					     										 	)  
								  							  					     						 : "not known"));		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EVENTS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Accessors(prefix="_")
	public class R01UILanguageChangeEvent 
	     extends Component.Event {
		private static final long serialVersionUID = 6771268655053782852L;
		
		@Getter private final Language _from;
		@Getter private final Language _to;
		
		public R01UILanguageChangeEvent(final Component source,
								  		final Language from,final Language to) {
			super(source);
			_from = from;
			_to = to;
		}
	}
	@FunctionalInterface
	public interface R01UILanguageChangeEventListener 
	         extends Serializable {
		void languageChanged(R01UILanguageChangeEvent event);
	}
}
