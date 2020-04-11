package r01ui.base.components.grid;	

import com.vaadin.ui.Grid;
import com.vaadin.ui.UI;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.patterns.Factory;
import r01f.types.Dimensions2D;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewFactories.VaadinViewFactory;
import r01f.ui.viewobject.UIViewObject;
import r01ui.base.components.form.VaadinDetailEditForm;
import r01ui.base.components.form.VaadinDetailEditFormWindowBase;
import r01ui.base.components.form.VaadinDetailForm;
import r01ui.base.components.form.VaadinFormEditsViewObject;

/**
 * A grid with a popup to edit the row
 * <pre class='brush:java'>
 *		          +==============================+
 *		 [new]    |------------------------------|         [delete] [up] [down]
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
 * 
 * Set the popup dimensions:
 * Option 1: Override _configureDetailEditForm(form) method 
 * 				<pre class='brush:java'>
 * 					public class MyCRUDGrid
 * 						 extends VaadinCRUDGridWithPopUpDetailBase<MyViewObj> {
 * 						...
 * 	
 * 						@Override
 *						protected <W extends VaadinDetailEditForm<V>> void _configureDetailEditForm(final W editForm) {
 *							win.setWidth(90,Unit.PERCENTAJE);
 *							win.setHeight(90,Unit.PERCENTAJE);	
 *						}
 * 					}
 * 				</pre>
 * 
 * Option 2: explicitly set the width
 * 				<pre class='brush:java'>
 * 					MyCRUDGrid crudGrid = new MyCRUDGrid(i18n);
 * 					crudGrid.setWinDimensions(new Dimensions2D(80,80);
 * 					crudGrid.setWinDimensionsUnit(Unit.PERCENTAGE);		// not strictly necessary since it's PERCENTAGE BY DEFAULT			
 * 				</pre>
 */
@Accessors(prefix="_")
public abstract class VaadinCRUDGridWithPopUpDetailBase<V extends UIViewObject>	// The view object
	 		  extends VaadinCRUDGridBase<V> {

	private static final long serialVersionUID = 7015895869617900059L;
/////////////////////////////////////////////////////////////////////////////////////////
//	POP-UP DIMENSIONS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter @Setter private Dimensions2D _winDimensions;
	@Getter @Setter private Unit _winDimensionsUnit = Unit.PERCENTAGE;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
// eclipse bug: crash while compiling in eclipse
	public <F extends VaadinDetailForm<V>
			 		& VaadinFormEditsViewObject<V>> VaadinCRUDGridWithPopUpDetailBase(final UII18NService i18n,
							  		   		 					   					  final VaadinViewFactory<F> formFactory,
																					  final Factory<V> viewObjFactory) {
		this(i18n,
			 // edit popup factory
			 new Factory<VaadinDetailEditFormWindowBase<V,F>>() {
					@Override
					public VaadinDetailEditFormWindowBase<V, F> create() {
						return new VaadinDetailEditFormWindowBase<V,F>(i18n,
							 									   	   formFactory.from(i18n),
							 									   	   viewObjFactory) {
								private static final long serialVersionUID = -5628170580725614674L;
						};
					}
			 },
			 // view obj factory
			 viewObjFactory);
	}
	public <F extends VaadinDetailForm<V>
			 		& VaadinFormEditsViewObject<V>> VaadinCRUDGridWithPopUpDetailBase(final UII18NService i18n,
							  		   		 					   					  final VaadinViewFactory<F> formFactory,
							  		   		 					   					  final Class<V> viewObjType,final Factory<V> viewObjFactory,
							  		   		 					   					  final String... viewObjPropertyNames) {	
		super(i18n,
			  // edit popup factory
			  new Factory<VaadinDetailEditFormWindowBase<V,F>>() {
					@Override
					public VaadinDetailEditFormWindowBase<V, F> create() {
						return new VaadinDetailEditFormWindowBase<V,F>(i18n,
					  												   formFactory.from(i18n),
					  												   viewObjFactory) {
							private static final long serialVersionUID = -5628170580725614674L;
						};
					}
			  },
			  // view object factory
			  viewObjFactory,
			  // grid cols factory
			  new Grid<>(viewObjType),
			  VaadinGridColumnProvider.createColumnProviderUsingViewObjectPropertiesWithNames(viewObjPropertyNames));	// grid columns are the property names
		
		// set the detail buttons behavior
		_setEditFormButtonsBehavior();
	}
///////////////////////////////////////////////////////////////////////////////////////
//	MORE CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////	
	public <F extends VaadinDetailForm<V>
			 		& VaadinFormEditsViewObject<V>,
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
		
		// set the detail buttons behavior
		_setEditFormButtonsBehavior();
	}
	public <F extends VaadinDetailForm<V>
			 		& VaadinFormEditsViewObject<V>,
			W extends VaadinDetailEditFormWindowBase<V,F>> VaadinCRUDGridWithPopUpDetailBase(final UII18NService i18n,
																			  		   		 final Factory<W> popUpFactory,
																			  		   		 final Factory<V> viewObjFactory) {
		super(i18n,
			  // edit popup factory
			  popUpFactory,
			  // view obj factory
			  viewObjFactory);		// no grid columns by default
		
		// set the detail buttons behavior
		_setEditFormButtonsBehavior();
	}
	public <F extends VaadinDetailForm<V>
			 		& VaadinFormEditsViewObject<V>,
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
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	protected <W extends VaadinDetailEditForm<V>> W enterCreateNew() {
		// create the popup
		W win = super.enterCreateNew();
		_setWinDimensions(win);
		
		// show it
		UI.getCurrent()
		  .addWindow((VaadinDetailEditFormWindowBase<V,?>)win);
		return win;
	}
	@Override @SuppressWarnings("unchecked")
	protected <W extends VaadinDetailEditForm<V>> W enterEdit(final V viewObj) {
		// create the popup
		W win = super.enterEdit(viewObj);
		_setWinDimensions(win);
		
		// show it
		UI.getCurrent()
		  .addWindow((VaadinDetailEditFormWindowBase<V,?>)win);
		return win;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private <W extends VaadinDetailEditForm<V>> void _setWinDimensions(final W win) {
		if (_winDimensions != null) {
			Unit theUnit = _winDimensionsUnit != null ? _winDimensionsUnit : Unit.PERCENTAGE;
			win.setWidth(_winDimensions.getWidth(),theUnit);
			win.setHeight(_winDimensions.getHeight(),theUnit);
		}
	}
}