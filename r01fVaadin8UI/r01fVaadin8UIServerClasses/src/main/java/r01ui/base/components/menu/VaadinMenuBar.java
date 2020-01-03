package r01ui.base.components.menu;

import java.util.Collection;
import java.util.Map;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.vaadin.server.Resource;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.locale.I18NKey;
import r01f.patterns.CommandOn;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.styles.VaadinValoTheme;
import r01f.ui.vaadin.view.VaadinComponentHasCaption;
import r01f.ui.vaadin.view.VaadinComponentHasIcon;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.ui.vaadin.view.VaadinViewID;
import r01f.ui.vaadin.view.VaadinViews;
import r01f.util.types.collections.CollectionUtils;

public class VaadinMenuBar 
	 extends MenuBar
  implements VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = 3130637584876790267L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS																		  
/////////////////////////////////////////////////////////////////////////////////////////
	protected final Collection<VaadinMenuItem> _rootItems = Lists.newArrayList();
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR																		  
/////////////////////////////////////////////////////////////////////////////////////////	
	public VaadinMenuBar() {
		// default no-args constructor
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ADD ITEMS																		  
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinMenuItemBuilderI18NStep addItem(final VaadinViewID viewId) {
		return this.addItem(viewId,(Map<String,String>)null);
	}
	public VaadinMenuItemBuilderI18NStep addItem(final VaadinViewID viewId,final Map<String,String> navParams) {
		return this.addItem(viewId,navParams,
							null);	// no icon
	}
	public VaadinMenuItemBuilderI18NStep addItem(final VaadinViewID viewId,final Map<String,String> navParams,
								  				 final Resource icon) {
		return this.addItem(I18NKey.forId(viewId),icon,
							 _createVaadinNavigateCommand(viewId,navParams));
	}
	public VaadinMenuItemBuilderI18NStep addItem(final I18NKey key) {
		return this.addItem(key,
							null);		// no command
	}
	public VaadinMenuItemBuilderI18NStep addItem(final Resource icon) {
		return this.addItem((I18NKey)null,icon,
							(MenuBar.Command)null);		// no command
	}
	public VaadinMenuItemBuilderI18NStep addItem(final I18NKey key,
								 				 final MenuBar.Command command) {
		return this.addItem(key,null,	// no icon
							command);
	}
	public VaadinMenuItemBuilderI18NStep addItem(final I18NKey key,final Resource icon,
								  				 final MenuBar.Command command) {
		return new VaadinMenuItemBuilderI18NStep(key,icon,
												 command,
												 _rootItems);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//																			  
/////////////////////////////////////////////////////////////////////////////////////////
	public void executeInAllInHierarchy(final CommandOn<VaadinMenuItem> cmd) {
		if (CollectionUtils.isNullOrEmpty(_rootItems)) return;
		for (VaadinMenuItem rootItem : _rootItems) {
			rootItem.executeInAllInHierarchy(cmd);
		}
	}
	public VaadinMenuItem itemOf(final MenuItem menuItem) {
		return this.itemOf(item -> item.getMenuItem() == menuItem);
	}
	public VaadinMenuItem itemOf(final I18NKey key) {
		return this.itemOf(item -> item.getKey().is(key));
	}
	// Recursively finds an item verifying the given predicate
	public VaadinMenuItem itemOf(final Predicate<VaadinMenuItem> pred) {
		VaadinMenuItem outItem = null;
		for (VaadinMenuItem rootItem : _rootItems) {
			outItem = rootItem.itemOf(pred);
			if (outItem != null) break;
		}
		return outItem;
	}
	public boolean selectMenuFor(final I18NKey key) {
		if (_rootItems.isEmpty()) return false;
		
		// [1] - Unselect the selected menu item
		this.executeInAllInHierarchy(item -> item.setNOTSelected());
   		
		// [2] - Find an item with the given key
		VaadinMenuItem item = this.itemOf(key);
		
		// [3] - Select the item
		if (item != null) item.setSelected();
		
		// [4] - Return
		return item != null;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	I18N																		  
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		for (VaadinMenuItem item : _rootItems) {
			item.updateI18NMessages(i18n);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	Command to execute when an item is clicked																		 
/////////////////////////////////////////////////////////////////////////////////////////
	/** Navigate to a view by menu selection */ 
	private MenuBar.Command _createVaadinNavigateCommand(final VaadinViewID viewId,final Map<String,String> navParams) {
		return new MenuBar.Command() {
							private static final long serialVersionUID = 7920906555442357534L;
							
							@Override
							public void menuSelected(final MenuItem selectedItem) {
								// [1] - Unselect the selected menu item
						   		VaadinMenuBar.this.executeInAllInHierarchy(item -> item.setNOTSelected());
						   		
								// [2] - Find an item with the given menu
								VaadinMenuItem item = VaadinMenuBar.this.itemOf(selectedItem);
								if (item == null) throw new IllegalStateException();
								item.setSelected();
								
								// [3] - use the key as vaadin id
								final VaadinViewID viewId = item != null ? VaadinViewID.forId(item.getKey().asString())
																		 : null;
								if (viewId != null) {
									String viewUrlPathParam = VaadinViews.vaadinViewUrlPathFragmentOf(viewId,
																									  navParams);
									UI.getCurrent().getNavigator()
												   .navigateTo(viewUrlPathParam);
								} else {
									Notification.show("Vaadin view NOT available");
								}			
							}
					}; 		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//																			  
/////////////////////////////////////////////////////////////////////////////////////////
	@Accessors(prefix="_")
	@RequiredArgsConstructor
	public class VaadinMenuItem 
	  implements VaadinComponentHasCaption,VaadinComponentHasIcon,
	  			 VaadinViewI18NMessagesCanBeUpdated {
		
		@Getter private final I18NKey _key;
		@Getter private final MenuItem _menuItem;
		@Getter private boolean _selected;
		
		@Getter private final Collection<VaadinMenuItem> _subItems = Lists.newArrayList();
		
		public boolean hasSubItems() {
			return !_subItems.isEmpty();			
		}
		public void executeInAllInHierarchy(final CommandOn<VaadinMenuItem> cmd) {
			cmd.executeOn(this);
			
			if (CollectionUtils.isNullOrEmpty(_subItems)) return;
			for (VaadinMenuItem item : _subItems) {
				cmd.executeOn(item);
				for (VaadinMenuItem subItem : item.getSubItems()) {
					subItem.executeInAllInHierarchy(cmd);	// BEWARE!! recursion
				}
			}
		}
		public VaadinMenuItem itemOf(final MenuItem menuItem) {
			return this.itemOf(item -> item.getMenuItem() == menuItem);
		}
		public VaadinMenuItem itemOf(final I18NKey key) {
			return this.itemOf(item -> item.getKey().is(key));
		}
		// Recursively finds an item verifying the given predicate
		public VaadinMenuItem itemOf(final Predicate<VaadinMenuItem> pred) {
			// check this
			if (pred.apply(this)) return this;
			
			// check children
			if (CollectionUtils.isNullOrEmpty(_subItems)) return null;
			VaadinMenuItem outItem = null;
			for (VaadinMenuItem item : _subItems) {
				if (pred.apply(item)) {
					outItem = item;
					break;
				}
				for (VaadinMenuItem subItem : item.getSubItems()) {
					outItem = subItem.itemOf(pred);		// BEWARE!! recursion
					if (outItem != null) break;
				}
			}
			return outItem;
		}
		public void setSelected() {
			_selected = true;
			_menuItem.setStyleName(VaadinValoTheme.MENU_ITEM_CHECKED);
		}
		public void setNOTSelected() {
			_selected = false;
			_menuItem.setStyleName(null);
		}
		public VaadinMenuItemBuilderI18NStep addItem(final VaadinViewID viewId) {
			return this.addItem(viewId,(Map<String,String>)null);		// no params
		}
		public VaadinMenuItemBuilderI18NStep addItem(final VaadinViewID viewId,final Map<String,String> navParams) {
			return this.addItem(viewId,navParams,
								null);		// no icon
		}
		public VaadinMenuItemBuilderI18NStep addItem(final VaadinViewID viewId,final Resource icon) {
			return this.addItem(viewId,(Map<String,String>)null,
								icon);
		}
		public VaadinMenuItemBuilderI18NStep addItem(final VaadinViewID viewId,final Map<String,String> navParams,
									 				 final Resource icon) {
			return this.addItem(I18NKey.forId(viewId),icon,
								_createVaadinNavigateCommand(viewId,navParams));
		}
		public VaadinMenuItemBuilderI18NStep addItem(final I18NKey key) {
			return this.addItem(key,
								null);		// no command
		}
		public VaadinMenuItemBuilderI18NStep addItem(final I18NKey key,
									  				 final MenuBar.Command command) {
			return this.addItem(key,null,	// no icon
								command);
		}
		public VaadinMenuItemBuilderI18NStep addItem(final I18NKey key,final Resource icon,
									  				 final MenuBar.Command command) {
			return new VaadinMenuItemBuilderI18NStep(key,icon,
													 command,
													 _subItems);
		}
		@Override
		public String getCaption() {
			return _menuItem.getText();
		}
		@Override
		public void setCaption(final String caption) {
			_menuItem.setText(caption);
		}
		@Override
		public Resource getIcon() {
			return _menuItem.getIcon();
		}
		@Override
		public void setIcon(final Resource icon) {
			_menuItem.setIcon(icon);
		}
		@Override
		public void updateI18NMessages(final UII18NService i18n) {
			if (_key != null) _menuItem.setText(i18n.getMessage(_key));
			if (this.hasSubItems()) {
				for (VaadinMenuItem subItem : _subItems) {
					subItem.updateI18NMessages(i18n);
				}
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor
	public class VaadinMenuItemBuilderI18NStep {
		private final I18NKey _key;
		private final Resource _icon;
		private final MenuBar.Command _command;
		private final Collection<VaadinMenuItem> _col;
		
		public VaadinMenuItem withCaptionFrom(final UII18NService i18n) {
			String caption = _key != null ? i18n.getMessage(_key)
										  : "";
			MenuItem menuItem = VaadinMenuBar.super.addItem(caption,_icon,
										  	  				_command);
			VaadinMenuItem item = new VaadinMenuItem(_key,menuItem);
			_col.add(item);  
			return item;
		}
		public VaadinMenuItem withNOCaption() {
			MenuItem menuItem = VaadinMenuBar.super.addItem(null,_icon,
										  	  				_command);
			VaadinMenuItem item = new VaadinMenuItem(_key,menuItem);
			_col.add(item);  
			return item;
		}
	}
}
