package r01ui.base.components;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.themes.ValoTheme;

/**
 * A dots [vertical menu] like:
 * <pre>
 * 		[:]
 * 		  +++++++++++++
 *        + Menu Item +
 *        + Menu Item +
 *        + Menu Item +
 *        +++++++++++++
 * </pre>
 */
public abstract class VaadinDotsVMenuBase<SELF_TYPE extends VaadinDotsVMenuBase<SELF_TYPE>>
			  extends MenuBar {

	private static final long serialVersionUID = 1987426127671369830L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final MenuItem _mainMenuItem;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinDotsVMenuBase() {
		super();
		super.setResponsive(true);
		super.setStyleName("component__content__framework");
		super.addStyleName(ValoTheme.BUTTON_PRIMARY);
		super.addStyleName(ValoTheme.BUTTON_BORDERLESS);

		_mainMenuItem = super.addItem("",
									  VaadinIcons.ELLIPSIS_DOTS_V,
									  null);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public MenuItem addItem(final String caption) {
		return _mainMenuItem.addItem(caption);
	}
	@Override
	public MenuItem addItem(final String caption,
							final Command command) {
		return _mainMenuItem.addItem(caption,
									 command);
	}
	@Override
	public MenuItem addItem(final String caption,final Resource icon,
							final Command command) {
		return _mainMenuItem.addItem(caption,icon,
									 command);
	}
	@Override
	public MenuItem addItemBefore(final String caption,final Resource icon,
								  final Command command,
								  final MenuItem itemToAddBefore) {
		// TODO Auto-generated method stub
		return _mainMenuItem.addItemBefore(caption,icon,
										   command,
										   itemToAddBefore);
	}
}
