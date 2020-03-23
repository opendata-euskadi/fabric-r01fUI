package r01ui.base.components.grid;	

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.google.common.collect.Lists;
import com.vaadin.data.HasDataProvider;
import com.vaadin.data.HasValue.ValueChangeListener;
import com.vaadin.data.ValueProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.DataProviderListener;
import com.vaadin.data.provider.GridSortOrder;
import com.vaadin.data.provider.GridSortOrderBuilder;
import com.vaadin.event.SortEvent.SortListener;
import com.vaadin.event.SortEvent.SortNotifier;
import com.vaadin.event.selection.SelectionListener;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.Registration;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.grid.ColumnResizeMode;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.shared.ui.grid.ScrollDestination;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.DescriptionGenerator;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.Column.NestedNullBehavior;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.StyleGenerator;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.components.grid.ColumnReorderListener;
import com.vaadin.ui.components.grid.ColumnResizeListener;
import com.vaadin.ui.components.grid.ColumnVisibilityChangeListener;
import com.vaadin.ui.components.grid.DetailsGenerator;
import com.vaadin.ui.components.grid.Editor;
import com.vaadin.ui.components.grid.FooterRow;
import com.vaadin.ui.components.grid.GridMultiSelect;
import com.vaadin.ui.components.grid.GridRowDragger;
import com.vaadin.ui.components.grid.GridSelectionModel;
import com.vaadin.ui.components.grid.GridSingleSelect;
import com.vaadin.ui.components.grid.HeaderRow;
import com.vaadin.ui.components.grid.ItemClickListener;
import com.vaadin.ui.renderers.AbstractRenderer;

import r01f.locale.I18NKey;
import r01f.patterns.Factory;
import r01f.ui.i18n.UII18NService;
import r01f.ui.subscriber.UISubscriber;
import r01f.ui.vaadin.view.VaadinViewFactories.VaadinViewFactory;
import r01f.ui.viewobject.UIViewObject;
import r01f.util.types.collections.CollectionUtils;
import r01ui.base.components.VaadinListDataProviders;
import r01ui.base.components.form.VaadinDetailEditFormWindowBase;
import r01ui.base.components.form.VaadinDetailForm;
import r01ui.base.components.form.VaadinFormBindings.VaadinFormHasVaadinUIBinder;
import r01ui.base.components.window.VaadinProceedGateDialogWindow;

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
 * Usage:
 * <pre class='brush:java'>
 * 		// Given a [model object] like:
 * 		@Accessors(prefix="_")
 * 		public class MyObj {
 * 			@Getter @Setter private _txt; 
 * 		}
 * 
 * 		// and the corresponding [view object]
 * 		public class MyViewObj 
 * 		     extends UIViewObjectWrappedBase<MyObj> {
 *			public String getTxt() {
 *				return this.getWrappedModelObject()
 *						   .getTxt();
 *			}
 *			public void setTxt(final String txt) {
 *				this.getWrappedModelObject()
 *					.setTxt(txt);
 *			}
 * 		}
 * 
 * 		// the edit [form]:
 * 		@Accessors(prefix="_")
 * 		public class MyForm 
 *	 	     extends VerticalLayout
 *		  implements VaadinDetailForm<MyViewObj>,
 * 			 		 VaadinFormHasVaadinUIBinder<MyViewObj> {
 * 			// UI binder
 * 			@Getter private final Binder<MyViewObj> _vaadinUIBinder = new Binder<>(MyViewObj.class);
 * 
 *			@VaadinViewField(bindToViewObjectFieldNamed="txt")
 *			@VaadinViewComponentLabels(captionI18NKey="txt")
 *			private final TextField _txt = new TextField("Text");
 *
 *			public MyForm(final UII18NService i18n) {
 * 				////////// Labels
 *				VaadinViews.using(i18n)
 *				   		   .setI18NLabelsOf(this);
 *				////////// Bind: automatic binding using using @VaadinViewField annotation of view fields
 *				VaadinViews.using(_vaadinUIBinder,i18n)
 *				   		   .bindComponentsOf(this)
 *				   		   .toViewObjectOfType(MyViewObj.class);
 *			}
 *			@Override
 *			public void bindUIControlsTo(final MyViewObj viewObj) {
 *				_vaadinUIBinder.setBean(viewObj);
 *			}
 *			@Override
 *			public void readUIControlsFrom(final MyViewObj viewObj) {
 *				_vaadinUIBinder.readBean(viewObj);
 *			}
 *			@Override
 *			public MyViewObj getViewObject() {
 *				return _vaadinUIBinder.getBean();
 *			}
 *			@Override
 *			public boolean writeIfValidFromUIControlsTo(final MyViewObj viewObj) {
 *				return _vaadinUIBinder.writeBeanIfValid(viewObj);
 *			}
 *		}
 * 
 * 		// And finally the grid
 *		public class MyCRUDGrid
 *			 extends VaadinCRUDGridBase<MyViewObj,
 *			 							MyForm> {
 *		
 *			public MyCRUDGrid(final UII18NService i18n) {
 *				super(i18n,
 *					  // form factory
 *					  MyForm::new,
 *					  // view obj factory
 *					  MyViewObj.class,() -> new MyViewObj(new MyObj()),
 *					  // grid cols
 *					  "txt");
 *			}
 *		}
 * </pre>
 * @param <V>
 * @param <F>
 */
public abstract class VaadinCRUDGridBase<// The view object
										 V extends UIViewObject,
										 // the edit form
										 F extends VaadinDetailForm<V> 
											 	 & VaadinFormHasVaadinUIBinder<V> 
											 	 & Component>
	 		  extends Composite 
	 	   implements HasDataProvider<V>, 
  			 		  SortNotifier<GridSortOrder<V>>,
  			 		  VaadinListView {

	private static final long serialVersionUID = -235814350977119673L;
/////////////////////////////////////////////////////////////////////////////////////////
//	SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	protected final transient UII18NService _i18n;
	
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Factory<V> _viewObjFactory;

/////////////////////////////////////////////////////////////////////////////////////////
//	UI CONTROLS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Grid<V> _grid;
	
	private final Label _lblCaption;
	
	private final Button _btnCreate;
	private final Button _btnEdit;
	private final Button _btnRemove;
	private final Button _btnUp;
	private final Button _btnDown;
	
	private final VaadinDetailEditFormWindowBase<V,F> _formPopUp;
	
	
/////////////////////////////////////////////////////////////////////////////////////////
//	UI SUBSCRIBERS
/////////////////////////////////////////////////////////////////////////////////////////	
	private UISubscriber<V> _onItemCreatedSubscriber;
	private UISubscriber<V> _onItemEditedSubscriber;
	private UISubscriber<V> _onItemDeletedSubscriber;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinCRUDGridBase(final UII18NService i18n,
							  final VaadinViewFactory<F> formFactory,
							  final Factory<V> viewObjFactory) {
		this(i18n,
			 // edit form and popup factories
			 formFactory,
			 () -> new VaadinDetailEditFormWindowBase<V,F>(i18n,formFactory.from(i18n)) {
							private static final long serialVersionUID = -5628170580725614674L;
				   },
			 // view obj factory
			 viewObjFactory);
	}
	public <W extends VaadinDetailEditFormWindowBase<V,F>> VaadinCRUDGridBase(final UII18NService i18n,
																			  final VaadinViewFactory<F> formFactory,
																			  final Factory<W> popUpFactory,
																			  final Factory<V> viewObjFactory) {
		this(i18n,
			 // edit form and popup factories
			 formFactory,
			 popUpFactory,
			 // view obj factory
			 viewObjFactory,
			 // grid columns provider
			 (VaadinGridColumnProvider<V>)null);		// no grid columns by default
	}
	public VaadinCRUDGridBase(final UII18NService i18n,
							  final VaadinViewFactory<F> formFactory,
							  final Class<V> viewObjType,final Factory<V> viewObjFactory,
					   	   	  final String... viewObjPropertyNames) {	
		this(i18n,
			 // edit form and popup factories
			 formFactory,
			 () -> new VaadinDetailEditFormWindowBase<V,F>(i18n,formFactory.from(i18n)) {
							private static final long serialVersionUID = -5628170580725614674L;
				   },
			 // view object factory
			 viewObjFactory,
			 // grid cols factory
			 new Grid<>(viewObjType),
			 VaadinGridColumnProvider.createColumnProviderUsingViewObjectPropertiesWithNames(viewObjPropertyNames));	// grid columns are the property names
	}
	public <W extends VaadinDetailEditFormWindowBase<V,F>> VaadinCRUDGridBase(final UII18NService i18n,
							  												  final VaadinViewFactory<F> formFactory,
							  												  final Factory<W> popUpFactory,
							  												  final Factory<V> viewObjFactory,
							  												  final VaadinGridColumnProvider<V> gridColsProvider) {
		this(i18n,
			 // edit form and popup factories
			 formFactory,popUpFactory,
			 // view object factory
			 viewObjFactory,
			 // grid and grid cols provider
			 new Grid<>(),
			 gridColsProvider);
	}
	private <W extends VaadinDetailEditFormWindowBase<V,F>> VaadinCRUDGridBase(final UII18NService i18n,
							  												   final VaadinViewFactory<F> formFactory,
							  												   final Factory<W> popUpFactory,
							  												   final Factory<V> viewObjFactory,
							  												   final Grid<V> grid,
							  												   final VaadinGridColumnProvider<V> gridColsProvider) {
		_i18n = i18n;
		_viewObjFactory = viewObjFactory;
		
		////////// Grid
		_grid = grid;
		_configureGrid();
		
		if (gridColsProvider != null) gridColsProvider.provideColumnsFor(_grid);		// provide columns for the grid
		
		////////// Caption
		_lblCaption = new Label();
		
		////////// Buttons
		_btnCreate = new Button(VaadinIcons.PLUS_SQUARE_LEFT_O);
		_btnEdit = new Button(VaadinIcons.EDIT);
		_btnRemove = new Button(VaadinIcons.TRASH);
		_btnUp = new Button(VaadinIcons.ARROW_UP);
		_btnDown = new Button(VaadinIcons.ARROW_DOWN);
		// all except the [create] button are disabled by default
		_resetButtonStatus();
		// behavior
		_setButtonsBehavior();
		// layout
		CssLayout lyButtons1 = new CssLayout(_btnEdit,_btnRemove);
		CssLayout lyButtons2 = new CssLayout(_btnUp,_btnDown);
		HorizontalLayout lyButtons = new HorizontalLayout(_lblCaption,_btnCreate,lyButtons1,lyButtons2);
		lyButtons.setWidthFull();
		lyButtons.setComponentAlignment(_lblCaption,Alignment.MIDDLE_LEFT);
		lyButtons.setComponentAlignment(_btnCreate,Alignment.MIDDLE_LEFT);
		lyButtons.setComponentAlignment(lyButtons2,Alignment.BOTTOM_RIGHT);
		lyButtons.setComponentAlignment(lyButtons1,Alignment.BOTTOM_RIGHT);
		lyButtons.setExpandRatio(_lblCaption,3);
		lyButtons.setExpandRatio(_btnCreate,1);
		lyButtons.setExpandRatio(lyButtons2,1);
		lyButtons.setExpandRatio(lyButtons1,1);
		
		////////// Form
		_formPopUp = popUpFactory.create();
		
		////////// Layout
		VerticalLayout vly = new VerticalLayout(lyButtons,
												_grid);
		vly.setMargin(false);
		vly.setSizeFull();
		this.setCompositionRoot(vly);
		
		////////// Initial empty data
		this.setItems(Lists.newArrayList());
	}
	/**
	 * Override this method to further configure the grid
	 * @param grid
	 */
	protected void _configureGrid() {
		// drag & drop
		GridRowDragger<V> gridRowDragger = new GridRowDragger<>(_grid); 		//	drag and drop order
		gridRowDragger.getGridDropTarget()
					  .addGridDropListener(gridDropEvent -> _setUpDownButtonsStatusForSelectedItem());
		
		// sizing
		_grid.setRowHeight(50.0);
		_grid.setWidthFull();
		_grid.setResponsive(true);
		
		// no column sortable
		_grid.getColumns()
			 .forEach(gridCol -> gridCol.setSortable(false));
		// when selecting a row
		_grid.addSelectionListener(selEvent -> {
										// Set the [edit] | [remove] | [up] | [down] buttons status
										Set<V> selectedItems = selEvent.getAllSelectedItems();
										if (CollectionUtils.isNullOrEmpty(selectedItems)
										 || selectedItems.size() > 1) {
											_btnEdit.setEnabled(false);
											_btnRemove.setEnabled(false);
											_btnUp.setEnabled(false);
											_btnDown.setEnabled(true);
										} 
										else if (selectedItems.size() == 1) {
											_btnEdit.setEnabled(true);
											_btnRemove.setEnabled(true);
											_setUpDownButtonsStatusForSelectedItem();
										}
								   });
	}
	private void _resetButtonStatus() {
		_btnCreate.setEnabled(true);
		_btnEdit.setEnabled(false);
		_btnRemove.setEnabled(false);
		_btnUp.setEnabled(false);
		_btnDown.setEnabled(false);
	}
	private void _setUpDownButtonsStatusForSelectedItem() {
		V selectedItem = _grid.asSingleSelect()
			   			  	  .getSelectedItem()
			   			  	  .orElse(null);
		_btnUp.setEnabled(VaadinListDataProviders.collectionBackedOf(this)
												 .canMoveItemUp(selectedItem));
		_btnDown.setEnabled(VaadinListDataProviders.collectionBackedOf(this)
												   .canMoveItemDown(selectedItem));
	}
	protected void _setButtonsBehavior() {
		// Create button
		_btnCreate.addClickListener(clickEvent -> this.showPopUpForCreateNew());
		
		// Edit button
		_btnEdit.addClickListener(clickEvent -> {
										V selectedViewObj = _grid.asSingleSelect().getSelectedItem()
																			   	  .orElse(null);
										if (selectedViewObj != null) this.showPopUpForEdit(selectedViewObj);
								  });
		// Delete button 
		_btnRemove.addClickListener(clickEvent -> {
										V selectedViewObj = _grid.asSingleSelect().getSelectedItem()
																			   	  .orElse(null);
										if (selectedViewObj != null) this.showPopUpForRemove(selectedViewObj);
									});
		// Up button
		_btnUp.addClickListener(clickEvent -> {
									V selectedViewObj = _grid.asSingleSelect().getSelectedItem()
																		   	  .orElse(null);
									if (selectedViewObj != null) { 
										VaadinListDataProviders.collectionBackedOf(_grid)
														   	   .moveItemUp(selectedViewObj);
										_setUpDownButtonsStatusForSelectedItem();
									}
								});
		// Down button
		_btnDown.addClickListener(clickEvent -> {
									V selectedViewObj = _grid.asSingleSelect().getSelectedItem()
																		   	  .orElse(null);
									if (selectedViewObj != null) { 
										VaadinListDataProviders.collectionBackedOf(_grid)
 														   	   .moveItemDown(selectedViewObj);
										_setUpDownButtonsStatusForSelectedItem();
									}
								});
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SUBSCRIBERS
/////////////////////////////////////////////////////////////////////////////////////////
	public void setOnCreateItemSubscriber(final UISubscriber<V> subscriber) {
		_onItemCreatedSubscriber = subscriber;
	}
	public void setOnEditedItemSubscriber(final UISubscriber<V> subscriber) {
		_onItemEditedSubscriber = subscriber;
	}
	public void setOnDeletedItemSubscriber(final UISubscriber<V> subscriber) {
		_onItemDeletedSubscriber = subscriber;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  Show ADD, REMOVE, EDIT modal window
/////////////////////////////////////////////////////////////////////////////////////////
	protected void showPopUpForCreateNew() {
			_formPopUp.forCreating(_viewObjFactory,
								   // What happens when the pop up is closed after creating a new [view object]
								   // ...add the created obj and refresh
								   createdViewObj -> {
					   					VaadinListDataProviders.collectionBackedOf(_grid)
					   										   .addNewItem(createdViewObj);
										this.setHeightByRows(VaadinListDataProviders.collectionBackedOf(_grid)
																					.getUnderlyingItemsCollectionSize());
										_setUpDownButtonsStatusForSelectedItem();	// maybe there existed a selected item... now there exists more than a single item and buttons need to be updated
										
										// tell the outside world
										if (_onItemCreatedSubscriber != null) _onItemCreatedSubscriber.onSuccess(createdViewObj);
								   });
			UI.getCurrent()
			  .addWindow(_formPopUp);
	}
	protected void showPopUpForEdit(final V viewObj) {
		_formPopUp.forEditing(viewObj,
							  // What happens when the pop up is closed after editing the [view object]
							  // ... update the edited obj and refresh
							  savedViewObj -> {
								  VaadinListDataProviders.collectionBackedOf(_grid)
								  						 .refreshItem(viewObj);
								  
								  // tell the outside world
								  if (_onItemEditedSubscriber != null) _onItemEditedSubscriber.onSuccess(savedViewObj);
							  });
			UI.getCurrent()
			  .addWindow(_formPopUp);
	}
	protected void showPopUpForEditSelected() {
		V selectedViewObj = _grid.asSingleSelect()
								 .getSelectedItem()
								 .orElse(null);
		if (selectedViewObj != null) this.showPopUpForEdit(selectedViewObj);
	}
	protected void showPopUpForRemove(final V viewObj) {
		VaadinProceedGateDialogWindow proceedGatewayPopUp = new VaadinProceedGateDialogWindow(_i18n,
																			   				  I18NKey.forId("delete"),
																			   				  I18NKey.forId("grid.crud.delete.message"),
																							  // what happens when the user allows the panel disposal
																							  () -> { 
																								  /// remove the item
																								  VaadinListDataProviders.collectionBackedOf(_grid)
																								  						 .removeItem(viewObj);
																								  // now there's no selected item
																								 _resetButtonStatus();
																								 
																								 // tell the outside world
																								 if (_onItemDeletedSubscriber != null) _onItemDeletedSubscriber.onSuccess(viewObj);
																							  });
		UI.getCurrent()
		  .addWindow(proceedGatewayPopUp);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  UI
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	protected VerticalLayout getCompositionRoot() {
		return (VerticalLayout)super.getCompositionRoot();
	}
	@Override
	public void setEnabled(final boolean enabled) {
		_grid.setEnabled(enabled);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LISTENERS
/////////////////////////////////////////////////////////////////////////////////////////	
	public Registration addSelectionListener(final SelectionListener<V> listener) throws UnsupportedOperationException {
		return _grid.addSelectionListener(listener);
	}
	public void addValueChangeListener(final ValueChangeListener<V> listener) {
		_grid.asSingleSelect()
			 .addValueChangeListener(listener);
	}
	public Registration addColumnReorderListener(final ColumnReorderListener listener) {
		return _grid.addColumnReorderListener(listener);
	}
	public Registration addColumnResizeListener(final ColumnResizeListener listener) {
		return _grid.addColumnResizeListener(listener);
	}
	public Registration addItemClickListener(final ItemClickListener<? super V> listener) {
		return _grid.addItemClickListener(listener);
	}
	public Registration addColumnVisibilityChangeListener(final ColumnVisibilityChangeListener listener) {
		return _grid.addColumnVisibilityChangeListener(listener);
	}
	@Override
	public Registration addSortListener(final SortListener<GridSortOrder<V>> listener) {
		return _grid.addSortListener(listener);
	}
	public void setaDataProviderListener(final DataProviderListener<V> listener) {
		_grid.getDataProvider().addDataProviderListener(listener);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DATA
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void refreshList() {
		VaadinListDataProviders.collectionBackedOf(_grid)
							   .refreshAll();
	}
	@Override @SuppressWarnings("unchecked")
	public void setItems(final V... items) {
		_grid.setItems(items);
		_resetButtonStatus();	// all buttons disabled except the [create] button
	}
	@Override
	public void setItems(final Stream<V> streamOfItems) {
		_grid.setItems(streamOfItems);
		_resetButtonStatus();	// all buttons disabled except the [create] button
	}
	@Override
	public void setItems(final Collection<V> items) {
		Collection<V> theItems = items != null ? items : Lists.newArrayList();
		_grid.setItems(theItems);
		this.setHeightByRows(theItems.size());
		_resetButtonStatus();	// all buttons disabled except the [create] button
	}
	@Override
	public void setDataProvider(final DataProvider<V,?> dataProvider) {
		_grid.setDataProvider(dataProvider);
	}
	@Override
	public DataProvider<V,?> getDataProvider() {
		return _grid.getDataProvider();
	}
	public Collection<V> getItems() {
		return VaadinListDataProviders.collectionBackedOf(_grid)
									  .getUnderlyingItemsCollection();
	}
	public V getSelectedItem() {
		return _grid.asSingleSelect()
					.getValue();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ENABLE & VISIBLE
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public boolean isEnabled() {
		return _grid.isEnabled();
	}
	@Override
	public boolean isVisible() {
		return _grid.isVisible();
	}
	@Override
	public void setVisible(final boolean visible) {
		_grid.setVisible(visible);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	COLUMNS
/////////////////////////////////////////////////////////////////////////////////////////	
	public void setDetailsGenerator(final DetailsGenerator<V> generator) {
		_grid.setDetailsGenerator(generator);
	}
	public void setDetailsVisible(final V item,final boolean visible) {
		_grid.setDetailsVisible(item,visible);
	}
	public boolean isDetailsVisible(final V item) {
		return _grid.isDetailsVisible(item);
	}
	public <T> void fireColumnVisibilityChangeEvent(final Column<V,T> column,
													final boolean hidden, 
													final boolean userOriginated) {
		_grid.fireColumnVisibilityChangeEvent(column, 
											  hidden,
											  userOriginated);
	}
	public Column<V,?> getColumn(final String columnId) {
		return _grid.getColumn(columnId);
	}
	public List<Column<V,?>> getColumns() {
		return _grid.getColumns();
	}
	public <C extends Component> Column<V,C> addComponentColumn(final ValueProvider<V,C> componentProvider) {
		return _grid.addComponentColumn(componentProvider);
	}
	public void setColumnOrder(final String... columnIds) {
		_grid.setColumnOrder(columnIds);
	}
	public void setColumns(final String... columnIds) {
		_grid.setColumns(columnIds);
	}
	public void setColumnOrder(final Column<V, ?>... columns) {
		_grid.setColumnOrder(columns);
	}
	public Column<V,?> addColumn(final String propertyName) {
		return _grid.addColumn(propertyName);
	}
	public Column<V,?> addColumn(final String propertyName, 
								 final AbstractRenderer<? super V,?> renderer) {
		return _grid.addColumn(propertyName, 
							   renderer);
	}
	public Column<V,?> addColumn(final String propertyName, 
								 final AbstractRenderer<? super V, ?> renderer,
								 final NestedNullBehavior nestedNullBehavior) {
		return _grid.addColumn(propertyName,
							   renderer, 
							   nestedNullBehavior);
	}
	public <T> Column<V,T> addColumn(final ValueProvider<V,T> valueProvider) {
		return _grid.addColumn(valueProvider);
	}
	public <T> Column<V,T> addColumn(final ValueProvider<V,T> valueProvider,
									 final AbstractRenderer<? super V,? super T> renderer) {
		return _grid.addColumn(valueProvider, 
							   renderer);
	}
	public <T> Column<V,T> addColumn(final ValueProvider<V,T> valueProvider,
									 final ValueProvider<T,String> presentationProvider) {
		return _grid.addColumn(valueProvider, 
							   presentationProvider);
	}
	public <T,P> Column<V,T> addColumn(final ValueProvider<V,T> valueProvider,
									   final ValueProvider<T,P> presentationProvider,
									   final AbstractRenderer<? super V,? super P> renderer) {
		return _grid.addColumn(valueProvider,
							   presentationProvider,
							   renderer);
	}
	public void removeColumn(final Column<V,?> column) {
		_grid.removeColumn(column);
	}
	public void removeColumn(final String columnId) {
		_grid.removeColumn(columnId);
	}
	public void removeAllColumns() {
		_grid.removeAllColumns();
	}	
	public void setFrozenColumnCount(final int numberOfColumns) {
		_grid.setFrozenColumnCount(numberOfColumns);
	}
	public int getFrozenColumnCount() {
		return _grid.getFrozenColumnCount();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SIZING
/////////////////////////////////////////////////////////////////////////////////////////
	public void setHeightByRows(final double rows) {
		_grid.setHeightByRows(rows > 0 ? rows : 1);
	}
	public void setColumnResizeMode(final ColumnResizeMode mode) {
		_grid.setColumnResizeMode(mode);
	}
	public ColumnResizeMode getColumnResizeMode() {
		return _grid.getColumnResizeMode();
	}
	public void recalculateColumnWidths() {
		_grid.recalculateColumnWidths();
	}
	public double getHeightByRows() {
		return _grid.getHeightByRows();
	}
	public void setHeightMode(final HeightMode heightMode) {
		_grid.setHeightMode(heightMode);
	}
	public HeightMode getHeightMode() {
		return _grid.getHeightMode();
	}
	public void setRowHeight(final double rowHeight) {
		_grid.setRowHeight(rowHeight);
	}
	public void setBodyRowHeight(final double rowHeight) {
		_grid.setBodyRowHeight(rowHeight);
	}
	public void setHeaderRowHeight(final double rowHeight) {
		_grid.setHeaderRowHeight(rowHeight);
	}
	public void setFooterRowHeight(final double rowHeight) {
		_grid.setFooterRowHeight(rowHeight);
	}
	public double getBodyRowHeight() {
		return _grid.getBodyRowHeight();
	}
	public double getHeaderRowHeight() {
		return _grid.getHeaderRowHeight();
	}
	public double getFooterRowHeight() {
		return _grid.getFooterRowHeight();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	STYLES
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean isColumnReorderingAllowed() {
		return _grid.isColumnReorderingAllowed();
	}
	public void setColumnReorderingAllowed(final boolean columnReorderingAllowed) {
		_grid.setColumnReorderingAllowed(columnReorderingAllowed);
	}
	public void setStyleGenerator(final StyleGenerator<V> styleGenerator) {
		_grid.setStyleGenerator(styleGenerator);
	}
	public StyleGenerator<V> getStyleGenerator() {
		return _grid.getStyleGenerator();
	}
	public void setDescriptionGenerator(final DescriptionGenerator<V> descriptionGenerator) {
		_grid.setDescriptionGenerator(descriptionGenerator);
	}
	public void setDescriptionGenerator(final DescriptionGenerator<V> descriptionGenerator, 
									    final ContentMode contentMode) {
		_grid.setDescriptionGenerator(descriptionGenerator, contentMode);
	}
	public DescriptionGenerator<V> getDescriptionGenerator() {
		return _grid.getDescriptionGenerator();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	HEADER & FOOTER
/////////////////////////////////////////////////////////////////////////////////////////
	public void setHeaderVisible(final boolean headerVisible) {
		_grid.setHeaderVisible(headerVisible);
	}
	public HeaderRow getHeaderRow(final int index) {
		return _grid.getHeaderRow(index);
	}
	public int getHeaderRowCount() {
		return _grid.getHeaderRowCount();
	}
	public HeaderRow addHeaderRowAt(final int index) {
		return _grid.addHeaderRowAt(index);
	}
	public HeaderRow appendHeaderRow() {
		return _grid.appendHeaderRow();
	}
	public HeaderRow prependHeaderRow() {
		return _grid.prependHeaderRow();
	}
	public void removeHeaderRow(final HeaderRow row) {
		_grid.removeHeaderRow(row);
	}
	public void removeHeaderRow(final int index) {
		_grid.removeHeaderRow(index);
	}
	public boolean isHeaderVisible() {
		return _grid.isHeaderVisible();
	}
	public HeaderRow getDefaultHeaderRow() {
		return _grid.getDefaultHeaderRow();
	}
	public void setDefaultHeaderRow(final HeaderRow row) {
		_grid.setDefaultHeaderRow(row);
	}
	public FooterRow getFooterRow(final int index) {
		return _grid.getFooterRow(index);
	}
	public int getFooterRowCount() {
		return _grid.getFooterRowCount();
	}
	public FooterRow addFooterRowAt(final int index) {
		return _grid.addFooterRowAt(index);
	}
	public FooterRow appendFooterRow() {
		return _grid.appendFooterRow();
	}
	public FooterRow prependFooterRow() {
		return _grid.prependFooterRow();
	}
	public void removeFooterRow(final FooterRow row) {
		_grid.removeFooterRow(row);
	}
	public void removeFooterRow(final int index) {
		_grid.removeFooterRow(index);
	}
	public void setFooterVisible(final boolean footerVisible) {
		_grid.setFooterVisible(footerVisible);
	}
	public boolean isFooterVisible() {
		return _grid.isFooterVisible();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SELECTION
/////////////////////////////////////////////////////////////////////////////////////////	
	public GridSelectionModel<V> getSelectionModel() {
		return _grid.getSelectionModel();
	}
	public GridSingleSelect<V> asSingleSelect() {
		return _grid.asSingleSelect();
	}
	public Editor<V> getEditor() {
		return _grid.getEditor();
	}
	public GridMultiSelect<V> asMultiSelect() {
		return _grid.asMultiSelect();
	}
	public GridSelectionModel<V> setSelectionMode(final SelectionMode selectionMode) {
		return _grid.setSelectionMode(selectionMode);
	}
	public Set<V> getSelectedItems() {
		return _grid.getSelectedItems();
	}
	public void select(final V item) {
		_grid.select(item);
	}
	public void deselect(final V item) {
		_grid.deselect(item);
	}
	public void deselectAll() {
		_grid.deselectAll();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SORT
/////////////////////////////////////////////////////////////////////////////////////////	
	public void sort(final Column<V, ?> column) {
		_grid.sort(column);
	}
	public void sort(final Column<V, ?> column, final SortDirection direction) {
		_grid.sort(column, direction);
	}
	public void sort(final String columnId) {
		_grid.sort(columnId);
	}
	public void sort(final String columnId, final SortDirection direction) {
		_grid.sort(columnId, direction);
	}
	public void clearSortOrder() {
		_grid.clearSortOrder();
	}
	public void setSortOrder(final List<GridSortOrder<V>> order) {
		_grid.setSortOrder(order);
	}
	public void setSortOrder(final GridSortOrderBuilder<V> builder) {
		_grid.setSortOrder(builder);
	}
	public List<GridSortOrder<V>> getSortOrder() {
		return _grid.getSortOrder();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public void scrollTo(final int row) throws IllegalArgumentException {
		_grid.scrollTo(row);
	}
	public void scrollTo(final int row, final ScrollDestination destination) {
		_grid.scrollTo(row, destination);
	}
	public void scrollToStart() {
		_grid.scrollToStart();
	}
	public void scrollToEnd() {
		_grid.scrollToEnd();
	}
}