/**
 * 
 */
package r01ui.base.components.layout;


import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Panel;
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
 * 		+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * 		+ [ H E A D E R ]                                             +
 * 		+												              +
 * 		+ +------ + +-----------------------------------------------+ +
 * 		+ + MENU  + |												| +
 * 		+ +       + |												| +
 * 		+ +       + |	Component container where [vaadin views]	| +
 * 		+ +       + |	are show									| +
 * 		+ +       + |												| +
 * 		+ +       + |												| +
 * 		+ +       + |												| +
 * 		+ +-------+ +-----------------------------------------------+ +
 * 		+												              +
 * 		+ [ F O O T E R] 								              +
 * 		+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 */
@Accessors(prefix="_")
public class VaadinViewDisplayWithHeaderLeftMenuAndFooter
	 extends Composite
  implements VaadinViewI18NMessagesCanBeUpdated {
	
	private static final long serialVersionUID = -3921436524914595338L;
/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////  
	/**
	 * The header layout
	 */
	@Getter private final Component _header;
	/**
	 * The menu layout
	 */
	@Getter private final Component _leftMenu;
	/**
	 * The view display: where the views are injected
	 */
	@Getter private final Panel _viewDisplay;
	/**
	 * The footer layout
	 */
	@Getter private final Component _footer;
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////  
	public VaadinViewDisplayWithHeaderLeftMenuAndFooter(final Component header,
														final Component leftMenu) {
		this(header,
			 leftMenu,
			 new VaadinUIFooter());
	}
	public VaadinViewDisplayWithHeaderLeftMenuAndFooter(final Component header,
														final Component leftMenu,
														final Component footer) {
		super();
		
		_header = header;
		_leftMenu = leftMenu;
		_viewDisplay = new Panel();
		_footer = footer;
		
		// style the view display
		_viewDisplay.setStyleName(ValoTheme.PANEL_BORDERLESS);
		_viewDisplay.addStyleName(VaadinValoTheme.VIEW_CONTAINER);
		
		// Horizontal split pannel: 
		//		[left menu] | [view display]
		HorizontalSplitPanel splitLayout = new HorizontalSplitPanel();
		splitLayout.setLocked(true);
		splitLayout.setSizeFull();
		splitLayout.addStyleName("r01-main-split-view");
		
		splitLayout.addComponent(_leftMenu);
		splitLayout.addComponent(_viewDisplay);
		
		// Vertical layout: 
		//		[ header                     ]
		//		[ [left menu] | [view display]
		//		[ footer                     ]
		VerticalLayout ly = new VerticalLayout();
		ly.addComponentAsFirst(_header);
		ly.addComponent(splitLayout);
		ly.addComponent(_footer);
		
		// style
		ly.setSizeFull();
		ly.setMargin(false);
		
		// composition root
		this.setCompositionRoot(ly);
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
}
