package r01ui.base.components.grid;	

import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

import r01f.patterns.Factory;
import r01f.ui.i18n.UII18NService;
import r01f.ui.subscriber.UISubscriber;
import r01f.ui.vaadin.view.VaadinViewFactories.VaadinViewFactory;
import r01f.ui.viewobject.UIViewObject;
import r01ui.base.components.form.VaadinDetailEditForm;
import r01ui.base.components.form.VaadinDetailEditFormBase;
import r01ui.base.components.form.VaadinDetailForm;
import r01ui.base.components.form.VaadinFormEditsViewObject;

/**
 * A grid / detail component
 * <pre class='brush:java'>
 *		
 *		[new]                                      [edit] [delete] [up] [down]
 *		+--------------------------------------------------------------------+
 *		|                                                                    |
 *		|--------------------------------------------------------------------|
 *		|                                                                    |
 *		|--------------------        -GRID--    -----------------------------|
 *		|                                                                    |
 *		|--------------------------------------------------------------------|
 *		|                                                                    |
 *		+--------------------------------------------------------------------+
 *      +====================================================================+
 *      |                                                                    |
 *      |                                                                    |
 *      |                                                                    |
 *      |                          Edit form                                 |
 *      |                                                                    |
 *      |                                                                    |
 *      | [Delete]                                            [Cancel] [OK]  |
 *      +====================================================================+
 * </pre>
 * 
 * Usage: see {@link VaadinCRUDGridBase}
 * @param <V>
 * @param <F>
 */
public abstract class VaadinCRUDGridWithDetailBase<// The view object
										 		   V extends UIViewObject>
	 		  extends VaadinCRUDGridBase<V> {

	private static final long serialVersionUID = -235814350977119673L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public <F extends VaadinDetailForm<V>
			 		& VaadinFormEditsViewObject<V>> VaadinCRUDGridWithDetailBase(final UII18NService i18n,
							  		   		 					   				 final VaadinViewFactory<F> formFactory,
							  		   		 					   				 final Factory<V> viewObjFactory) {
		this(i18n,
			 // edit detail factory
			 () -> new VaadinDetailEditFormBase<V,F>(i18n,
					 								 formFactory.from(i18n),	// form
					 								 viewObjFactory) {			// view obj factory
				 			private static final long serialVersionUID = 442840240106223037L;
				   },
			 // view obj factory
			 viewObjFactory);
	}
	public <F extends VaadinDetailForm<V>
			 		& VaadinFormEditsViewObject<V>> VaadinCRUDGridWithDetailBase(final UII18NService i18n,
							  		   		 					   				 final VaadinViewFactory<F> formFactory,
							  		   		 					   				 final Class<V> viewObjType,final Factory<V> viewObjFactory,
							  		   		 					   				 final String... viewObjPropertyNames) {	
		super(i18n,
			  // edit detail factory
			  () -> new VaadinDetailEditFormBase<V,F>(i18n,
					 								  formFactory.from(i18n),	// form
					 								  viewObjFactory) {			// view obj factory
							private static final long serialVersionUID = -5628170580725614674L;
			  },
			  // view object factory
			  viewObjFactory,
			  // grid cols factory
			  new Grid<>(viewObjType),
			  VaadinGridColumnProvider.createColumnProviderUsingViewObjectPropertiesWithNames(viewObjPropertyNames));	// grid columns are the property names
		
		// set the detail buttons behavior
		_setEditFormButtonsBehavior();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	MORE CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public <E extends VaadinDetailEditForm<V>> VaadinCRUDGridWithDetailBase(final UII18NService i18n,
																			final Factory<E> formFactory,
																			final Factory<V> viewObjFactory) {
		super(i18n,
			 // edit form factory
			 formFactory,
			 // view obj factory
			 viewObjFactory,
			 // grid & columns provider
			 null);		// no grid columns by default
		
		// set the detail buttons behavior
		_setEditFormButtonsBehavior();
	}
	public <E extends VaadinDetailEditForm<V>> VaadinCRUDGridWithDetailBase(final UII18NService i18n,
							  		   										final Factory<E> formFactory,
							  		   										final Class<V> viewObjType,final Factory<V> viewObjFactory,
							  		   										final String... viewObjPropertyNames) {	
		super(i18n,
			 // edit form factory
			 formFactory,
			 // view object factory
			 viewObjFactory,
			 // grid cols factory
			 new Grid<>(viewObjType),
			 VaadinGridColumnProvider.createColumnProviderUsingViewObjectPropertiesWithNames(viewObjPropertyNames));	// grid columns are the property names
		
		// set the detail buttons behavior
		_setEditFormButtonsBehavior();
	}
	public <E extends VaadinDetailEditForm<V>> VaadinCRUDGridWithDetailBase(final UII18NService i18n,
																			final Factory<E> formFactory,
																			final Factory<V> viewObjFactory,
																			final VaadinGridColumnProvider<V> gridColsProvider) {
		super(i18n,
			 // edit form factory
			 formFactory,
			 // view object factory
			 viewObjFactory,
			 // grid and grid cols provider
			 new Grid<>(),
			 gridColsProvider);
		
		// set the detail buttons behavior
		_setEditFormButtonsBehavior();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CANCEL BUTTON 
/////////////////////////////////////////////////////////////////////////////////////////
	private void _setEditFormButtonsBehavior() {
		_detailEditForm.addCancelButtonClickListner(clickEvent -> this.setEnabled(true));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ACTION METHODS (override if needed)
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected void doCreateItem(final V item,
								final UISubscriber<V> subscriber) {
		// delegate
		super.doCreateItem(item,
						   subscriber);
		// ...and setup ui
		_detailEditForm.close();
	}
	@Override
	protected void doSaveItem(final V item,
							  final UISubscriber<V> subscriber) {
		// delegate
		super.doSaveItem(item,
						 subscriber);
		// ...and setup ui
		_detailEditForm.close();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override 
	protected <F extends VaadinDetailEditForm<V>> F enterCreateNew() {
		// create the detail form and show
		F form = super.enterCreateNew();
		_showEditForm(form);
		
		return form;
	}
	@Override 
	protected <F extends VaadinDetailEditForm<V>> F enterEdit(final V viewObj) {	
		// create the detail form and show
		F form = super.enterEdit(viewObj);
		_showEditForm(form);
		
		return form;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private <E extends VaadinDetailEditForm<V>> void _showEditForm(final E form) {
		VerticalLayout root = (VerticalLayout)this.getCompositionRoot();
		
		if (root.getComponentCount() == 2) {
			// wrap the form with the [accept] | [cancel] | [delete] buttons
			root.addComponent(form);
		}
		form.setVisible(true);
	}
}