package r01ui.base.components.contact;

import com.vaadin.ui.Component;

import r01ui.base.components.form.VaadinDetailForm;
import r01ui.base.components.form.VaadinFormBindings.VaadinFormHasVaadinUIBinder;

public interface VaadinContactMeanDetailEdit<V extends VaadinContactMeanObject> 
		 extends VaadinDetailForm<V>,
		 		 VaadinFormHasVaadinUIBinder<V>,
		 		 Component {
	// just a marker interface
}
