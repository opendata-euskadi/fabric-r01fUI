package r01f.ui.vaadin.view;

import com.vaadin.data.Binder;

import r01f.ui.viewobject.UIViewHasViewObject;
import r01f.ui.viewobject.UIViewObject;

public interface VaadinViewHasVaadinViewObjectBinder<M extends UIViewObject>
  		 extends UIViewHasViewObject<M> {	// if binding the view object...
/////////////////////////////////////////////////////////////////////////////////////////
//	VIEW OBJECT > UI CONTROLS																		  
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Binds the given object to the underlying UI controls
	 * This method uses vaadin's {@link Binder} setBean method that BINDs the [UI controls] to 
	 * the [bean's properties] so when an [UI control] is changed, the bean's property 
	 * is also updated
	 * @param obj
	 */
	public void bindViewTo(final M obj);
	
	@Override
	public default void setViewObject(final M viewObject) {
		this.bindViewTo(viewObject);
	}
	/**
	 * Sets the [UI controls] values from the [bean properties] values
	 * BEWARE!!	Unlike {@link #bindViewTo(UIViewObject)} if the UI controls are updated,
	 * 			the bean is NOT updated accordingly because [UI controls] are NOT bound to 
	 * 			the [bean properties]
	 * @param obj
	 */
	public void readBean(final M obj);
/////////////////////////////////////////////////////////////////////////////////////////
//	UI CONTROLS > VIEW OBJECT																		  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Writes changes from the [UI controls] to the given [bean properties]
	 * There are TWO validation levels:
	 * [1] - [UI control]s validation: if any ui-field binding validator fails, no values are written and <code>false</code> is returned
	 * [2] - [View object] (bean) validation: If all [UI control] level-validators pass, the given bean is updated and bean-level validators are run on the 
	 * 						  				  updated bean. 
	 * 						  				  If any bean level validator fails, the bean updates are reverted and a <code>false<code> is returned
	 * @param viewObject
	 * @return
	 */
	public boolean writeBeanIfValid(final M viewObject);
/////////////////////////////////////////////////////////////////////////////////////////
//																			  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * There are TWO validation levels:
	 * [1] - UI control fields validation
	 * [2] - View Object (bean) validation: 
	 *
	 * This method runs:
	 * 	- [ui control field]s validation
	 *  - [view object] validation (ONLY if a bean is currently set with {@link #setBean(Object)})
	 * Returns whether any of the validators failed.
	 * 
	 * NOTE:	This method will attempt to temporarily apply all current changes to the bean 
	 * 			and run full bean validation for it. 
	 * 			The changes are reverted after bean validation.
	 * @return whether this binder is in a valid state
	 * @throws IllegalStateException if bean level validators have been configured and no bean is currently set
	 */
	public boolean isValid();
}
