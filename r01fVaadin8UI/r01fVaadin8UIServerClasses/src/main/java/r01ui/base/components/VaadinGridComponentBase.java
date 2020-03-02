package r01ui.base.components;

import java.util.Collection;

import com.google.common.collect.Lists;
import com.vaadin.data.Binder;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

import lombok.extern.slf4j.Slf4j;
import r01f.locale.I18NKey;
import r01f.patterns.Factory;
import r01f.ui.i18n.UII18NService;
import r01f.ui.presenter.UIPresenterSubscriber;
import r01f.ui.vaadin.styles.VaadinValoTheme;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.ui.viewobject.UIViewObject;
import r01f.util.types.collections.CollectionUtils;

/**
 * Creates a grid component like:
 * <pre>
 * 		+++++++++++++++++++++++++++++++++++++++++
 * 		+ Object title  [ADD]				    +
 *	    +										+
 *	    + +-----------------------------------+ +
 *	    + |		      |		     |			  | +
 *	    + +-----------|----------|------------+ +
 *	    + |		      |		     |		      | +
 *	    + |		      |		     |			  | +
 *	    + |		      |		     |			  | +
 *	    + +-----------------------------------+ +
 *	    +++++++++++++++++++++++++++++++++++++++++
 * </pre>
 * @param <V>
 * @param <W>
 */
@Slf4j
public abstract class VaadinGridComponentBase<V extends UIViewObject,
											  W extends VaadinDetailEditWindow<V>>
			  extends VerticalLayout
		   implements VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = 1186941723663834774L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final W _winDetailEdit;

	protected final Label _lblCaption;
	protected final Button _btnNew = new Button(VaadinIcons.PLUS);
	protected final Grid<V> _grid;
	protected final Label _noResultsLabel;
	protected final Binder<V> _gridBinder;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinGridComponentBase(final UII18NService i18n,
								   final W winDetailEdit,
								   final Factory<V> viewObjFactory,
								   final I18NKey captionkey) {
		////////// Edit window
		_winDetailEdit = winDetailEdit;

		////////// Title & add button
		_lblCaption = new Label(i18n.getMessage(captionkey).toUpperCase());
		_btnNew.addStyleName(VaadinValoTheme.BUTTON_ADD);
		_btnNew.addClickListener(event -> {
									// open the [edit] window in ADDITION MODE
									UIPresenterSubscriber<V> subscriber = _afterAddSubscriber(_winDetailEdit);
									_winDetailEdit.forCreating(viewObjFactory,
															   subscriber);		// what to do after save
									UI.getCurrent()
									  .addWindow((Window)_winDetailEdit);
							  	});
		HorizontalLayout hlyoutTitle = new HorizontalLayout(_lblCaption,
															   _btnNew);
		hlyoutTitle.addStyleName(VaadinValoTheme.LABEL_AND_ADD_BUTTON);

		////////// No results label
		_noResultsLabel = new Label(i18n.getMessage("notification.noresults"));
		_noResultsLabel.addStyleName(VaadinValoTheme.GRID_NO_RESULTS_LABEL);
		_noResultsLabel.addStyleName(VaadinValoTheme.NO_MARGIN_TOP);
		_noResultsLabel.addStyleName(VaadinValoTheme.NO_MARGIN_BOTTOM);

		/////////// Grid
		_grid = new Grid<>();
		// binder
		_gridBinder = _grid.getEditor()
						   .getBinder();
		// common cols
		_configureGrid(i18n);

		// more cols
		_configureGridColumns(i18n);

		// data
		_grid.setItems(Lists.newArrayList());

		////////// add components
		this.addComponent(hlyoutTitle);
		this.addComponent(_grid);
		this.addComponent(_noResultsLabel);
		this.addStyleName(VaadinValoTheme.GRID_COMPONENT);
	}
	protected void _configureGrid(final UII18NService i18n) {
		////////// style
		_grid.setStyleName(VaadinValoTheme.GRID_STRIPPED);
		_grid.setSizeFull();
		_grid.setHeightMode(HeightMode.UNDEFINED);

		////////// grid columns
		// add menu column
		_grid.addComponentColumn(viewObj -> new R01UIGridMenu(i18n,
																			 viewObj))
				.setExpandRatio(0)		// exactly as wide as its contents requires
				.setResizable(false);
	}
	/**
	 * Adds the contact mean specific columns
	 * @param i18n
	 */
	protected abstract void _configureGridColumns(final UII18NService i18n);
/////////////////////////////////////////////////////////////////////////////////////////
//	ENTRY POINT
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Sets the list items
	 * @param items
	 */
	public void setItems(final Collection<V> items) {
		Collection<V> theItems = items != null ? items
											   : Lists.newArrayList();
		_noResultsLabel.setVisible(CollectionUtils.isNullOrEmpty(theItems));
		_grid.setVisible(CollectionUtils.hasData(theItems));
		VaadinListDataProviders.collectionBackedOf(_grid)
							   .setItems(theItems);
	}
	/**
	 * @return the item list
	 */
	public Collection<V> getItems() {
		return VaadinListDataProviders.collectionBackedOf(_grid)
									  .getUnderlyingItemsCollection();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CAPTION
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void setCaption(final String caption) {
		_lblCaption.setValue(caption);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	MENU
/////////////////////////////////////////////////////////////////////////////////////////
	protected class R01UIGridMenu
			extends MenuBar {

		private static final long serialVersionUID = -8624200824370173655L;

		protected R01UIGridMenu(final UII18NService i18n,
								final V viewObj) {
			super.setResponsive(true);
			super.addStyleName(ValoTheme.BUTTON_PRIMARY);
			super.addStyleName(ValoTheme.BUTTON_BORDERLESS);
			super.addStyleName(VaadinValoTheme.GRID_DOTS_MENU);
			MenuItem principal = super.addItem("",			// text
											   VaadinIcons.ELLIPSIS_DOTS_V,
											   null);		// command

			// Edit
			principal.addItem(i18n.getMessage("edit"),
						 	  VaadinIcons.EDIT,
							  selectedItem -> {
								   	// open the detail window in EDIT mode
									UIPresenterSubscriber<V> saveSubscriber = _afterEditSubscriber(_winDetailEdit);
									UIPresenterSubscriber<V> deleteSubscriber = _afterDeleteSubscriber(_winDetailEdit);
									_winDetailEdit.forEditing(viewObj,
															  saveSubscriber,deleteSubscriber);		// what to do after save or delete
									UI.getCurrent()
									  .addWindow((Window)_winDetailEdit);

								  });
			// Delete
			principal.addItem(i18n.getMessage("delete"),
						 	  VaadinIcons.TRASH,
							  selectedItem -> {
									VaadinListDataProviders.collectionBackedOf(_grid)
														   .removeItem(viewObj);
									_showOrHideNoResultsMessage();
								});
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SUBSCRIBERS
/////////////////////////////////////////////////////////////////////////////////////////
	private UIPresenterSubscriber<V> _afterEditSubscriber(final W win) {
		return UIPresenterSubscriber.from(
					// on success
					viewObj -> {
						// Refresh the edited item
						VaadinListDataProviders.collectionBackedOf(_grid)
											   .refreshItem(viewObj);
						_afterAddOrEdit(viewObj);
						// close the window
						win.close();
					},
					// on error
					th -> {
						// TODO do something with the error
						log.error("Error: {}",
								  th.getMessage(),th);
					});
	}
	private UIPresenterSubscriber<V> _afterAddSubscriber(final W window) {
		return UIPresenterSubscriber.from(
					// on success
					viewObj -> {
						// Add the new item to the list
						VaadinListDataProviders.collectionBackedOf(_grid)
											   .addNewItem(viewObj);

						_afterAddOrEdit(viewObj);
						_showOrHideNoResultsMessage();
						// close the delete confirm view
						window.close();
					},
					// on error
					th -> {
						// TODO do something with the error
						log.error("Error: {}",
								  th.getMessage(),th);
					});
	}
	private UIPresenterSubscriber<V> _afterDeleteSubscriber(final W window) {
		return UIPresenterSubscriber.from(
					// on success
					obj -> {
						// Refresh the edited item
						VaadinListDataProviders.collectionBackedOf(_grid)
											   .removeItem(obj);
						_showOrHideNoResultsMessage();
						// close the window
						window.close();
					},
					// on error
					th -> {
						// TODO do something with the error
						log.error("Error: {}",
								  th.getMessage(),th);
					});
	}
	private void _showOrHideNoResultsMessage() {
		if (VaadinListDataProviders.collectionBackedOf(_grid)
								   .getUnderlyingItemsCollectionSize() > 0) {
			_noResultsLabel.setVisible(false);
			_grid.setVisible(true);
		} else {
			_noResultsLabel.setVisible(true);
			_grid.setVisible(false);
		}
	}
	/**
	 * Called just after an item is added to the grid or edited
	 * This hook can be used to process all visible grid items
	 * and change something
	 * @param viewObj
	 */
	protected abstract void _afterAddOrEdit(final V viewObj);
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		// TODO Auto-generated method stub
	}
}
