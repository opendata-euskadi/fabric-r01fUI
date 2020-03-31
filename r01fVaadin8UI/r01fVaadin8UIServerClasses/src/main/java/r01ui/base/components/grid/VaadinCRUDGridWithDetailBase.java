package r01ui.base.components.grid;	

import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.VerticalLayout;

import r01f.patterns.Factory;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewFactories.VaadinViewFactory;
import r01f.ui.viewobject.UIViewObject;
import r01ui.base.components.form.VaadinDetailEditForm;
import r01ui.base.components.form.VaadinDetailEditFormBase;
import r01ui.base.components.form.VaadinDetailForm;
import r01ui.base.components.form.VaadinFormBindings.VaadinFormHasVaadinUIBinder;

/**
 * A grid / detail component
 * <pre class='brush:java'>
 *		
 *											   [new] [delete] [up] [down]
 *		+--------------------------------------------------------------------+
 *		|--------------------------------------------------------------------|
 *		|--------------------------------------------------------------------|
 *		|--------------------------------------------------------------------|
 *		|--------------------        -GRID--    -----------------------------|
 *		|--------------------------------------------------------------------|
 *		|--------------------------------------------------------------------|
 *		|--------------------------------------------------------------------|
 *		+--------------------------------------------------------------------+
 *      +====================================================================+
 *      |--------------------------------------------------------------------|
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
			 		& VaadinFormHasVaadinUIBinder<V>> VaadinCRUDGridWithDetailBase(final UII18NService i18n,
							  		   		 					   						final VaadinViewFactory<F> formFactory,final Factory<V> viewObjFactory) {
		this(i18n,
			 // edit popup factory
			 () -> new VaadinDetailEditFormBase<V,F>(i18n,
					 								 formFactory.from(i18n)) {
				 			private static final long serialVersionUID = 442840240106223037L;

							@Override
							public void close() {
								this.setVisible(false);
							}
				   },
			 // view obj factory
			 viewObjFactory);
	}
	public <F extends VaadinDetailForm<V>
			 		& VaadinFormHasVaadinUIBinder<V>> VaadinCRUDGridWithDetailBase(final UII18NService i18n,
							  		   		 					   				   final VaadinViewFactory<F> formFactory,
							  		   		 					   				   final Class<V> viewObjType,final Factory<V> viewObjFactory,
							  		   		 					   				   final String... viewObjPropertyNames) {	
		super(i18n,
			  // edit popup factory
			 () -> new VaadinDetailEditFormBase<V,F>(i18n,
					 								 formFactory.from(i18n)) {
							private static final long serialVersionUID = -5628170580725614674L;

							@Override
							public void close() {
								this.setVisible(false);
							}
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
	public <W extends VaadinDetailEditForm<V>> VaadinCRUDGridWithDetailBase(final UII18NService i18n,
																			final Factory<W> formFactory,
																			final Factory<V> viewObjFactory) {
		super(i18n,
			 // edit form and popup factories
			 formFactory,
			 // view obj factory
			 viewObjFactory,
			 // grid & columns provider
			 null);		// no grid columns by default
	}
	public <W extends VaadinDetailEditForm<V>> VaadinCRUDGridWithDetailBase(final UII18NService i18n,
							  		   										final Factory<W> formFactory,
							  		   										final Class<V> viewObjType,final Factory<V> viewObjFactory,
							  		   										final String... viewObjPropertyNames) {	
		super(i18n,
			 // edit form and popup factories
			 formFactory,
			 // view object factory
			 viewObjFactory,
			 // grid cols factory
			 new Grid<>(viewObjType),
			 VaadinGridColumnProvider.createColumnProviderUsingViewObjectPropertiesWithNames(viewObjPropertyNames));	// grid columns are the property names
	}
	public <W extends VaadinDetailEditForm<V>> VaadinCRUDGridWithDetailBase(final UII18NService i18n,
							  												final Factory<W> formFactory,
							  												final Factory<V> viewObjFactory,
							  												final VaadinGridColumnProvider<V> gridColsProvider) {
		super(i18n,
			 // edit form and popup factories
			 formFactory,
			 // view object factory
			 viewObjFactory,
			 // grid and grid cols provider
			 new Grid<>(),
			 gridColsProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override 
	protected <W extends VaadinDetailEditForm<V>> W enterCreateNew() {
//		_grid.setEnabled(false);
		
		// create the detail form
		W win = super.enterCreateNew();
		
		// show it
		VerticalLayout root = (VerticalLayout)this.getCompositionRoot();
		if (root.getComponentCount() == 2) root.addComponent(new CssLayout(win));
		win.setVisible(true);
		return win;
	}
	@Override 
	protected <W extends VaadinDetailEditForm<V>> W enterEdit(final V viewObj) {		
		// create the detail form
		W win = super.enterEdit(viewObj);
		
		// show it
		VerticalLayout root = (VerticalLayout)this.getCompositionRoot();
		if (root.getComponentCount() == 2)	root.addComponent(new CssLayout(win));
		win.setVisible(true);
		return win;
	}
}