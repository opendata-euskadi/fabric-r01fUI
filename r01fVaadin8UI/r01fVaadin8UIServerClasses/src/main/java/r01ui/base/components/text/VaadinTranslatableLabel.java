package r01ui.base.components.text;

import com.vaadin.shared.ui.ContentMode;
import com.vaadin.ui.Label;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.locale.I18NKey;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;

@Accessors(prefix="_")
public class VaadinTranslatableLabel 
	 extends Label 
  implements VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = 5597160685135390385L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final I18NKey _i18nKey;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinTranslatableLabel(final UII18NService i18n,
								   final I18NKey i18nKey) {
		this(i18n,i18nKey,
			 ContentMode.TEXT);
	}
	public VaadinTranslatableLabel(final UII18NService i18n,
								   final I18NKey i18nKey,final ContentMode contentMode) {
		super(i18nKey != null ? i18n.getMessage(i18nKey) : null,
			  contentMode);
		_i18nKey = i18nKey;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		if (_i18nKey != null) this.setValue(i18n.getMessage(_i18nKey));
	}
}
