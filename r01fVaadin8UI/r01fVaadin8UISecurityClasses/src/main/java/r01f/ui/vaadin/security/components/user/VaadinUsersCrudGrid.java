package r01f.ui.vaadin.security.components.user;

import java.util.Collection;
import java.util.stream.Collectors;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Composite;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import lombok.experimental.Accessors;
import r01f.client.api.security.SecurityAPIBase;
import r01f.model.security.user.User;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.ui.i18n.UII18NService;
import r01f.ui.subscriber.UISubscriber;
import r01f.ui.vaadin.VaadinComponents;
import r01f.ui.vaadin.security.components.user.search.VaadinSecurityUserSearchCOREMediator;
import r01f.ui.vaadin.security.components.user.search.VaadinSecurityUserSearchForm;
import r01f.ui.vaadin.security.components.user.search.VaadinSecurityUserSearchPresenter;
import r01f.ui.vaadin.security.user.VaadinViewUser;
import r01f.ui.vaadin.styles.VaadinValoTheme;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.util.types.collections.Lists;
import r01ui.base.components.VaadinListDataProviders;
import r01ui.base.components.form.VaadinViewTracksChanges;
import r01ui.base.components.grid.VaadinGridButton;


/**
 * Creates the [user] admin view
 * <pre>
 * 		+========================+		   	  User search form
 * 		| [Add] ---------------click---------->  +==============================================+
 * 		|						 |				 | [x] Service catalog  [ ] Corporate directory |
 * 		|+----------------------+|				 | User [							 ] [search] |
 * 		|| User 1		   [del]||				 +----------------------------------------------+
 * 		|| User 2		   [del]A|				 | Name	   | Surname			| Phone | Email |
 * 		||				  		||				 |---------+--------------------+-------+-------|
 * 		||						||				 |		   |					|		|		|
 * 		||						||				 |---------+--------------------+-------+-------|
 * 		||						||				 |		   |					|		|		|
 * 		||						||				 |---------+--------------------+-------+-------|
 * 		|+----------------------+|				 |		   |					|		|		|
 * 		+========================+				 |---------+--------------------+-------+-------|
 *												 |		   |					|		|		|
 *												 +----------------------------------------------+
 *												 |							  	  [OK] [Cancel] |
 *												 +----------------------------------------------+
 * </pre>
 */
@Accessors(prefix = "_")
public abstract class VaadinUsersCrudGrid<U extends User,V extends VaadinViewUser<U>,
								 		  P extends VaadinSecurityUserSearchPresenter<U,V,
								 											 		  ? extends VaadinSecurityUserSearchCOREMediator<U,
																									   								 ? extends SecurityAPIBase<U,?,?,?,?,?,?,?>>>,
								 		  SELF_TYPE extends VaadinUsersCrudGrid<U,V,P,SELF_TYPE>>
	 		  extends Composite
	 	   implements VaadinViewTracksChanges,
  			 		  VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = 2735944790896863411L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final Button _btnAddUser = new Button();
	protected final Grid<V> _gridUsers = new Grid<>();
	protected final VaadinIsUserSearchPopUp _popUpSearch;
/////////////////////////////////////////////////////////////////////////////////////////
//	STATUS (avoid whenever possible)
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Stores the items set into the grid
	 * It's used to track changes: items added & removed
	 */
	protected Collection<V> _originalItems;
	/**
	 * Is enabled
	 */
	protected boolean _enabled = true;
	/**
	 * Control data change
	 */
	protected boolean _viewDataChange = false;
	/**
	 * Listener of user additions
	 */
	protected VaadinUsersGridModifiedListener<U,V,SELF_TYPE> _gridModifiedListener;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUsersCrudGrid(final UII18NService i18n,
							   final P userSearchPresenter) {
		this(i18n,
			 userSearchPresenter,
			 null);
	}
	@SuppressWarnings("unchecked")
	public VaadinUsersCrudGrid(final UII18NService i18n,
							   final P userSearchPresenter,
							   final VaadinUsersGridModifiedListener<U,V,SELF_TYPE> addUserToOtherRoleListener) {
		_gridModifiedListener = addUserToOtherRoleListener;
		////////// UI
		_configureGrid(i18n);
		_popUpSearch = _createUserSearchPopUp(i18n,
											  userSearchPresenter,
											  // what happens when an uses is selected at the popup
											  viewUser -> {
												  if (_gridModifiedListener != null) {
													  // tell the listener that a user is to be added
													  // ... the listener is in charge of adding (or not) the user to the grid
													  _gridModifiedListener.onGridModified((SELF_TYPE)this,
															  							   VaadinUsersGridAction.USER_ADDITION,viewUser);
												  } else {
													  this.addItem(viewUser);
												  }
											  });

		////////// Layout
		VerticalLayout vly = new VerticalLayout(_btnAddUser,
												_gridUsers);
		vly.setMargin(false);
		this.setCompositionRoot(vly);

		////////// Style
		this.setStyleName(VaadinValoTheme.GRID_CELL_NO_BORDER);

		////////// Behavior
		_setBehavior();

		////////// I18n
		this.updateI18NMessages(i18n);
	}
	@SuppressWarnings("unchecked")
	protected void _configureGrid(final UII18NService i18n) {
		// text expansible
		_gridUsers.addComponentColumn(viewUser -> {
											// wrap the column value into a layout to enable the click listener
											CssLayout layout = VaadinComponents.wrapIntoCssLayout(viewUser.getDisplayName());
											layout.addLayoutClickListener(lyClickEvent -> _gridUsers.asSingleSelect().setValue(viewUser));
											return layout;
									  })
				  .setDescriptionGenerator(viewUser -> viewUser.getDescription())
				  .setResizable(false)
				  .setId("workPlaceCode");

		// the [edit] | [delete] buttons at the end of the row
		_gridUsers.addComponentColumn(viewObj -> {
											Button btnDel = new VaadinGridButton(i18n.getMessage("delete"),
																				 VaadinIcons.TRASH,
																				 clickEvent -> {
																					 if (_gridModifiedListener != null) {
																						// tell the listener that a user is to be added
																						// ... the listener is in charge of removing (or not) the user to the grid
																						 _gridModifiedListener.onGridModified((SELF_TYPE)this,
																								  							  VaadinUsersGridAction.USER_REMOVAL,viewObj);
																					 } else {
																						 this.removeItem(viewObj.getOid());
																					 }
																				 },
																				 VaadinValoTheme.NO_PADDING);
											return new CssLayout(btnDel);
										})
				   .setId("btnDel")
				   .setResizable(false)
				   .setWidth(60);

		// style the grid
		_gridUsers.setSizeFull();
		_gridUsers.setResponsive(true);
		_gridUsers.setHeaderVisible(false);
		_gridUsers.setHeight(200, Unit.PIXELS);
	}
	private void _setBehavior() {
		// when clicking the [add user] button open the popup
		_btnAddUser.addClickListener(clickEvent -> _popUpSearch.show());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ENABLE / DISABLE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setEnabled(final boolean enabled) {
		_enabled = enabled;

		_btnAddUser.setEnabled(enabled);
		_gridUsers.setEnabled(enabled);
		_popUpSearch.setEnabled(enabled);
	}
	@Override
	public boolean isEnabled() {
		return _enabled;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ITEMS
/////////////////////////////////////////////////////////////////////////////////////////
	public void setItems(final Collection<V> items) {
		ListDataProvider<V> listDataProvider = DataProvider.ofCollection(items);
		_gridUsers.setDataProvider(listDataProvider);

		_originalItems = Lists.newArrayList(items);
	}
	public Collection<V> getItems() {
		return VaadinListDataProviders.collectionBackedOf(_gridUsers)
									  .getUnderlyingItemsCollection();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  ITEMS ACCESS
/////////////////////////////////////////////////////////////////////////////////////////
	public void removeItem(final UserOID userOid) {
		VaadinListDataProviders.collectionBackedOf(_gridUsers)
							   .removeItemIf(theUser -> theUser.getOid().is(userOid));
		_viewDataChange = true;
	}
	public void addItem(final V viewUser) {
		// do NOT add item if it already exists
		if (!VaadinListDataProviders.collectionBackedOf(_gridUsers)
	  						 	    .containsItem(viewUser)) {
			VaadinListDataProviders.collectionBackedOf(_gridUsers)
								   .addNewItem(viewUser);
			_viewDataChange = true;
		}
	}
	public Collection<V> getDeletedItems() {
		Collection<V> deleteItems = _originalItems.stream()
												  	  		  .filter(user -> !this.getItems().contains(user))
												  	  		  .collect(Collectors.toList());
		return deleteItems;
	}
	public Collection<V> getAddedItems() {
		Collection<V> addedItems = this.getItems()
												   .stream()
												   .filter(user -> !_originalItems.contains(user))
												   .collect(Collectors.toList());
		return addedItems;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DATA CHANGE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setViewDataChanged(final boolean changed) {
		_viewDataChange = changed;
	}
	@Override
	public boolean hasViewDataChanged() {
		return _viewDataChange;
	}
	public void changesCommited() {
		this.setViewDataChanged(false);
		_originalItems = Lists.newArrayList(this.getItems());	// BEWARE! create a NEW collection!
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EVENT RAISED WHEN THE GRID IS MODIFIED
/////////////////////////////////////////////////////////////////////////////////////////
	public void setGridModifiedListener(final VaadinUsersGridModifiedListener<U,V,SELF_TYPE> listener) {
		if (_gridModifiedListener == null) {
			_gridModifiedListener = listener;
		}
	}
	public interface VaadinUsersGridModifiedListener<U extends User,V extends VaadinViewUser<U>,
													 G extends VaadinUsersCrudGrid<U,V,?,?>> {
		public void onGridModified(final G srcGrid,
								   final VaadinUsersGridAction action,
								   final V viewUser);
	}
	public enum VaadinUsersGridAction {
		USER_ADDITION,
		USER_REMOVAL;
	}
/////////////////////////////////////////////////////////////////////////////////////////
// I18N
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		_btnAddUser.setCaption(i18n.getMessage("add"));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER SEARCH POPUP
/////////////////////////////////////////////////////////////////////////////////////////
	protected abstract VaadinIsUserSearchPopUp _createUserSearchPopUp(final UII18NService i18n,
																	  final P userSearchPresenter,
																	  final UISubscriber<V> selectSubscriber);
	protected interface VaadinIsUserSearchPopUp
				extends Component,
						VaadinViewI18NMessagesCanBeUpdated {
		public void show();
	}
	protected abstract class VaadinUserSearchPopUpBase<F extends VaadinSecurityUserSearchForm<U,V,P>>
		  			 extends Window
		  		  implements VaadinIsUserSearchPopUp {

		private static final long serialVersionUID = -5423050396649827819L;

		////////// UI
		private final VaadinSecurityUserSearchForm<U,V,?> _frmSearch;
		private final Button _btnPopUpOK = new Button();
		private final Button _btnPopUpCancel = new Button();

		private final UISubscriber<V> _selectSubscriber;

		////////// State (avoid as much as possible)
		private V _selectedViewUser = null;

		protected VaadinUserSearchPopUpBase(final UII18NService i18n,
											final P userSearchPresenter,
											final UISubscriber<V> selectSubscriber) {
			_selectSubscriber = selectSubscriber;

			// create the form
			_frmSearch = _createUsersSearchFormUsing(i18n,
													 userSearchPresenter,
													 // what happens when a user is selected at the grid
													 viewUser -> {
														 // store the selected user
														 _selectedViewUser = viewUser;
														 // if no user is selected, disable the [select] button
														 _btnPopUpOK.setEnabled(viewUser != null);
													 },
													 // what happens when a user is double-clicked at the grid
													 viewUser -> {
														 // store the selected user
														 _selectedViewUser = viewUser;
														 // tell the selected subscriber and close the popup
														 _tellSelectSubscriberAboutSelectedUserAndClose();
													 });
			// style
			_frmSearch.setStyleName(VaadinValoTheme.GRID_CELL_NO_BORDER);
			_btnPopUpOK.setStyleName(ValoTheme.BUTTON_PRIMARY);

			// layout
			HorizontalLayout lyButtons = new HorizontalLayout(_btnPopUpOK,_btnPopUpCancel);

			VerticalLayout ly = new VerticalLayout(_frmSearch,
												   lyButtons);

			ly.setComponentAlignment(lyButtons, Alignment.MIDDLE_RIGHT);
			this.setContent(ly);

			// style
			this.setModal(true);
			this.setWidth(70, Unit.PERCENTAGE);

			// behavior
			_setBehavior();

			// i18n
			this.updateI18NMessages(i18n);
		}
		private void _setBehavior() {
			// if CANCEL, just close the window (no data is commited to the [view object])
			_btnPopUpCancel.addClickListener(clickEvent -> {
												_selectedViewUser = null;
												this.close();
											 });
			// select button: just tell the subscriber
			_btnPopUpOK.addClickListener(clickEvent -> _tellSelectSubscriberAboutSelectedUserAndClose());
		}
		private void _tellSelectSubscriberAboutSelectedUserAndClose() {
			if (_selectSubscriber != null && _selectedViewUser != null) {
				_selectSubscriber.onSuccess(_selectedViewUser);
				this.close();
			}
		}
		@Override
		public void show() {
			_frmSearch.clear();
			UI.getCurrent()
			  .addWindow(this);
		}
		@Override
		public void updateI18NMessages(final UII18NService i18n) {
			this.setCaption(i18n.getMessage("select"));
			_btnPopUpOK.setCaption(i18n.getMessage("select"));
			_btnPopUpCancel.setCaption(i18n.getMessage("cancel"));
		}
		protected abstract F _createUsersSearchFormUsing(final UII18NService i18n,
														 final P userSearchPresenter,
													     final UISubscriber<V> onSelectSubscriber,
													     final UISubscriber<V> onDoubleClickSubscriber);
	}
}