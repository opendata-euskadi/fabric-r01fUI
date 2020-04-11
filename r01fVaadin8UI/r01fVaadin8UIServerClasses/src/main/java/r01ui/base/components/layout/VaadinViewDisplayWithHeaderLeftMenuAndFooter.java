package r01ui.base.components.layout;


import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
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
	/**
	 * The layout >> [left menu] [view display]
	 */
	private final HorizontalLayout _splitLayout;
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
		
		// style the header
		
		// style the view display
		_viewDisplay.setStyleName(ValoTheme.PANEL_BORDERLESS);
		_viewDisplay.addStyleName(VaadinValoTheme.VIEW_CONTAINER);
		_viewDisplay.setSizeFull();
		
		// Horizontal split pannel: 
		//		[left menu] | [view display]
		_splitLayout = new HorizontalLayout();
		_splitLayout.setDefaultComponentAlignment(Alignment.TOP_LEFT);
		_splitLayout.setSizeFull();
		_splitLayout.setMargin(false);
		_splitLayout.setSpacing(false);
		_splitLayout.setResponsive(true);
		_splitLayout.setWidthFull();
		
		_splitLayout.addComponent(_leftMenu);
		_splitLayout.addComponent(_viewDisplay);
		
		_splitLayout.setExpandRatio(_viewDisplay,3f);	// this makes the [view display] to expand 3 more times than teh [menu]
		
		// Vertical layout: 
		//		[ header                     ]
		//		[ [left menu] | [view display]
		//		[ footer                     ]
		VerticalLayout ly = new VerticalLayout();
		if (_header != null) ly.addComponentAsFirst(_header);
							 ly.addComponent(_splitLayout);
		if (_footer != null) ly.addComponent(_footer);
		
		// ensure 100% height
		if (header != null)  ly.setExpandRatio(_header,0);
							 ly.setExpandRatio(_splitLayout,100);
		if (_footer != null) ly.setExpandRatio(_footer,0);
		
		// style
		ly.setMargin(false);
		ly.setSpacing(false);
		ly.setWidthFull();
		ly.setHeightFull();
		
		_splitLayout.addStyleName("r01-view-display-container");
		_leftMenu.addStyleName("r01-view-display-left-menu");
		_viewDisplay.addStyleName("r01-view-display");
		
		// composition root
		this.setCompositionRoot(ly);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void hideLeftMenu() {
		_leftMenu.setVisible(false);
	}
	public void showLeftMenu() {
		_leftMenu.setVisible(true);
	}
	public void toggleLeftMenu() {
		// toggle left menu visibility
		this.setLeftMenuVisible(!_leftMenu.isVisible());
	}
	public void setLeftMenuVisible(final boolean visible) {
		// toggle left menu visibility
		if (!visible) {
			this.hideLeftMenu();
		} else {
			this.showLeftMenu();
		}		
	}
	public boolean isLeftMenuVisible() {
		return _leftMenu.isVisible();
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
