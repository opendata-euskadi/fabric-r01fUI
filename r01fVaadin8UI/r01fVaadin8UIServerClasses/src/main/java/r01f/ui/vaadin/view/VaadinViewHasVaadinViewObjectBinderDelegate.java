package r01f.ui.vaadin.view;

import java.io.Serializable;

import com.vaadin.data.Binder;

import lombok.RequiredArgsConstructor;
import r01f.ui.viewobject.UIViewObject;

@RequiredArgsConstructor
public class VaadinViewHasVaadinViewObjectBinderDelegate<M extends UIViewObject> 
  implements VaadinViewHasVaadinViewObjectBinder<M>,
  			 Serializable {

	private static final long serialVersionUID = -5261480263250197252L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	private final Binder<M> _vaadinBinder;
/////////////////////////////////////////////////////////////////////////////////////////
//	[BEAN PROPERTIES] > [UI-CONTROLS]                                                                         
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void bindViewTo(final M obj) {
		// vaadinBinder.setBean binds the [ui controls] to the [bean properties] 
		// so when an [ui control] is update, the corresponding 
		// [bean property] is updated accordingly
		_vaadinBinder.setBean(obj);
	}
	@Override
	public void readBean(final M obj) {
		// vaadinBinder.readBean sets the [ui control] values to the 
		// corresponding [bean property]
		// ... BUT the [ui control] is NOT bound to the [bean property]
		//	   so when the [ui control] is updated, the [bean property]
		//	   is NOT changed
		_vaadinBinder.readBean(obj);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	[UI-CONTROLS] > [BEAN PROPERTIES]                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public M getViewObject() {
		return _vaadinBinder.getBean(); 
	}
	@Override
	public boolean writeBeanIfValid(final M viewObject) {
		// vaadinBinder.writeBeanIfValid() writes [bean properties]
		// from [ui control] values
		return _vaadinBinder.writeBeanIfValid(viewObject);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public boolean isValid() {
		//
		return _vaadinBinder.isValid();
	}
}
