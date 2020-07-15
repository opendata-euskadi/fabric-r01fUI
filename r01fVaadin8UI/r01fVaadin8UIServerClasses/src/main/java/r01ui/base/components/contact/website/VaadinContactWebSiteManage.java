package r01ui.base.components.contact.website;

import r01f.locale.I18NKey;
import r01f.ui.i18n.UII18NService;
import r01ui.base.components.contact.VaadinContactMeanManageBase;

public class VaadinContactWebSiteManage
	 extends VaadinContactMeanManageBase<VaadinViewDirectoryContactWebSite,
	 											 VaadinContactWebSiteDetailEditWindow> {

	private static final long serialVersionUID = 793707311914095044L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinContactWebSiteManage(final UII18NService i18n) {
		super(i18n,
			  new VaadinContactWebSiteDetailEditWindow(i18n),
			  VaadinViewDirectoryContactWebSite::new,					// factory
			  I18NKey.named("contact.webSite.list.title"));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	protected void _configureMoreGridColumns(final UII18NService i18n) {
		// user
		_grid.addColumn(VaadinViewDirectoryContactWebSite::getUrl)
			 .setCaption(i18n.getMessage("contact.web.url"))
			 .setExpandRatio(0)
			 .setResizable(false)
			 .setId("url");
	}
}
