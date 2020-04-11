package r01ui.base.components.grid;	

import com.vaadin.shared.Registration;
import com.vaadin.ui.AbstractSplitPanel.SplitPositionChangeListener;
import com.vaadin.ui.AbstractSplitPanel.SplitterClickListener;
import com.vaadin.ui.Component;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.VerticalSplitPanel;

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
			 // edit detail edit form factory (wraps the form with [accept] [cance] buttons
			 new Factory<VaadinDetailEditFormBase<V,F>>() {
					@Override
					public VaadinDetailEditFormBase<V, F> create() {
						return new VaadinDetailEditFormBase<V,F>(i18n,
					 								 			 formFactory.from(i18n),	// form
					 								 			 viewObjFactory) {			// view obj factory
				 			private static final long serialVersionUID = 442840240106223037L;
						};
					}
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
			  // edit detail edit form factory (wraps the form with [accept] [cance] buttons
			  new Factory<VaadinDetailEditFormBase<V,F>>() {
					@Override
					public VaadinDetailEditFormBase<V, F> create() {
						return new VaadinDetailEditFormBase<V,F>(i18n,
					 								  			 formFactory.from(i18n),	// form
					 								  			 viewObjFactory) {			// view obj factory
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
//	LAYOUT
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected Component _initComponentContent(final Label caption,
											  final HorizontalLayout lyButtons,
											  final Grid<V> grid) {
		// Header: 
		//		[caption]
		//		[Buttons]
		CssLayout lyHeader = new CssLayout(caption,
									 	   lyButtons);
		lyHeader.setWidthFull();
		
		// a vertical split panel:
		//		[     Grid    ]
		//		---------------
		//		[     Form    ]
		VerticalSplitPanel vsplit = new VerticalSplitPanel();
		vsplit.setFirstComponent(grid);
		vsplit.setResponsive(true);
		vsplit.setSplitPosition(30,Unit.PERCENTAGE);
		
		// layoyt
		VerticalLayout ly = new VerticalLayout(lyHeader,
									 		   vsplit);
		// ... ensure 100% height
		ly.setExpandRatio(lyHeader,0);
		ly.setExpandRatio(vsplit,100);
		ly.setWidthFull();
		ly.setHeightFull();
		ly.setMargin(false);
		
		
		return ly;
	}
	private <E extends VaadinDetailEditForm<V>> void _showEditForm(final E form) {
		VerticalSplitPanel gridAndForm = _splitPanel();
		if (gridAndForm.getSecondComponent() == null) gridAndForm.setSecondComponent(form);
		form.setVisible(true);
	}
	private VerticalSplitPanel _splitPanel() {
		VerticalLayout root = (VerticalLayout)this.getCompositionRoot();
		VerticalSplitPanel gridAndForm = (VerticalSplitPanel)root.getComponent(1);	// second component
		return gridAndForm;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void setSplitPosition(final float pos) {
		_splitPanel().setSplitPosition(pos);
	}
	public void setSplitPosition(final float pos,
								 final boolean reverse) {
		_splitPanel().setSplitPosition(pos,
									   reverse);
	}
	public void setSplitPosition(final float pos,final Unit unit,
								 final boolean reverse) {
		_splitPanel().setSplitPosition(pos, unit, reverse);
	}
	public void setSplitPosition(final float pos,final Unit unit) {
		_splitPanel().setSplitPosition(pos,unit);
	}
	public void setMaxSplitPosition(final float pos,final Unit unit) {
		_splitPanel().setMaxSplitPosition(pos,unit);
	}
	public void setMinSplitPosition(final float pos,final Unit unit) {
		_splitPanel().setMinSplitPosition(pos,unit);
	}
	public float getSplitPosition() {
		return _splitPanel().getSplitPosition();
	}
	public Unit getSplitPositionUnit() {
		return _splitPanel().getSplitPositionUnit();
	}
	public boolean isSplitPositionReversed() {
		return _splitPanel().isSplitPositionReversed();
	}
	public float getMinSplitPosition() {
		return _splitPanel().getMinSplitPosition();
	}
	public Unit getMinSplitPositionUnit() {
		return _splitPanel().getMinSplitPositionUnit();
	}
	public float getMaxSplitPosition() {
		return _splitPanel().getMaxSplitPosition();
	}
	public Unit getMaxSplitPositionUnit() {
		return _splitPanel().getMaxSplitPositionUnit();
	}
	public boolean isLocked() {
		return _splitPanel().isLocked();
	}
	public Registration addSplitterClickListener(final SplitterClickListener listener) {
		return _splitPanel().addSplitterClickListener(listener);
	}
	public Registration addSplitPositionChangeListener(final SplitPositionChangeListener listener) {
		return _splitPanel().addSplitPositionChangeListener(listener);
	}
}