/**
 *
 */
package r01ui.base.components.layout;


import com.vaadin.ui.Panel;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.themes.ValoTheme;

import lombok.experimental.Accessors;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.styles.VaadinValoTheme;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;

@Accessors(prefix="_")
public class VaadinViewDisplay
	 extends Panel	// SingleComponentContainer
  implements SingleComponentContainer,
  			 VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = -3921436524914595338L;
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewDisplay() {
		this.setStyleName(ValoTheme.PANEL_BORDERLESS);
		this.addStyleName(VaadinValoTheme.VIEW_CONTAINER);
		this.setSizeFull();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	COMPONENT CONTAINER;
/////////////////////////////////////////////////////////////////////////////////////////
	public SingleComponentContainer getViewDisplay() {
		return this;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		this.iterator()
			.forEachRemaining(comp -> {
								if (comp instanceof VaadinViewI18NMessagesCanBeUpdated) {
									VaadinViewI18NMessagesCanBeUpdated viewI18NUpdatable = (VaadinViewI18NMessagesCanBeUpdated)comp;
									viewI18NUpdatable.updateI18NMessages(i18n);
								}
							 });
	}
}
