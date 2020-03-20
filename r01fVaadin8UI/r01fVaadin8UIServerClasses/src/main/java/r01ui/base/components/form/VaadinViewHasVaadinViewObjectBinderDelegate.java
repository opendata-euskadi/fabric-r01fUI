package r01ui.base.components.form;

import com.vaadin.data.Binder;

import r01f.ui.viewobject.UIViewObject;

@Deprecated	// see VaadinFormHasVaadinViewObjectBinderDelegate
public class VaadinViewHasVaadinViewObjectBinderDelegate<M extends UIViewObject> 
     extends VaadinFormHasVaadinViewObjectBinderDelegate<M> {

	private static final long serialVersionUID = -9129810845919783961L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR	
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewHasVaadinViewObjectBinderDelegate(final Binder<M> vaadinUIBinder) {
		super(vaadinUIBinder);
	}
}
