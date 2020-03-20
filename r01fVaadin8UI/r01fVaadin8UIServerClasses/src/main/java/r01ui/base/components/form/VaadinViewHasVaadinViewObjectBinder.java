package r01ui.base.components.form;

import r01f.ui.viewobject.UIViewObject;
import r01ui.base.components.form.VaadinFormBindings.VaadinFormHasVaadinUIBinder;

@Deprecated	// see VaadinFormHasVaadinUIBinder
public interface VaadinViewHasVaadinViewObjectBinder<V extends UIViewObject> 
		 extends VaadinFormHasVaadinUIBinder<V> {
	// just extend
}
