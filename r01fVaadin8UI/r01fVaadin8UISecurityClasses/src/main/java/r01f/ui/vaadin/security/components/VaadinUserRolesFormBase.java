package r01f.ui.vaadin.security.components;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import r01f.locale.HasI18NKey;
import r01f.locale.I18NKey;
import r01f.model.security.user.User;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.security.components.VaadinUsersCrudGrid.VaadinUsersGridModifiedListener;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;
import r01ui.base.components.form.VaadinViewTracksChanges;
import r01ui.base.components.user.VaadinViewUser;
import r01ui.base.components.window.VaadinProceedGateDialogWindow;

/**
 * A form to manage [system-wide roles]
 * <pre>
 * 		+========================================================+
 * 		| Master Role    | Horizontal Admin  |  Customer service |
 * 		|                | Role              |  Role             |
 * 		| [Add]          | [Add]             |  [Add]            |
 * 		|+--------------+ +-----------------+ +-----------------+|
 * 		||              | |                 | |                 ||
 * 		||              | |                 | |                 ||
 * 		||              | |                 | |                 ||
 * 		||              | |                 | |                 ||
 * 		||              | |                 | |                 ||
 * 		||              | |                 | |                 ||
 * 		||              | |                 | |                 ||
 * 		|+--------------+ +-----------------+ +-----------------+|
 * 		+========================================================+
 * </pre>
 */
public abstract class VaadinUserRolesFormBase<U extends User,V extends VaadinViewUser<U>,
											  G extends VaadinUsersCrudGrid<U,V,?,G>>
   			  extends HorizontalLayout
   		   implements VaadinViewTracksChanges,
   		   			  VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = 1068078186596701814L;
/////////////////////////////////////////////////////////////////////////////////////////
//	EVENT RAISED WHENEVER THE FORM IS MODIFIED
/////////////////////////////////////////////////////////////////////////////////////////
	public interface VaadinUserRolesFormModifiedListener {
		public void onRolesModified();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	private final transient UII18NService _i18n;

/////////////////////////////////////////////////////////////////////////////////////////
//	UI
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * An index of [users grid]s
	 */
	private final transient Map<I18NKey,Integer> _componentIndex = Maps.newHashMap();
	/**
	 * Every [users grid] uses this [users grid modified] listener
	 */
	private final VaadinUsersGridModifiedListener<U,V,G> _usersGridModifiedListener;
	/**
	 * A form-external listener used to tell anyone that the [grid] was modified
	 * (any [users-grid] was modified)
	 */
	private VaadinUserRolesFormModifiedListener _formModifiedListener;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinUserRolesFormBase(final UII18NService i18n) {
		////////// Services
		_i18n = i18n;

		_usersGridModifiedListener = _createUsersGridModifiedListener(i18n);

		////////// Style
		this.setMargin(false);

		////////// i18n
		this.updateI18NMessages(i18n);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Set a listener of any grid modification event
	 * @param formModifiedListener
	 */
	public void setFormModifiedListener(final VaadinUserRolesFormModifiedListener formModifiedListener) {
		_formModifiedListener = formModifiedListener;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CARD CREATE METHOD
/////////////////////////////////////////////////////////////////////////////////////////
	protected void _addRoleGrid(final HasI18NKey hasI18NKey,
								final G usersGrid) {
		_addRoleGrid(hasI18NKey.getI18nKey(),
					 usersGrid);
	}
	protected void _addRoleGrid(final I18NKey caption,
								final G usersGrid) {
		Component comp = _createRoleGrid(caption,
										 usersGrid);
		this.addComponent(comp);
		_componentIndex.put(caption,this.getComponentIndex(comp));
	}
	protected Component _createRoleGrid(final I18NKey caption,
									 	final G usersGrid) {
		// Label
		Label lbl = new Label();
		lbl.addStyleNames(ValoTheme.LABEL_H4,
								 ValoTheme.LABEL_COLORED,
								 ValoTheme.LABEL_NO_MARGIN);
		// Ensure the grid has the [user added to grid] listener
		usersGrid.setGridModifiedListener(_usersGridModifiedListener);

		// Layout
		VerticalLayout ly = new VerticalLayout(lbl,
											   usersGrid);
		ly.setWidthFull();
		ly.addStyleName(ValoTheme.LAYOUT_CARD);

		return ly;
	}
	protected void _changeRoleGridCaption(final I18NKey i18nKey) {
		_changeRoleGridCaption(_getComponentIndexOf(i18nKey),
							   i18nKey);
	}
	protected void _changeRoleGridCaption(final int compIndex,
										  final I18NKey i18nKey) {
		VerticalLayout comp = (VerticalLayout)this.getComponent(compIndex);
		Label lbl = (Label)comp.getComponent(0);
		lbl.setValue(_i18n.getMessage(i18nKey));
	}
	protected G _getUsersCrudGrid(final HasI18NKey hasI18NKey) {
		return _getUsersCrudGrid(hasI18NKey.getI18nKey());
	}
	protected G _getUsersCrudGrid(final I18NKey i18nKey) {
		return _getUsersCrudGrid(_getComponentIndexOf(i18nKey));
	}
	@SuppressWarnings("unchecked")
	protected G _getUsersCrudGrid(final int compIndex) {
		VerticalLayout comp = (VerticalLayout)this.getComponent(compIndex);
		return (G)comp.getComponent(1);
	}
	private int _getComponentIndexOf(final I18NKey i18nKey) {
		if (this.getComponentCount() == 0) throw new IllegalStateException("NO grid exists!");
		Integer compIndex = _componentIndex.get(i18nKey);
		if (compIndex == null) throw new IllegalArgumentException("There does NOT exists a grid named " + i18nKey);
		return compIndex;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DATA CHANGE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean hasViewDataChanged() {
		if (CollectionUtils.isNullOrEmpty(_componentIndex)) return false;
		boolean anyGridHasChanged = false;
		for (int compIndex : _componentIndex.values()) {
			G grid = _getUsersCrudGrid(compIndex);
			boolean gridHasChanges = grid.hasViewDataChanged();
			if (gridHasChanges) {
				anyGridHasChanged = true;
				break;
			}
		}
		return anyGridHasChanged;
	}
	@Override
	public void setViewDataChanged(final boolean changed) {
		if (CollectionUtils.isNullOrEmpty(_componentIndex)) return;
		for (int compIndex : _componentIndex.values()) {
			G grid = _getUsersCrudGrid(compIndex);
			grid.setViewDataChanged(changed);
		}
	}
	public void changesCommited() {
		// changes were saved to the DB
		this.setViewDataChanged(false);		// no changes
		_componentIndex.entrySet()
					   // tell each grid that changes are comited to the db
					   .forEach(me -> {
						   			G grid = _getUsersCrudGrid(me.getValue());
						   			grid.changesCommited();
					   			});
	}
	protected Collection<V> _getDeletedItems() {
		if (CollectionUtils.isNullOrEmpty(_componentIndex)) return Lists.newArrayList();

		Collection<V> allDeleted = Lists.newArrayList();
		for (int compIndex : _componentIndex.values()) {
			G grid = _getUsersCrudGrid(compIndex);
			allDeleted.addAll(grid.getDeletedItems());
		}
		return allDeleted;
	}
	protected Collection<V> _getAddeditems() {
		if (CollectionUtils.isNullOrEmpty(_componentIndex)) return Lists.newArrayList();

		Collection<V> allAdded = Lists.newArrayList();
		for (int compIndex : _componentIndex.values()) {
			G grid = _getUsersCrudGrid(compIndex);
			allAdded.addAll(grid.getAddedItems());
		}
		return allAdded;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	private VaadinUsersGridModifiedListener<U,V,G> _createUsersGridModifiedListener(final UII18NService i18n) {
		return (selectedGrid,action,viewUser) -> {
					switch (action) {
					case USER_ADDITION:
						// find a grid containing the user (other than the selected grid)
						G otherGrid = _findGridContainigUserOtherThan(selectedGrid,viewUser);

						// if the user exists in another grid: show a proceed window
						if (otherGrid != null) {
							VaadinProceedGateDialogWindow window = new VaadinProceedGateDialogWindow(i18n,
																									 I18NKey.named("confirm"),
																							 		 I18NKey.named("security.role.change"),
																							 		 () -> {
																				   			  			 	selectedGrid.addItem(viewUser);
																				   			  			 	otherGrid.removeItem(viewUser.getOid());
																				   			  		 },
																				   			  		 // cancel listener
																				   			  		 null,
																				   			  		 // puzzle
																				   			  		 null);
							UI.getCurrent().addWindow(window);
						}
						// if the user does NOT exists in another grid, just add
						else {
							selectedGrid.addItem(viewUser);
						}
						break;
					case USER_REMOVAL:
						selectedGrid.removeItem(viewUser.getOid());
						break;
					default:
						throw new IllegalArgumentException(action + " is NOT a supported [users grid] action!");
					}
					// in any case (user addition or deletion), tell the [form modified listener]
					// that a modification has been taken place
					if (_formModifiedListener != null) _formModifiedListener.onRolesModified();
				};
	}
	/**
	 * Finds a [grid] other than the given one that contains the given [user]
	 * @param selectedGrid
	 * @param viewUser
	 * @return
	 */
	protected G _findGridContainigUserOtherThan(final G selectedGrid,
											   	final V viewUser) {
		G outGrid = null;
		for (int compIndex : _componentIndex.values()) {
			G grid = _getUsersCrudGrid(compIndex);
			if (grid == selectedGrid) continue;		// not the given grid
			if (grid.getItems().contains(viewUser)) {
				outGrid = grid;
				break;
			}
		}
		return outGrid;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	i18n
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		if (CollectionUtils.isNullOrEmpty(_componentIndex)) return;
		_componentIndex.entrySet()
					   .forEach(me -> {
						   			I18NKey i18nKey = me.getKey();
						   			int index = me.getValue();
						   			_changeRoleGridCaption(index,
						   								   i18nKey);
					   			});
	}
}
