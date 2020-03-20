package r01ui.base.components.collectionmanage;

import java.util.Collection;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.locale.I18NKey;
import r01f.locale.Language;
import r01f.patterns.Factory;
import r01f.patterns.FactoryFrom;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.ui.viewobject.UIViewObject;
import r01f.util.types.collections.CollectionUtils;
import r01ui.base.components.collectionmanage.VaadinCollectionManageComponent.HasVaadinManagedCollectionItemSummaryChangeEventListener;
import r01ui.base.components.collectionmanage.VaadinCollectionManageComponent.VaadinCollectionItemSummaryComponent;
import r01ui.base.components.form.VaadinFormBindings.VaadinFormHasVaadinUIBinder;

/**
 * A [not normalized relations] grid component like:
 * <pre>
 * 		++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * 		+ [add] [remove]                                             +
 * 		+------------------------------------------------------------+
 * 		+ [x] [summary] 				[edit] [delete] [up] [down]	 + 
 * 		+ [ ] [summary]					[edit] [delete] [up] [down]	 +
 * 		+ [ ] [summary]  				[edit] [delete] [up] [down]  +
 * 		++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 */
public class VaadinCollectionManageComponent<// the view object type
											 V extends UIViewObject,
											 // the editor component
											 E extends Component 
											 		 & VaadinFormHasVaadinUIBinder<V>
													 & HasVaadinManagedCollectionItemSummaryChangeEventListener<SV>,
											 // the summary component
											 SV,
											 S extends VaadinCollectionItemSummaryComponent<SV>>
	 extends Composite
  implements VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = -5687359002320663669L;

/////////////////////////////////////////////////////////////////////////////////////////
//	SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	private final transient UII18NService _i18n;
	private final Factory<V> _viewObjFactory;
	private final FactoryFrom<UII18NService,E> _editComponentFactory;
	private final FactoryFrom<UII18NService,S> _summaryComponentFactory;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	UI FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Label _lblTooltip;
	
	@SuppressWarnings("unused")
	private I18NKey _tooltipI18NKey;
	
	private final Button _btnAdd;
	
	private final VerticalLayout _vlyGrid;
	
	private Collection<VaadinCollectionItemAddedOrRemovedEventListener<V>> _itemAddedOrRemovedEventListeners;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinCollectionManageComponent(final UII18NService i18n,
								  		   final Factory<V> viewObjFactory,
								  		   final FactoryFrom<UII18NService,E> editComponentFactory,
								  		   final FactoryFrom<UII18NService,S> summaryComponentFactory) {
		_i18n = i18n;
		_viewObjFactory = viewObjFactory;
		_editComponentFactory = editComponentFactory;
		_summaryComponentFactory = summaryComponentFactory;
		
		////////// Components
		// Grid
		_vlyGrid = new VerticalLayout();
		_vlyGrid.setSizeFull();
		_vlyGrid.setMargin(false);
		_vlyGrid.setSpacing(false);
		
		// Tooltip
		_lblTooltip = new Label();
		_lblTooltip.setVisible(false);
		
		// add and remove buttons
		_btnAdd = new Button(VaadinIcons.PLUS_CIRCLE);
		_btnAdd.setDescription(i18n.getMessage("add"));
		_btnAdd.addStyleName(ValoTheme.BUTTON_QUIET);
		
		////////// Layout
		// Buttons: 	[add]
		HorizontalLayout hlyAddRemoveButtons = new HorizontalLayout(_btnAdd);
		hlyAddRemoveButtons.setComponentAlignment(_btnAdd,Alignment.MIDDLE_LEFT);
		hlyAddRemoveButtons.setSizeFull();
		hlyAddRemoveButtons.setSpacing(false);
		hlyAddRemoveButtons.setMargin(false);
		
		
		// Vertical layout
		//		[Buttons]
		//		[ Grid  ]
		VerticalLayout vly = new VerticalLayout(_lblTooltip,
												hlyAddRemoveButtons,
												_vlyGrid);
		vly.setMargin(false);
		vly.setSpacing(false);
		vly.setSizeFull();
		
		this.setCompositionRoot(vly);
		
		////////// Behavior
		_setBehavior();
	}
	private void _setBehavior() {
		// create a new [rel]
		_btnAdd.addClickListener(clickEvent -> {
										// add a new obj
										V viewObj = _viewObjFactory.create();
										
										VaadinCollectionManageRowComponent newRow = this.add(viewObj);
										newRow.setNew(true);
										
										// set the move buttons status
										int index = _vlyGrid.getComponentIndex(newRow);
										_setUpDownButtonsStatus(index);
										
										// scroll into view
										UI.getCurrent().scrollIntoView(newRow);
									 });
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PUBLIC METHODS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public void addItemAdditionOrRemovalListener(final VaadinCollectionItemAddedOrRemovedEventListener<V> listener) {
		if (listener == null) throw new IllegalArgumentException("item added or removed event listner cannot be null!");
		if (_itemAddedOrRemovedEventListeners == null) _itemAddedOrRemovedEventListeners = Lists.newArrayList();
		_itemAddedOrRemovedEventListeners.add(listener);
	}
	public void addAll(final Collection<V> viewObjs) {
		_vlyGrid.removeAllComponents();
		if (CollectionUtils.hasData(viewObjs)) viewObjs.stream()
				  									   .forEach(viewObj -> this.add(viewObj));
	}
	public VaadinCollectionManageRowComponent add(final V viewObj) {
		// store the objs BEFORE adding
		Collection<V> viewObjsBefore = this.getAll();
		
		// add 
		VaadinCollectionManageRowComponent editRow = new VaadinCollectionManageRowComponent();
		// edit
		editRow.edit(viewObj);
		
		// layout
		_vlyGrid.addComponent(editRow);
		
		// set the move buttons status
		int index = _vlyGrid.getComponentIndex(editRow);
		_setUpDownButtonsStatus(index);
		
		// raise an event
		if (CollectionUtils.hasData(_itemAddedOrRemovedEventListeners)) {
			VaadinCollectionItemAddedOrRemovedEvent<V> event = new VaadinCollectionItemAddedOrRemovedEvent<>(VaadinCollectionItemOperation.ADD, // add
																											 viewObj,
																											 viewObjsBefore);
			_itemAddedOrRemovedEventListeners.stream()
											 .forEach(listener -> listener.onItemAddedOrRemoved(event));
		}
		// return
		return editRow;
	}
	@SuppressWarnings("unchecked")
	public Iterator<VaadinCollectionManageRowComponent> getRowsIterator() {
		return Iterators.transform(_vlyGrid.iterator(), 
								   comp -> (VaadinCollectionManageRowComponent)comp);
	}
	@SuppressWarnings("unchecked")
	public Collection<V> getAll() {
		Collection<V> outViewObjs = Lists.newArrayList();
		_vlyGrid.iterator()
				.forEachRemaining(comp -> {
										// every row is like: [x] [DishEdit]
										VaadinCollectionManageRowComponent editRow = (VaadinCollectionManageRowComponent)comp;
										V viewRel = editRow.getViewObject();
										outViewObjs.add(viewRel);
								  });
		return outViewObjs;
	}
	public void setTooltip(final I18NKey tooltipKey) {
		_tooltipI18NKey = tooltipKey;
		_lblTooltip.setValue(_i18n.getMessage(tooltipKey));
		_lblTooltip.setVisible(true);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	private void _rowExpanded(final VaadinCollectionManageRowComponent expandedRowComp) {
		// collapse any other expanded row
		_rowStream().forEach(rowComp -> {
						 		if (rowComp != expandedRowComp) rowComp.collapse();
					 		 });
	}
	@SuppressWarnings("unchecked")
	private VaadinCollectionManageRowComponent _rowAt(final int index) {
		if (index < 0 || index > _vlyGrid.getComponentCount() - 1) throw new IllegalArgumentException("No row at " + index + " (max = " + _vlyGrid.getComponentCount() + ")");
		return (VaadinCollectionManageRowComponent)_vlyGrid.getComponent(index);
	}
	private void _setUpDownButtonsStatus(final int index) {
		VaadinCollectionManageRowComponent row = _rowAt(index);
		row.setMoveButtonsStatusIfRowAt(index);
		
		// button status on the displaced row (if any) must be set
		if (_vlyGrid.getComponentCount() > 1) {
			int otherIndex = index == 0 ? index + 1
										: index == (_vlyGrid.getComponentCount() - 1)
												? index - 1
												: -1;
			VaadinCollectionManageRowComponent otherRow = otherIndex >= 0 ? _rowAt(otherIndex)
																		  : null;
			if (otherRow != null) otherRow.setMoveButtonsStatusIfRowAt(otherIndex);
		}
	}
	private Stream<VaadinCollectionManageRowComponent> _rowStream() {
		Iterable<VaadinCollectionManageRowComponent> rowsIterable = () -> this.getRowsIterator();
		return StreamSupport.stream(rowsIterable.spliterator(), 
							 		false);	// parallel
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private class VaadinCollectionManageRowComponent 
		  extends Composite
	   implements VaadinViewI18NMessagesCanBeUpdated {
		private static final long serialVersionUID = 8093451147953767189L;
		
		private final S _summary;
		private final Button _btnEdit;
		private final Button _btnRemove;
		private final Button _btnUp;
		private final Button _btnDown;
		private final E _editComponent;
		
		@SuppressWarnings("unused")
		private boolean _new;
		
		public VaadinCollectionManageRowComponent() {
			////////// Summary 
			_summary = _summaryComponentFactory.from(_i18n);
			
			// edit button
			_btnEdit = new Button(VaadinIcons.EDIT);
			_btnEdit.setDescription(_i18n.getMessage("edit"));
			_btnEdit.addStyleName(ValoTheme.BUTTON_QUIET);

			// remove button
			_btnRemove = new Button(VaadinIcons.TRASH);
			_btnRemove.setDescription(_i18n.getMessage("remove"));
			_btnRemove.addStyleName(ValoTheme.BUTTON_DANGER);
			
			// up & down buttons
			_btnUp = new Button(VaadinIcons.ARROW_UP);
			_btnUp.setDescription(_i18n.getMessage("up"));
			_btnUp.addStyleName(ValoTheme.BUTTON_QUIET);
			
			_btnDown = new Button(VaadinIcons.ARROW_DOWN);
			_btnDown.setDescription(_i18n.getMessage("down"));
			_btnDown.addStyleName(ValoTheme.BUTTON_QUIET);
			

			HorizontalLayout hlySummary = new HorizontalLayout(_summary,_btnEdit,_btnRemove,_btnUp,_btnDown);
			hlySummary.setComponentAlignment(_summary,Alignment.MIDDLE_LEFT);
			hlySummary.setComponentAlignment(_btnEdit,Alignment.MIDDLE_RIGHT);
			hlySummary.setComponentAlignment(_btnRemove,Alignment.MIDDLE_RIGHT);
			hlySummary.setComponentAlignment(_btnUp,Alignment.MIDDLE_RIGHT);
			hlySummary.setComponentAlignment(_btnDown,Alignment.MIDDLE_RIGHT);
			hlySummary.setExpandRatio(_summary,1);
			hlySummary.setExpandRatio(_btnEdit,0);		// exact expand ratio
			hlySummary.setExpandRatio(_btnRemove,0);	// exact expand ratio
			hlySummary.setExpandRatio(_btnUp,0);		// exact expand ratio
			hlySummary.setExpandRatio(_btnDown,0);		// exact expand ratio
			hlySummary.setSizeFull();
			hlySummary.setMargin(false);
			
			////////// Edit component
			_editComponent = _editComponentFactory.from(_i18n);
			_editComponent.setVisible(false);
			
			////////// Layout
			VerticalLayout ly = new VerticalLayout(hlySummary,
												   _editComponent);
			ly.setSizeFull();
			ly.setMargin(false);
			
			this.setCompositionRoot(ly);
			
			////////// Behavior
			_setBehavior();
		}
		private void _setBehavior() {
			// Edit
			_btnEdit.addClickListener(clickEvent -> {
										// show the edit component
										this.toggleExpansion();
									 });
			// Remove
			_btnRemove.addClickListener(clickEvent -> {
											// store the view objs BEFORE removing
											Collection<V> viewObjsBefore = VaadinCollectionManageComponent.this.getAll();
											
								   			// get the removed rel
								   			V removedViewObj = this.getViewObject();
								   			
								   			// remove the row
								   			_vlyGrid.removeComponent(this);
								   			
											// raise an event for each removed view obj
											if (CollectionUtils.hasData(_itemAddedOrRemovedEventListeners)) {
												VaadinCollectionItemAddedOrRemovedEvent<V> event = new VaadinCollectionItemAddedOrRemovedEvent<>(VaadinCollectionItemOperation.REMOVE, 	// remove
																																				 removedViewObj,
																																				 viewObjsBefore);
												_itemAddedOrRemovedEventListeners.stream()
																				 .forEach(listener -> listener.onItemAddedOrRemoved(event));
											}
									   });
			_btnUp.addClickListener(clickEvent -> {
										int index = _vlyGrid.getComponentIndex(this);
										if (index == 0) return;
										
										int newIndex = index - 1;
										_vlyGrid.removeComponent(this);
										_vlyGrid.addComponent(this,newIndex);
										
										_setUpDownButtonsStatus(newIndex);
									});
			_btnDown.addClickListener(clickEvent -> {
										int index = _vlyGrid.getComponentIndex(this);
										if (index == (_vlyGrid.getComponentCount() - 1)) return;
										
										int newIndex = index + 1;
										_vlyGrid.removeComponent(this);
										_vlyGrid.addComponent(this,newIndex);
										
										_setUpDownButtonsStatus(newIndex);
									  });
			// Update the [summary] when the [rel] name changes at the form
			_editComponent.addSummaryChangeEventListener(_i18n.getCurrentLanguage(),
														 summChangeEvt -> {
															 SV val = summChangeEvt.getValue();
															 _summary.setSummaryViewObject(val);	
														 });
		}
		public void setMoveButtonsStatusIfRowAt(final int index) {
			_btnUp.setEnabled(index > 0);
			_btnDown.setEnabled(index < (_vlyGrid.getComponentCount() - 1));
		}
		public void edit(final V viewObj) {
			// bind the form to the view object
			_editComponent.bindUIControlsTo(viewObj);
			_new = false;
		}
		public void setNew(final boolean isNew) {
			_new = isNew;
			this.expand();
		}
		public void expand() {
			_editComponent.setVisible(true);
			if (this.isExpanded()) _rowExpanded(this);
		}
		public void collapse() {
			_editComponent.setVisible(false);
		}
		public void toggleExpansion() {
			if (this.isExpanded()) {
				this.collapse();
			} else {
				this.expand();
			}
		}
		public boolean isExpanded() {
			return _editComponent.isVisible();
		}
		@SuppressWarnings("unused")
		public boolean isCollapsed() {
			return !this.isExpanded();
		}
		public V getViewObject() {
			return _editComponent.getViewObject();
		}
		@Override
		public VerticalLayout getCompositionRoot() {
			return (VerticalLayout)super.getCompositionRoot();
		}
		@Override
		public void updateI18NMessages(final UII18NService i18n) {
			// TODO I18n
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	MANAGED ITEM SUMMARY CHANGED
/////////////////////////////////////////////////////////////////////////////////////////
	public interface VaadinCollectionItemSummaryComponent<SV> 
			 extends Component {
		public void setSummaryViewObject(final SV viewObj);
	}
	public interface HasVaadinManagedCollectionItemSummaryChangeEventListener<SV> {
		public void addSummaryChangeEventListener(final Language lang,
												  final VaadinManagedCollectionItemSummaryChangeEventListener<SV> listener);
	}
	public interface VaadinManagedCollectionItemSummaryChangeEventListener<SV> {
		public void onSummaryChanged(VaadinManagedCollectionItemSummaryChangeEvent<SV> event);
	}
	@Accessors(prefix="_")
	@RequiredArgsConstructor
	public static class VaadinManagedCollectionItemSummaryChangeEvent<SV> {
		@Getter private final SV _oldValue;
		@Getter private final SV _value;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ADD / REMOVE EVENT
/////////////////////////////////////////////////////////////////////////////////////////
	public interface VaadinCollectionItemAddedOrRemovedEventListener<V extends UIViewObject> {
		public void onItemAddedOrRemoved(VaadinCollectionItemAddedOrRemovedEvent<V> event);
	}
	@Accessors(prefix="_")
	@RequiredArgsConstructor
	public static class VaadinCollectionItemAddedOrRemovedEvent<V extends UIViewObject> {
		@Getter private final VaadinCollectionItemOperation _operation;
		@Getter private final V _addedOrRemovedViewObj;
		@Getter private final Collection<V> _viewObjsBeforeAddOrRemove;
	}
	public static enum VaadinCollectionItemOperation {
		ADD,
		REMOVE;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		_rowStream().forEach(rowComp -> rowComp.updateI18NMessages(i18n));	
	}
}
