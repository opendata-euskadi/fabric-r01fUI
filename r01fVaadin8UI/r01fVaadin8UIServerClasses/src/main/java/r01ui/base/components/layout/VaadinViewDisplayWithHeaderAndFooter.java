/**
 * 
 */
package r01ui.base.components.layout;


import com.vaadin.shared.Registration;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Panel;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.styles.VaadinValoTheme;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;

/**
 * A vaadin's panel that contains a [header], a [view container] and a [footer]
 * <pre>
 * 		+++++++++++++++++++++++++++++++++++++++++++++++++++
 * 		+ [ H E A D E R ]                                 +
 * 		+												  +
 * 		+ +---------------------------------------------+ +
 * 		+ |												| +
 * 		+ |												| +
 * 		+ |	Component container where [vaadin views]	| +
 * 		+ |	are show									| +
 * 		+ |												| +
 * 		+ |												| +
 * 		+ |												| +
 * 		+ +---------------------------------------------+ +
 * 		+												  +
 * 		+ [ F O O T E R] 								  +
 * 		+++++++++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 */
@Accessors(prefix="_")
public class VaadinViewDisplayWithHeaderAndFooter
	 extends Composite
  implements SingleComponentContainer,
  			 VaadinViewI18NMessagesCanBeUpdated {
	
	private static final long serialVersionUID = -3921436524914595338L;
/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////  
	/**
	 * The header layout
	 */
	@Getter private final Component _header;
	/**
	 * This is where the views are displayed
	 */
	@Getter private final Panel _viewDisplay;
	/**
	 * The footer layout
	 */
	@Getter private final Component _footer;
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////    
	public VaadinViewDisplayWithHeaderAndFooter(final Component header) {
		this(header,
			 new VaadinUIFooter());
	}
	public VaadinViewDisplayWithHeaderAndFooter(final Component header,
												final Component footer) {
		super();
		
		_header = header;
		_viewDisplay = new Panel();
		_footer = footer;
		
		VerticalLayout ly = new VerticalLayout();
		ly.addComponentAsFirst(_header);
		ly.addComponent(_viewDisplay);
		ly.addComponent(_footer);
		
		this.setCompositionRoot(ly);
		
		// style
		_viewDisplay.setStyleName(ValoTheme.PANEL_BORDERLESS);
		_viewDisplay.addStyleName(VaadinValoTheme.VIEW_CONTAINER);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		// update the [header] & [footer] messages
		if (_header instanceof VaadinViewI18NMessagesCanBeUpdated) {
			VaadinViewI18NMessagesCanBeUpdated i18nAwareHeader = (VaadinViewI18NMessagesCanBeUpdated)_header;
			i18nAwareHeader.updateI18NMessages(i18n);
		}
		if (_viewDisplay instanceof VaadinViewI18NMessagesCanBeUpdated) {
			VaadinViewI18NMessagesCanBeUpdated i18nAwareView = (VaadinViewI18NMessagesCanBeUpdated)_viewDisplay;
			i18nAwareView.updateI18NMessages(i18n);
		}
		if (_footer instanceof VaadinViewI18NMessagesCanBeUpdated) {
			VaadinViewI18NMessagesCanBeUpdated i18nAwareFooter = (VaadinViewI18NMessagesCanBeUpdated)_footer;
			i18nAwareFooter.updateI18NMessages(i18n);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SingleComponentContainer
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public Registration addComponentAttachListener(final ComponentAttachListener listener) {
		return _viewDisplay.addComponentAttachListener(listener);
	}
	@Override @Deprecated
	public void removeComponentAttachListener(final ComponentAttachListener listener) {
		_viewDisplay.removeComponentAttachListener(listener);
	}
	@Override
	public Registration addComponentDetachListener(final ComponentDetachListener listener) {
		return _viewDisplay.addComponentDetachListener(listener);
	}
	@Override @Deprecated
	public void removeComponentDetachListener(final ComponentDetachListener listener) {
		_viewDisplay.removeComponentDetachListener(listener);
	}
	@Override
	public int getComponentCount() {
		return _viewDisplay.getComponentCount();
	}
	@Override
	public Component getContent() {
		return _viewDisplay.getContent();
	}
	@Override
	public void setContent(final Component content) {
		_viewDisplay.setContent(content);
	}
}
