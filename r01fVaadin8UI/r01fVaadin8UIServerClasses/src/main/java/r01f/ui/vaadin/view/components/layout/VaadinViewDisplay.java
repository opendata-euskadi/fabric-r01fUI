package r01f.ui.vaadin.view.components.layout;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewDisplay;
import com.vaadin.ui.Component;
import com.vaadin.ui.SingleComponentContainer;
import com.vaadin.ui.themes.ValoTheme;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix="_")
public class VaadinViewDisplay 
  implements ViewDisplay {
	private static final long serialVersionUID = -640692658586461375L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * The components container
	 */
	@Getter private final SingleComponentContainer _container;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewDisplay(final SingleComponentContainer container) {
		_container = container;
		_container.setSizeFull();
		_container.setStyleName(ValoTheme.PANEL_BORDERLESS);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void showView(final View view) {
		if ( !(view instanceof Component) ) {
			throw new IllegalArgumentException("View is not a component: " + view);
		}
		_container.setContent(view.getViewComponent());
	}
}