package r01f.ui.vaadin.security.components.user.search;

import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.RadioButtonGroup;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import r01f.client.api.security.SecurityAPIBase;
import r01f.model.security.user.User;
import r01f.ui.i18n.UII18NService;
import r01f.ui.subscriber.UISubscriber;
import r01f.ui.vaadin.security.user.VaadinSecurityUserDirectory;
import r01f.ui.vaadin.security.user.VaadinViewUser;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.util.types.Strings;
import r01f.util.types.collections.Lists;

/**
 * A fom used to search for users
 * <pre>
 * 		+==============================================+
 * 		| [x] Service catalog  [ ] Corporate directory |
 * 		| User [                            ] [search] |
 * 		+----------------------------------------------+
 * 		| Name    | Surname            | Phone | Email |
 * 		|---------+--------------------+-------+-------|
 * 		|         |                    |       |       |
 * 		|---------+--------------------+-------+-------|
 * 		|         |                    |       |       |
 * 		|---------+--------------------+-------+-------|
 * 		|         |                    |       |       |
 * 		|---------+--------------------+-------+-------|
 * 		|         |                    |       |       |
 * 		+----------------------------------------------+
 * 		|                                [OK] [Cancel] |
 * 		+----------------------------------------------+
 * </pre>
 */
public abstract class VaadinSecurityUserSearchForm<U extends User,V extends VaadinViewUser<U>,
										  		   P extends VaadinSecurityUserSearchPresenter<U,V,
										  											  		   ? extends VaadinSecurityUserSearchCOREMediator<U,
																									   								 		  ? extends SecurityAPIBase<U,?,?,?,?,?>>>>
	 		  extends Composite
	 	   implements VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = -4256416248999437390L;
/////////////////////////////////////////////////////////////////////////////////////////
//	SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	protected final transient P _userSearchPresenter;

/////////////////////////////////////////////////////////////////////////////////////////
//	UI FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final RadioButtonGroup<VaadinSecurityUserDirectory> _radioUserDirectory;

	protected final TextField _txtSearch = new TextField();

	protected final Grid<V> _gridUsers = new Grid<>();
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private final UISubscriber<V> _onSelectSubscriber;
	private final UISubscriber<V> _onDoubleClickSubscriber;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinSecurityUserSearchForm(final UII18NService i18n,
									    final P userSearchPresenter,
									    final UISubscriber<V> onSelectSubscriber,
									    final UISubscriber<V> onDoubleClickSubscriber) {
		_userSearchPresenter = userSearchPresenter;

		_onSelectSubscriber = onSelectSubscriber;
		_onDoubleClickSubscriber = onDoubleClickSubscriber;

		////////// UI
		// user search directory
		_radioUserDirectory = new RadioButtonGroup<>(i18n.getMessage("security.directory"));
		_radioUserDirectory.setItems(VaadinSecurityUserDirectory.LOCAL,
									 VaadinSecurityUserDirectory.CORPORATE);
		_radioUserDirectory.setValue(VaadinSecurityUserDirectory.LOCAL);		// local by default
		_radioUserDirectory.setItemCaptionGenerator(dir -> dir.getNameUsing(i18n));
		_radioUserDirectory.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);

		// grid
		_configureGrid(i18n);

		////////// Style
		_txtSearch.setWidthFull();
		//_gridUsers.setStyleName(R01UIServiceCatalogTheme.SERVICE_CATALOG_GRID);

		// Root layout
		VerticalLayout vly = new VerticalLayout(_radioUserDirectory,
												_txtSearch,
											    _gridUsers);
		vly.setMargin(false);
		this.setCompositionRoot(vly);

		////////// Behavior
		_setBehavior();

		////////// I18N
		this.updateI18NMessages(i18n);
	}
	@SuppressWarnings("unused")
	private void _configureGrid(final UII18NService i18n) {
		// configure columns
		Grid.Column<V,String> colName = _gridUsers.addColumn(V::getName)
															  .setResizable(true)
															  .setMinimumWidthFromContent(true)
															  .setExpandRatio(1)
															  .setId("name");
		Grid.Column<V,String> colSurname = _gridUsers.addColumn(V::getSurname)
																 .setResizable(true)
																 .setMinimumWidth(100)
																 .setId("surname");
		Grid.Column<V,String> colPhone = _gridUsers.addColumn(V::getPhoneAsStringOrNull)
															   .setResizable(false)
															   .setSortable(false)
															   .setId("phone");
		Grid.Column<V,String> colEMail = _gridUsers.addColumn(V::getEMailAsStringOrNull)
															   .setResizable(false)
															   .setSortable(false)
															   .setId("email");

		// set selection mode
		_gridUsers.setSelectionMode(SelectionMode.SINGLE);
		_gridUsers.setSizeFull();
		_gridUsers.setHeightByRows(5);
	}
	private void _setBehavior() {
		// search text: disable search button if no text
		// and refresh the list if at least 3 letters are entered
		_txtSearch.addValueChangeListener(valChangeEvent -> {
												_search(valChangeEvent.getValue());
										  });
		// grid select: enable / disable [select] button depending on there's an item selected
		_gridUsers.addSelectionListener(rowSelectedEvent -> {
											VaadinSecurityUserDirectory userDirectory = _radioUserDirectory.getValue();

											V selectedViewUser = rowSelectedEvent.getFirstSelectedItem()
																				 .orElse(null);
											if (selectedViewUser != null) selectedViewUser.setSourceUserDirectory(userDirectory);
											_return(selectedViewUser,
													_onSelectSubscriber);
										});
		// grid double click: select
		_gridUsers.addItemClickListener(itemClickEvent -> {
											boolean doubleClick = itemClickEvent.getMouseEventDetails()
																				.isDoubleClick();
											if (doubleClick) {
												VaadinSecurityUserDirectory userDirectory = _radioUserDirectory.getValue();

												V selectedViewUser = itemClickEvent.getItem();
												if (selectedViewUser != null) selectedViewUser.setSourceUserDirectory(userDirectory);
												_return(selectedViewUser,
														_onDoubleClickSubscriber);
											}
										});
		// radio change
		_radioUserDirectory.addValueChangeListener(valueChangeEvent -> {
														String text = _txtSearch.getValue();
														_search(text);
												   });
	}
	private void _search(final String text) {
		if (Strings.isNOTNullOrEmpty(text)
		 && text.length() > 3) {
			// search and refresh the grid
			VaadinSecurityUserDirectory searchDir = _radioUserDirectory.getValue();
			_userSearchPresenter.onSearchUserRequested(text,searchDir,
													   viewUsers -> {
														   _gridUsers.setDataProvider(new ListDataProvider<>(viewUsers));
													   });
		} else {
			// just clear the grid
			_gridUsers.setItems(Lists.newArrayList());
		}
		// tell the subscriber that no item is selected
		if (_onSelectSubscriber != null) _onSelectSubscriber.onSuccess(null);
	}
	private void _return(final V selectedViewUser,
						 final UISubscriber<V> subscriber) {
		if (selectedViewUser == null) return;

		V outViewUser = null;

		// if it's an XLNets user, the [user oid] at the LOCAL DB is null (XLNets do know nothing about this local db)
		// The user might or might NOT exist at the LOCAL db so the LOCAL db is queried using the XLNets [user code]
		//		- If the user already exists at the LOCAL db, it's returned
		//		- If the user does NOT already exists at the LOCAL db, it's CREATED
		if (selectedViewUser.getSourceUserDirectory() == VaadinSecurityUserDirectory.CORPORATE) {
			outViewUser = _userSearchPresenter.ensureThereExistsLocalUserForCorporateDirectoryUser(selectedViewUser);
		} else {
			outViewUser = selectedViewUser;
		}

		// tell the subscriber that an item is selected
		if (outViewUser != null && subscriber != null) subscriber.onSuccess(outViewUser);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public void setHeightByRows(final double rows) {
		_gridUsers.setHeightByRows(rows);
	}
	public void clear() {
		_txtSearch.clear();
		_gridUsers.setItems(Lists.newArrayList());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	I18N
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		// grid
		_gridUsers.getColumn("name").setCaption(i18n.getMessage("name"));
		_gridUsers.getColumn("surname").setCaption(i18n.getMessage("surname"));
		_gridUsers.getColumn("phone").setCaption(i18n.getMessage("phone"));
		_gridUsers.getColumn("email").setCaption(i18n.getMessage("email"));
	}
}
