package r01ui.base.components.contact.phone;

import com.vaadin.ui.renderers.HtmlRenderer;

import r01f.locale.I18NKey;
import r01f.ui.i18n.UII18NService;
import r01ui.base.components.contact.VaadinContactMeanManageBase;

public class VaadinContactPhoneManage
	 extends VaadinContactMeanManageBase<VaadinViewContactPhone,
	 									 VaadinContactPhoneDetailEditWindow> {

	private static final long serialVersionUID = 793707311914095044L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinContactPhoneManage(final UII18NService i18n) {
		super(i18n,
			  new VaadinContactPhoneDetailEditWindow(i18n),
			  VaadinViewContactPhone::new,			// factory
			  I18NKey.named("contact.phone.list.title"));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void _configureMoreGridColumns(final UII18NService i18n) {
		////////// Columns
		// phone
		_grid.addColumn(VaadinViewContactPhone::getNumber)
			 .setCaption(i18n.getMessage("contact.phone.number"))
			 .setExpandRatio(0)
			 .setMinimumWidth(150)
			 .setResizable(false)
			 .setId("number");
		// type
		_grid.addColumn(VaadinViewContactPhone::getType)
			 .setCaption(i18n.getMessage("contact.phone.type"))
			 .setExpandRatio(0)
			 .setMinimumWidth(150)
			 .setResizable(false)
			 .setId("type");
		// availability range
		_grid.addColumn(VaadinViewContactPhone::getAvailableRange)
			 .setRenderer(VaadinViewContactPhone::availableRangeAsString,
					 	  new HtmlRenderer(""))
			 .setCaption(i18n.getMessage("contact.phone.available"))
			 .setExpandRatio(1)
			 .setResizable(false)
			 .setId("availability");
	}
}
