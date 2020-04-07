package r01ui.base.components.treeanddetail;

import java.util.Collection;

import com.vaadin.data.Binder;
import com.vaadin.ui.Component;
import com.vaadin.ui.CustomField;
import com.vaadin.ui.HorizontalSplitPanel;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.facets.HasLanguage;
import r01f.facets.LangInDependentNamed.HasLangInDependentNamedFacet;
import r01f.locale.Language;
import r01f.patterns.Factory;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinView;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.ui.viewobject.UIViewObjectInLanguage;
import r01ui.base.components.form.VaadinFormEditsViewObject;
import r01ui.base.components.tree.VaadinTree.VaadinTreeChangedEventListener;
import r01ui.base.components.tree.VaadinTreeData;

/**
 * A component that wraps the [tree] and the [detail] and coordinates interactions between both
 * ie:
 * 		- when an item is clicked at the [tree], the [detail] view should show the [edit form]
 * 		- when an item is added at the [tree], the [detail] view should also show the [edit form]
 * 		- when the [edit form] contains invalid or incomplete data, this circumstance should be let to know to the [tree]
 * 		  so it can be disabled because the user should NOT leave the [edit form] (for example by clicking another item at the [tree])
 * <pre>
 * 				 +-- This component that wraps the [tree] and the [detail]
 * 				 |
 * 		++++++++\/+++++++++++++++++++++++++++++++++++
 * 		+										   +
 * 		+ +--------+ +---------------------------+ +
 * 		+ | node   | |                           | +
 * 		+ |  +node | | D: Detail                 | +
 * 		+ |  +node | |                           | +
 * 		+ |        | |                           | +
 * 		+ +--------+ +---------------------------+ +
 * 		+ 										   +
 * 		++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 */
@Accessors(prefix="_")
public abstract class VaadinHierarchicalDataInLangViewBase<// the [view object] binded at the [tree] + [detail] component
														   // (this [view object] MIGHT BE the VIL [view object in language] BUT is easier if NOT)
														   VO extends UIViewObjectInLanguage			// a view object (in a language)
														          & HasLangInDependentNamedFacet,	// has a name (in a language)
														   // the detail view where the view obj is edited
														   D extends VaadinHierarchicalDataInLangDetailView<VO>> 
	 		  extends CustomField<VaadinTreeData<VO>> 		// BEWARE! TreeData<VIL>
  		   implements HasLanguage,
  			 		  VaadinView,
  			 		  VaadinFormEditsViewObject<VaadinTreeData<VO>>,	// BEWARE!! this is binding a TreeData<VIL>
  			 		  VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = -741879526211369538L;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter @Setter private Language _language;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	UI COMPONENTS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final VaadinHierarchicalDataTree<VO> _treeGrid;
	@Getter private final D _detailComponent;

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinHierarchicalDataInLangViewBase(final UII18NService i18n,
												final VaadinHierarchicalDataEditConfig settings,
												final Language lang,final Collection<Language> availableLangs,
												final Factory<VO> viewObjInLangFactory,
												final D detailComponent) {
		this(i18n,
			 settings,
			 lang,availableLangs,
			 viewObjInLangFactory,
			 detailComponent,
			 null);	// no tree change event listener
	}
	public VaadinHierarchicalDataInLangViewBase(final UII18NService i18n,
												final VaadinHierarchicalDataEditConfig settings,
												final Language lang,final Collection<Language> availableLangs,
												final Factory<VO> viewObjInLangFactory,
												final D detailComponent,
												final VaadinTreeChangedEventListener treeChangeEventListener) {
		_language = lang;
		
		////////// UI
		// the tree grid
		_treeGrid = new VaadinHierarchicalDataTree<>(i18n,
													 settings,
												 	 // lang & portal available langs
													 lang,availableLangs,	
													 // [view obj] factory
													 viewObjInLangFactory);
		_detailComponent = detailComponent;
		_detailComponent.setVisible(false);		// not visible until a tree item is clicked
		_detailComponent.setOnValueChangeEventListener(// Enable / disable the [tree] depending on the item validation status
													   valChangeEvent -> {
														   VO changedViewObj = valChangeEvent.getViewObject();
														   _treeGrid.setEnabled(true);
													   });
		
		////////// Events
		// what happens when an item is selected (or created) at the tree
		_treeGrid.setOnItemEditEventListener(itemEditReqEvt -> {
													// the currently edited viewObj (this forces the binded object to be saved from the UI controls)
													VO currEditedViewObj = viewObjInLangFactory.create();
													if (_detailComponent != null)  _detailComponent.writeAsDraftEditedViewObjectTo(currEditedViewObj);
																				
													// tell the [detail component] to edit the item
													VO viewObj = itemEditReqEvt.getItemToBeEdited();
													_detailComponent.editViewObject(viewObj);
													_detailComponent.setVisible(true);
											 });
		// what happens when an item is deleted at the tree
		_treeGrid.setOnItemDeletedEventListener(itemDeletedEvt -> {
													// hide the [detail component]
													_detailComponent.setVisible(false);
												});
		// what happens when the tree is update (ie by drag & drop elements or by removing an item)
		if (treeChangeEventListener != null) this.setOnTreeChangedEventListener(treeChangeEventListener);
	}
	@Override
	protected Component initContent() {
		////////// text & description split-panel layout
		HorizontalSplitPanel hsplitPanel = new HorizontalSplitPanel();
		hsplitPanel.setFirstComponent(_treeGrid);
		hsplitPanel.setSecondComponent(_detailComponent);
		hsplitPanel.setSplitPosition(30);
		
		return hsplitPanel;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GET & SET                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public VaadinTreeData<VO> getValue() {
		return _treeGrid.getValue();
	}
	@Override
	protected void doSetValue(final VaadinTreeData<VO> value) {
		_treeGrid.setValue(value);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	Binding
/////////////////////////////////////////////////////////////////////////////////////////
	////////// [viewObject] > [UI control] --------------	
	@Override
	public void editViewObject(final VaadinTreeData<VO> viewObj) {
		_treeGrid.setValue(viewObj);
		_detailComponent.setVisible(false);
	}
	////////// [UI control] > [viewObject] --------------	
	@Override
	public void writeAsDraftEditedViewObjectTo(final VaadinTreeData<VO> viewObj) {
		// in order to keep the received [view object] the "internal" state of the [view object]
		// is REPLACED with the [tree data]
		viewObj.clear();
		VaadinTreeData<VO> currData = this.getValue();
		viewObj.importTree(currData);
	}
	@Override
	public boolean writeIfValidEditedViewObjectTo(final VaadinTreeData<VO> viewObj) {
		this.writeAsDraftEditedViewObjectTo(viewObj);
		return true;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public void setOnTreeChangedEventListener(final VaadinTreeChangedEventListener listener) {
		_treeGrid.setOnTreeChangedEventListener(listener);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	I18N                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		_treeGrid.updateI18NMessages(i18n);
	}
}
