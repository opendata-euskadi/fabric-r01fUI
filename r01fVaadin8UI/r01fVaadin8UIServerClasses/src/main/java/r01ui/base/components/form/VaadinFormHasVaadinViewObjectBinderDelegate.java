package r01ui.base.components.form;

import java.io.Serializable;

import com.vaadin.data.Binder;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.ui.viewobject.UIViewObject;
import r01ui.base.components.form.VaadinFormBindings.VaadinFormHasVaadinUIBinder;

@Accessors(prefix="_")
@RequiredArgsConstructor
public class VaadinFormHasVaadinViewObjectBinderDelegate<M extends UIViewObject> 
  implements VaadinFormHasVaadinUIBinder<M>,
  			 Serializable {

	private static final long serialVersionUID = -5261480263250197252L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final Binder<M> _vaadinUIBinder;
/////////////////////////////////////////////////////////////////////////////////////////
//	[BEAN PROPERTIES] > [UI-CONTROLS]                                                                         
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void bindUIControlsTo(final M obj) {
		// vaadinBinder.setBean binds the [ui controls] to the [view object] properties 
		// so when an [ui control] is update, the corresponding [view object] property is updated accordingly
		// The inverse is ALSO TRUE: when the [view object] property is updated, the [ui control]
		// is updated acordingly
		_vaadinUIBinder.setBean(obj);
	}
	@Override
	public void readUIControlsFrom(final M obj) {
		// vaadinBinder.readBean binds the [ui controls] to the [view object] properties 
		// so when an [ui control] is update, the corresponding [view object] property is updated accordingly
		
		// ... BUT the inverse is NOT TRUE: when a [view object] property is updated, the [ui control]
		//	   is NOT changed since it's NOT bound to the [view object] property
		_vaadinUIBinder.readBean(obj);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	[UI-CONTROLS] > [BEAN PROPERTIES]                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public M getViewObject() {
		return _vaadinUIBinder.getBean(); 
	}
	@Override
	public boolean writeIfValidFromUIControlsTo(final M viewObject) {
		// vaadinBinder.writeBeanIfValid() writes [bean properties]
		// from [ui control] values
		return _vaadinUIBinder.writeBeanIfValid(viewObject);
	}
}
