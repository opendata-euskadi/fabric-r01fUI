/**
 *
 */
package r01ui.base.components.layout;


import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Panel;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;

@Accessors(prefix="_")
public class VaadinViewDisplay
	 extends Panel
  implements VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = -3921436524914595338L;
/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * The main layout container. The application views will placed here.
	 * (this display is handed to the {@link Navigator} at the UI type)
	 */
	@Getter protected final SingleComponentContainer _viewDisplay;
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewDisplay() {
		super.setContent(new VerticalLayout());

		super.setStyleName(ValoTheme.PANEL_BORDERLESS);
		this.getContent().setMargin(false);
		this.getContent().setSizeFull();

		_viewDisplay = new Panel();

		this.getContent().addComponent(_viewDisplay);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public VerticalLayout getContent() {
		return (VerticalLayout)super.getContent();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		if (_viewDisplay instanceof VaadinViewI18NMessagesCanBeUpdated) {
			VaadinViewI18NMessagesCanBeUpdated viewI18NUpdatable = (VaadinViewI18NMessagesCanBeUpdated)_viewDisplay;
			viewI18NUpdatable.updateI18NMessages(i18n);
		}
	}
}
