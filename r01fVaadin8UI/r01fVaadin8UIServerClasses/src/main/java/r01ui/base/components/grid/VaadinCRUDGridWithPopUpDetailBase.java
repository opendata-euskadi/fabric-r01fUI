package r01ui.base.components.grid;	

import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;

import r01f.patterns.Factory;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewFactories.VaadinViewFactory;
import r01f.ui.viewobject.UIViewObject;
import r01ui.base.components.form.VaadinDetailEditForm;
import r01ui.base.components.form.VaadinDetailEditFormWindowBase;
import r01ui.base.components.form.VaadinDetailForm;
import r01ui.base.components.form.VaadinFormBindings.VaadinFormHasVaadinUIBinder;

/**
 * A grid with a popup to edit the row
 * <pre class='brush:java'>
 *		          +==============================+
 *		          |------------------------------|  [new] [delete] [up] [down]
 *		 +--------|                              |----------------------------+
 *		 |--------|                              |----------------------------|
 *		 |--------|          Edit form           |----------------------------|
 *		 |--------|                              |----------------------------|
 *		 |--------|                              |----------------------------|
 *		 |--------| [Delete]       [Cancel] [OK] |----------------------------|
 *		 |--------+==============================+----------------------------|
 *		 |--------------------------------------------------------------------|
 *		 +--------------------------------------------------------------------+
 * </pre>
 * 
 * Usage: see {@link VaadinCRUDGridBase}
 */
public abstract class VaadinCRUDGridWithPopUpDetailBase<// The view object
										 		   	    V extends UIViewObject>
	 		  extends VaadinCRUDGridBase<V> {

	private static final long serialVersionUID = 7015895869617900059L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public <F extends VaadinDetailForm<V>
			 		& VaadinFormHasVaadinUIBinder<V>> VaadinCRUDGridWithPopUpDetailBase(final UII18NService i18n,
							  		   		 					   						final VaadinViewFactory<F> formFactory,final Factory<V> viewObjFactory) {
		this(i18n,
			 // edit popup factory
			 () -> new VaadinDetailEditFormWindowBase<V,F>(i18n,
					 									   formFactory.from(i18n)) {
							private static final long serialVersionUID = -5628170580725614674L;
				   },
			 // view obj factory
			 viewObjFactory);
	}
	public <F extends VaadinDetailForm<V>
			 		& VaadinFormHasVaadinUIBinder<V>> VaadinCRUDGridWithPopUpDetailBase(final UII18NService i18n,
							  		   		 					   						final VaadinViewFactory<F> formFactory,
							  		   		 					   						final Class<V> viewObjType,final Factory<V> viewObjFactory,
							  		   		 					   						final String... viewObjPropertyNames) {	
		super(i18n,
			  // edit popup factory
			  () -> new VaadinDetailEditFormWindowBase<V,F>(i18n,
					  										formFactory.from(i18n)) {
							private static final long serialVersionUID = -5628170580725614674L;
				    },
			  // view object factory
			  viewObjFactory,
			  // grid cols factory
			  new Grid<>(viewObjType),
			  VaadinGridColumnProvider.createColumnProviderUsingViewObjectPropertiesWithNames(viewObjPropertyNames));	// grid columns are the property names
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	MORE CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////	
	public <F extends VaadinDetailForm<V>
			 		& VaadinFormHasVaadinUIBinder<V>,
			W extends VaadinDetailEditFormWindowBase<V,F>> VaadinCRUDGridWithPopUpDetailBase(final UII18NService i18n,
																			  		   		 final Factory<W> popUpFactory,
																			  		   		 final Class<V> viewObjType,final Factory<V> viewObjFactory,
																			  		   		 final String... viewObjPropertyNames) {
		super(i18n,
			 // edit popup factory
			 popUpFactory,
			 // view obj factory
			 viewObjFactory,
			 // grid cols factory
			 new Grid<>(viewObjType),
			 VaadinGridColumnProvider.createColumnProviderUsingViewObjectPropertiesWithNames(viewObjPropertyNames));		// no grid columns by default
	}
	public <F extends VaadinDetailForm<V>
			 		& VaadinFormHasVaadinUIBinder<V>,
			W extends VaadinDetailEditFormWindowBase<V,F>> VaadinCRUDGridWithPopUpDetailBase(final UII18NService i18n,
																			  		   		 final Factory<W> popUpFactory,
																			  		   		 final Factory<V> viewObjFactory) {
		super(i18n,
			  // edit popup factory
			  popUpFactory,
			  // view obj factory
			  viewObjFactory,
			  // grid & columns provider
			  (VaadinGridColumnProvider<V>)null);		// no grid columns by default
	}
	public <F extends VaadinDetailForm<V>
			 		& VaadinFormHasVaadinUIBinder<V>,
			W extends VaadinDetailEditFormWindowBase<V,F>> VaadinCRUDGridWithPopUpDetailBase(final UII18NService i18n,
							  												  		   		 final Factory<W> popUpFactory,
							  												  		   		 final Factory<V> viewObjFactory,
							  												  		   		 final VaadinGridColumnProvider<V> gridColsProvider) {
		super(i18n,
			  // edit popup factory
			  popUpFactory,
			  // view object factory
			  viewObjFactory,
			  // grid and grid cols provider
			  new Grid<>(),
			  gridColsProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	protected <W extends VaadinDetailEditForm<V>> W enterCreateNew() {
		// create the popup
		W win = super.enterCreateNew();
		
		// show it
		UI.getCurrent()
		  .addWindow((VaadinDetailEditFormWindowBase<V,?>)win);
		return win;
	}
	@Override @SuppressWarnings("unchecked")
	protected <W extends VaadinDetailEditForm<V>> W enterEdit(final V viewObj) {
		// create the popup
		W win = super.enterEdit(viewObj);
		
		// show it
		UI.getCurrent()
		  .addWindow((VaadinDetailEditFormWindowBase<V,?>)win);
		return win;
	}
}