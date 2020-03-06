package r01ui.base.components.treeanddetail;

import java.util.Collection;
import java.util.Comparator;
import java.util.EventListener;
import java.util.EventObject;

import com.vaadin.data.TreeData;
import com.vaadin.data.provider.TreeDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.dnd.DropTargetExtension;
import com.vaadin.ui.themes.ValoTheme;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.facets.LangInDependentNamed.HasLangInDependentNamedFacet;
import r01f.locale.I18NKey;
import r01f.locale.Language;
import r01f.patterns.Factory;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.ui.viewobject.UIViewObjectInLanguage;
import r01f.util.types.collections.CollectionUtils;
import r01ui.base.components.tree.VaadinTree;
import r01ui.base.components.tree.VaadinTree.VaadinTreeChangedEventListener;
import r01ui.base.components.tree.VaadinTreeData;
import r01ui.base.components.window.VaadinProceedGateDialogWindow;

/**
 * A tree like:
 * <pre>
 * 		+++++++++++++++++++++++++++++++++++++++++++++
 * 		+ [add] [remove] [copy to lang]             +
 * 		+-------------------------------------------+
 * 		+ node                                      +
 * 		+   + node                                  +
 * 		+		+ node                              +
 * 		+		+ node                              +
 * 		+	+ node                                  +
 * 		+		+ node                              +
 * 		+++++++++++++++++++++++++++++++++++++++++++++
 * </pre>
 * @param <VO>
 */
public class VaadinHierarchicalDataTree<VO extends UIViewObjectInLanguage 
												 & HasLangInDependentNamedFacet> 
	 extends Composite 
  implements VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = 4193358827131783715L;
/////////////////////////////////////////////////////////////////////////////////////////
//																			  
/////////////////////////////////////////////////////////////////////////////////////////
	private final Factory<VO> _viewObjFactory;
	private final VaadinHierarchicalDataEditConfig _settings;
/////////////////////////////////////////////////////////////////////////////////////////
//	UI
/////////////////////////////////////////////////////////////////////////////////////////	
	private final VaadinHierarchicalDataTreeImpl _treeGrid;
	
	private final Button _btnAdd = new Button();
	private final Button _btnRemove = new Button();
	
	private VaadinHiearchicalDataTreeOnItemEditEventListener<VO> _itemEditEventListener;
	private VaadinHiearchicalDataTreeOnItemDeletedEventListener<VO> _itemDeletedEventListener;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR																		  
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinHierarchicalDataTree(final UII18NService i18n,
									  final VaadinHierarchicalDataEditConfig settings,
									  final Language lang,final Collection<Language> availableLangs,
									  final Factory<VO> viewObjFactory) { 
		_viewObjFactory = viewObjFactory;
		_settings = settings;
		
		_treeGrid = new VaadinHierarchicalDataTreeImpl(i18n,
													   settings);
		HorizontalLayout hlyButtons = _configureButtonsLayout(settings);
		
		VerticalLayout vly = new VerticalLayout(hlyButtons,
												_treeGrid);
		this.setCompositionRoot(vly);
		
		// configure events
		_initBehavior(i18n,
					  settings);
		
		// init i18n
		this.updateI18NMessages(i18n);
	}
	private HorizontalLayout _configureButtonsLayout(final VaadinHierarchicalDataEditConfig vaTypeSettings) {
		// add button
		HorizontalLayout hlButtons = new HorizontalLayout();
		if (vaTypeSettings.isCollection() || 
		   (vaTypeSettings.isNOTCollection() && CollectionUtils.isNullOrEmpty(_treeGrid.getRootItems()))) {
			// allow add a single item
			_btnAdd.addStyleName(ValoTheme.BUTTON_LINK);
			hlButtons.addComponent(_btnAdd);
		}
		
		// remove button
		_btnRemove.setIcon(VaadinIcons.TRASH);
		_btnRemove.addStyleName(ValoTheme.BUTTON_LINK);	
		
		hlButtons.addComponents(_btnRemove);
		
		return hlButtons;
	}
	@SuppressWarnings("unchecked")
	private void _initBehavior(final UII18NService i18n,
							   final VaadinHierarchicalDataEditConfig vaTypeSettings) {
		////////// when an item is selected
		_treeGrid.addSelectionListener(selEvent -> {
											// if selecting an item at the max depth level, the [add button]
											// should be disabled
											VO treeSelectedItemViewObj = _treeGrid.getUniqueSelectedItem();
											int treeSelectedItemDepth = _treeGrid.getItemDepth(treeSelectedItemViewObj);
//											_btnAdd.setEnabled(treeSelectedItemDepth < (_settings.getMaxDepth() - 1));
											_btnAdd.setEnabled(treeSelectedItemDepth < _settings.getMaxDepth());
											
											if (treeSelectedItemViewObj == null) return;	// no selected item (should NOT happen)
											
											// tell the detail view to enter editing the new view object
											VaadinHiearchicalDataTreeOnItemEditEvent<VO> event = new VaadinHiearchicalDataTreeOnItemEditEvent<>(this,
																																				treeSelectedItemViewObj);
											if (_itemEditEventListener != null) _itemEditEventListener.onItemEditRequested(event);
									   });
		////////// add
		if (vaTypeSettings.isCollection() 
			||
		   (vaTypeSettings.isNOTCollection() && CollectionUtils.isNullOrEmpty(_treeGrid.getRootItems()))) {
			// allow add a single item
			_btnAdd.addClickListener(click -> {
										// create a new view obj
										VO newViewObj = _viewObjFactory.create();
										
										// put it into the tree
										VO treeSelectedItem = _treeGrid.getUniqueSelectedItem();
										boolean treeSelectedItemCanContainChildren = _treeGrid.canContainChildItems(treeSelectedItem);
										VO newViewObjParent = treeSelectedItemCanContainChildren ? treeSelectedItem
											  												     : null;
										_treeGrid.addItem(newViewObjParent,	
											  			  newViewObj);
										_treeGrid.expand(newViewObjParent);
										_treeGrid.refreshAll();
										
										// tell the detail view to enter editing the new view object
										VaadinHiearchicalDataTreeOnItemEditEvent<VO> event = new VaadinHiearchicalDataTreeOnItemEditEvent<>(this,		// source component
											  																			   				    newViewObj);
										if (_itemEditEventListener != null) _itemEditEventListener.onItemEditRequested(event);
										
										_treeGrid.movePositionTo(newViewObj);
										
										// while adding a new item, do not allow clicking in another item
										// neither click to the add button
										this.setEnabled(false);
										_btnRemove.setEnabled(true);
										
										// if not a collection and an item has already been added, 
										// DO NOT allow additions
										if (vaTypeSettings.isNOTCollection()
										 && _treeGrid.getRootItems()
											  	     .size() == 1) {
										  _btnAdd.setVisible(false);
										}
									 });
		}
		////////// Trash button
		// trash click
		_btnRemove.addClickListener(clickEvent -> {
										VO treeSelectedItem = _treeGrid.getUniqueSelectedItem();
										if (treeSelectedItem != null) _deleteTreeItem(i18n,
																					  treeSelectedItem);
									});
		// add the drag & drop behavior to the [trash] button 
		final DropTargetExtension<Button> removeDrop = new DropTargetExtension<>(_btnRemove);
		removeDrop.setDropEffect(DropEffect.MOVE);
		removeDrop.addDropListener(dropEvent -> {
										VO draggedItem = _treeGrid.getDraggedItem();
										_deleteTreeItem(i18n,
														draggedItem);
								  });
	}
	private class TreeProceedGateDialogWindow
		  extends VaadinProceedGateDialogWindow {
		
		private static final long serialVersionUID = -7534910999703683273L;

		TreeProceedGateDialogWindow(final UII18NService i18n,
									final R01UIProceedGateOpendEventListener proceedGateOpenedListener) {
			super(i18n,
		  		  I18NKey.forId("portal.manager.delete.confirm.required"),
		  		  I18NKey.forId("portal.manager.operation.delete.captcha"),
		  		  // what happens when the user solves the puzzle
		  		  proceedGateOpenedListener,
		  		  // ask the user to solve a puzzle BEFORE removing the item (or items)
				  new R01UIProceedPuzzleCheck() {
				 	@Override
				 	public boolean check(final String text) {
				  	   return text.equals(i18n.getMessage("portal.manager.operation.delete.validateText"));
				 	}
		  		});
		}
	}
	private void _deleteTreeItem(final UII18NService i18n,
								 final VO item) {
		TreeProceedGateDialogWindow proceedGateDialog = null;
		proceedGateDialog = new TreeProceedGateDialogWindow(i18n,
													  	    // when the user solves the puzzle just remove the items
													  	    () -> {
														   	    
															   // the the container view that an item (or items) were deleted
															   if (_itemDeletedEventListener != null) {
																   	TreeData<VO> deletedTree = _treeGrid.getSubTreeOf(item);
																   	VaadinHiearchicalDataTreeOnItemDeletedEvent<VO> event = new VaadinHiearchicalDataTreeOnItemDeletedEvent<>(this,
																   			  																								  deletedTree);
																   	_itemDeletedEventListener.onItemsDeleted(event);
															   }
															   
															   // remove the item from the tree
															    _treeGrid.removeItem(item);
															    _treeGrid.deselectAll();
															    _treeGrid.refreshAll();
															     
															   // If NOT a collection the [add button] must be
															   // enabled or disabled depending on whether there's an item or not
															   boolean addEnabled = _settings.isCollection() 
																	   		   || (_settings.isNOTCollection() 
																	   				 && CollectionUtils.isNullOrEmpty(_treeGrid.getRootItems()));	// no root items = no items
															   this.setEnabled(addEnabled);
													  		   _btnAdd.setEnabled(addEnabled); 
													  	    });
		UI.getCurrent()
		  .addWindow(proceedGateDialog);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GET VALUE                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinTreeData<VO> getValue() {
		return _treeGrid.getTreeData();
	}
	public void setValue(final VaadinTreeData<VO> treeData) {
		_treeGrid.replaceDataWith(treeData);
		_treeGrid.deselectAll();
		_treeGrid.refreshAll();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ENABLED / DISABLED																		  
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void setEnabled(final boolean enabled) {
		_treeGrid.setEnabled(enabled);
		_btnAdd.setEnabled(enabled);
		_btnRemove.setEnabled(enabled);		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	TREE UPDATED 
/////////////////////////////////////////////////////////////////////////////////////////
	public void setOnTreeChangedEventListener(final VaadinTreeChangedEventListener listener) {
		// just pass the tree the event listener
		_treeGrid.setTreeChangedEventListener(listener);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ITEM EDIT EVENT                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public void setOnItemEditEventListener(final VaadinHiearchicalDataTreeOnItemEditEventListener<VO> listener) {
		_itemEditEventListener = listener;
	}
	@Accessors(prefix="_")
	public static class VaadinHiearchicalDataTreeOnItemEditEvent<VIL extends UIViewObjectInLanguage>
		 		extends EventObject {
		private static final long serialVersionUID = -3843906493580325214L;
		
		@Getter private final VIL _itemToBeEdited;
		
		public VaadinHiearchicalDataTreeOnItemEditEvent(final Component srcComponent,
														final VIL itemToBeEdited) {
			super(srcComponent);
			_itemToBeEdited = itemToBeEdited;
		}
	}
	@FunctionalInterface
	public interface VaadinHiearchicalDataTreeOnItemEditEventListener<VIL extends UIViewObjectInLanguage> 
	         extends EventListener {
	
        public abstract void onItemEditRequested(final VaadinHiearchicalDataTreeOnItemEditEvent<VIL> event);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ITEM DELETE EVENT                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public void setOnItemDeletedEventListener(final VaadinHiearchicalDataTreeOnItemDeletedEventListener<VO> listener) {
		_itemDeletedEventListener = listener;
	}
	@Accessors(prefix="_")
	public static class VaadinHiearchicalDataTreeOnItemDeletedEvent<VIL extends UIViewObjectInLanguage>
		 		extends EventObject {

		private static final long serialVersionUID = 7381227122007331462L;
		
		@Getter private final TreeData<VIL> _treeToBeDeleted;
		
		public VaadinHiearchicalDataTreeOnItemDeletedEvent(final Component srcComponent,
												   		   final TreeData<VIL> treeToBeDeleted) {
			super(srcComponent);
			_treeToBeDeleted = treeToBeDeleted;
		}
	}
	@FunctionalInterface
	public interface VaadinHiearchicalDataTreeOnItemDeletedEventListener<VIL extends UIViewObjectInLanguage> 
	         extends EventListener {
	
        public abstract void onItemsDeleted(final VaadinHiearchicalDataTreeOnItemDeletedEvent<VIL> event);
	} 
/////////////////////////////////////////////////////////////////////////////////////////
//																			  
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		_btnAdd.setCaption(i18n.getMessage("add"));
		_btnRemove.setCaption(i18n.getMessage("remove"));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//																			  
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unused")
	private class VaadinHierarchicalDataTreeImpl
		  extends VaadinTree<VO> {

		private static final long serialVersionUID = -2845849590863734274L;
		
		private final UII18NService _i18n;
		private final VaadinHierarchicalDataEditConfig _vaTypeSettings;
		
		private final TreeDataProvider<VO> _treeDataProvider;
		private final VaadinTreeData<VO> _treeData;
		
		VaadinHierarchicalDataTreeImpl(final UII18NService i18n,
									   final VaadinHierarchicalDataEditConfig vaTypeSettings) {
			super();
			_i18n = i18n;
			_vaTypeSettings = vaTypeSettings;
			
			_treeData = new VaadinTreeData<VO>();
			_treeDataProvider = new TreeDataProvider<VO>(_treeData);
			this.setDataProvider(_treeDataProvider);
			
			_configureTree(i18n);
			
			this.addStyleNames("stripes");
			this.setWidth("340px");
			this.setHeight("500px");
		}
		private void _configureTree(final UII18NService i18n) {
			this.addColumn(vo -> vo.getName())		
					.setId("column-text")
					.setCaption(i18n.getMessage("portal.manager.newImageLink.text"))
					.setSortable(false);
			this.setHierarchyColumn("column-text");
			this.setSelectionMode(SelectionMode.SINGLE);
		}
		public void refreshAll() {
			_treeDataProvider.refreshAll();
		}
		@Override
		public VaadinTreeData<VO> getTreeData() {
			return _treeData;
		}
		public void replaceDataWith(final VaadinTreeData<VO> data) {
			_treeData.replaceWith(data);
		}
		public void addItem(final VO parent,final VO item) {
			_treeData.addItem(parent,item);
		}
		public void removeItem(final VO item) {
			_treeData.removeItem(item);
			_treeDataUpdated();
		}
		public Collection<VO> getRootItems() {
			return _treeData.getRootItems();
		}
		public TreeData<VO> getSubTreeOf(final VO item) {
			return _treeData.getSubTreeOf(item);
		}
		public VO getUniqueSelectedItem() {
			VO outSelItem = null;
			if (CollectionUtils.hasData(this.getSelectedItems())
			 && this.getSelectedItems().size() == 1) {
				outSelItem = this.getSelectedItems().stream().findFirst().orElse(null);
			}
			return outSelItem;
		}
		public int getItemDepth(final VO item) {
			return _recurseFindRoot(item,0);
		}
		public boolean canSelectedItemContainChildItems() {
			VO treeSelItem = this.getUniqueSelectedItem();
			return this.canContainChildItems(treeSelItem);
		}
		public boolean canContainChildItems(final VO item) {
			int itemDepth = item != null ? this.getItemDepth(item)
		  						  		 : -1;
			return itemDepth < 0
				|| _vaTypeSettings.getMaxDepth() == itemDepth ? false
															  : true;
		}
		public int _recurseFindRoot(final VO item,final int currDepth) {
			if (_treeData().getParent(item) == null) return currDepth;
			VO parentItem = _treeData().getParent(item);
			return _recurseFindRoot(parentItem,currDepth+1);
		}
		@Override
		protected String _itemCaption(final VO item) {
			return item.getName();
		}
		@Override
		protected StringBuilder _treeDebugInfo(final TreeData<VO> treeData) {
			return null;
		}
		@Override
		protected boolean _customIsValid(final VO dropTargetItem, 
										 final VO draggedItem) {
			VO dropTargetItemParent = _treeData().getParent(dropTargetItem);
			
			boolean droppingIntoRootNode = dropTargetItemParent == null;
			boolean droppingIntoLevel1Node = _treeData().getRootItems()
														.contains(dropTargetItemParent);
			
			boolean canDropIntoRootNode = droppingIntoRootNode					// dropping to a root node 
									   && _recurseBranchMaxDepthStartingAt(draggedItem,0) < _vaTypeSettings.getMaxDepth(); 	// the dragged item max depth is less than the allowed max depth
			boolean canDroIntoLevel1Node = droppingIntoLevel1Node
										&& _recurseBranchMaxDepthStartingAt(draggedItem,1) < _vaTypeSettings.getMaxDepth();
			if (canDropIntoRootNode
			 || canDroIntoLevel1Node) {
				return true;
			} else {
				Notification.show(_i18n.getMessage("portal.manager.newImageLink.drag.no"), 
								  Type.ERROR_MESSAGE);
				return false;
			}
		}
		/**
		 * The branch max depth
		 * <pre>
		 * 		node				<-- _branchMaxDepthStartingAt(node) = 3
		 * 			+ node			<-- _branchMaxDepthStartingAt(node) = 2	
		 * 				+ node		<-- _branchMaxDepthStartingAt(node) = 1
		 * 					+ node	<-- _branchMaxDepthStartingAt(node) = 0
		 *	</pre>
		 * @param viewObj
		 * @return
		 */
		private int _recurseBranchMaxDepthStartingAt(final VO viewObj,
													 final int startingDepth) {
			if (CollectionUtils.isNullOrEmpty(_treeData().getChildren(viewObj))) return startingDepth;
			return _treeData.getChildren(viewObj)
							.stream()
							.map(childViewObj -> _recurseBranchMaxDepthStartingAt(childViewObj,
																		  		  startingDepth + 1))
							.max(Comparator.naturalOrder())
							.get();
		}
		void movePositionTo(final VO viewObj) {
			this.asSingleSelect()
				.setSelectedItem(viewObj);
			_treeDataProvider.refreshAll();
		}
	}
}
