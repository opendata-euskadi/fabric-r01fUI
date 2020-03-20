package r01ui.base.components.form;

import com.vaadin.data.Binder;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.ui.viewobject.UIViewObject;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinFormBindings {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public interface VaadinFormHasVaadinUIBinder<V extends UIViewObject> 
			 extends VaadinBindsUIControlsToWiewObjectProperties<V>,
			 		 VaadinBindsUIControlsFromViewObjectProperties<V> {
		/**
		 * @return the Vaadin {@link Binder}
		 */
		public Binder<V> getVaadinUIBinder();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
// Binds the a [view object]'s properties to the underlying [ui control]s and back 
//	
// Uses vaadin's {@link Binder}  #tBean(bean) method that BINDs the [UI controls] to 
// the [bean's properties] so when an [UI control] is changed, the bean's property 
// is also updated. 
// The inverse is also true: when the [view object] is updated, the [ui controls] 
// are also updated
// 
// ... so changes in the [UI controls] are immediately reflected to the received bean 
//     so it's NOT possible to CANCEL editions
//	
// If this binding method is used, use {@link #getViewObject()} to get the [view object]
/////////////////////////////////////////////////////////////////////////////////////////	
	public interface VaadinBindsUIControlsToWiewObjectProperties<V extends UIViewObject> {
		@Deprecated // set bindUIControlsTo
		public default void bindViewTo(final V viewObj) {
			this.bindUIControlsTo(viewObj);
		}
		
		public void bindUIControlsTo(final V viewObj);
		
		public default void setViewObject(final V viewObject) {
			this.bindUIControlsTo(viewObject);
		}
		
		/**
		 * Returns the ui-controls binded [view object]
		 * This method MUST only be used if {@link #bindUIControlsTo(UIViewObject)} method was used beforehand
		 * @return
		 */
		public V getViewObject();
	}
/////////////////////////////////////////////////////////////////////////////////////////
// Sets the [UI control]s values from the [view object]'s properties values BUT not back
// 
// This method uses vaadin's {@link Binder} readBean(bean) method that BINDs the [UI controls] to 
// the [view object]'s properties so when an [UI control] is changed, the bean's property 
// is also updated. 
// BUT the inverses is NOT true: unlike {@link #bindViewTo(UIViewObject)} if the [view object] 
// is updated, the [ui controls] are NOT updated accordingly because [UI controls] are NOT bound 
// to the [bean properties]
//
// ... so changes in the [UI controls] are NOT reflected to the [view object] 
// so it's possible to CANCEL editions
/////////////////////////////////////////////////////////////////////////////////////////	
	public interface VaadinBindsUIControlsFromViewObjectProperties<V extends UIViewObject> {
		@Deprecated	// use readUIControlFrom
		public default void readBean(final V viewObj) {
			this.readUIControlsFrom(viewObj);
		}
		public void readUIControlsFrom(final V viewObj);
		
		@Deprecated	// see writeIfValidFromUIControlsTo
		public default boolean writeBeanIfValid(final V viewObject) {
			return this.writeIfValidFromUIControlsTo(viewObject);
		}
		/**
		 * Writes changes from the [UI controls] to the given [view object]
		 * There are TWO validation levels:
		 * [1] - [UI control]s validation: if any ui-field binding validator fails, no values are written and <code>false</code> is returned
		 * [2] - [View object] (bean) validation: If all [UI control] level-validators pass, the given bean is updated and bean-level validators are run on the 
		 * 						  				  updated bean. 
		 * 						  				  If any bean level validator fails, the bean updates are reverted and a <code>false<code> is returned
		 * @param viewObject
		 * @return
		 */
		public boolean writeIfValidFromUIControlsTo(final V viewObject);
	}
}
