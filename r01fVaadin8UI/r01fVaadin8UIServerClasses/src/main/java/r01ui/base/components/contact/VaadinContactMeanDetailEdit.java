package r01ui.base.components.contact;

import com.vaadin.ui.Component;

import r01f.ui.vaadin.view.VaadinDetailView;
import r01f.ui.vaadin.view.VaadinViewHasVaadinViewObjectBinder;

public interface VaadinContactMeanDetailEdit<V extends VaadinContactMeanObject> 
		 extends VaadinDetailView<V>,
		 		 VaadinViewHasVaadinViewObjectBinder<V>,
		 		 Component {
	// just a marker interface
}
