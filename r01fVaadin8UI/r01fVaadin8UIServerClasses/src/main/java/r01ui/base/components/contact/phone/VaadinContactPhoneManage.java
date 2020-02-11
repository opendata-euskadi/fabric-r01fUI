package r01ui.base.components.contact.phone;

import com.vaadin.ui.renderers.HtmlRenderer;

import r01f.locale.I18NKey;
import r01f.ui.i18n.UII18NService;
import r01ui.base.components.contact.VaadinContactMeanManageBase;

public class VaadinContactPhoneManage
	 extends VaadinContactMeanManageBase<VaadinDirectoryContactPhone,
	 									 VaadinContactPhoneDetailEditWindow> {

	private static final long serialVersionUID = 793707311914095044L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinContactPhoneManage(final UII18NService i18n) {
		super(i18n,
			  new VaadinContactPhoneDetailEditWindow(i18n),
			  VaadinDirectoryContactPhone::new,			// factory
			  I18NKey.named("contact.phone.list.title"));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void _configureMoreGridColumns(final UII18NService i18n) {
		////////// Columns
		// phone
		_grid.addColumn(VaadinDirectoryContactPhone::getNumber)
			 .setCaption(i18n.getMessage("contact.phone.number"))
			 .setExpandRatio(0)
			 .setResizable(false)
			 .setId("number");
		// type
		_grid.addColumn(VaadinDirectoryContactPhone::getType)
			 .setCaption(i18n.getMessage("contact.phone.type"))
			 .setExpandRatio(0)
			 .setResizable(false)
			 .setId("type");
		// availability range
		_grid.addColumn(VaadinDirectoryContactPhone::getAvailableRange)
			 .setRenderer(VaadinDirectoryContactPhone::availableRangeAsString,
					 	  new HtmlRenderer(""))
			 .setCaption(i18n.getMessage("contact.phone.available"))
			 .setExpandRatio(0)
			 .setResizable(false)
			 .setId("availability");
	}
}
