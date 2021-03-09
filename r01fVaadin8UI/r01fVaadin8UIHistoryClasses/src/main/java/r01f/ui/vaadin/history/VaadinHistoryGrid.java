package r01f.ui.vaadin.history;

import java.time.LocalDateTime;
import java.time.format.FormatStyle;
import java.util.Collection;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;

import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.VaadinDates;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;


/**
 * A grid of [history entries]
 * <pre>
 * 		+----------+-----------+-------------+---------------+
 * 		| When     | What      | About       | Who           |
 * 		+----------+-----------+-------------+---------------+
 * 		|          |           |             |               |
 * 		|          |           |             |               |
 * 		|          |           |             |               |
 * 		|          |           |             |               |
 * 		|          |           |             |               |
 * 		|          |           |             |               |
 * 		+----------+-----------+-------------+---------------+
 * </pre>
 * Usage:
 * <pre class='brush:java'>
 * 		VaadinHistoryGrid<MyHistoryEntry> grid = new VaadinHistoryGrid<>(i18n);
 * 		historyPresenter.onUserHistoryEntriesLoadRequested(objOid,i18n.getCurrentLanguage(),
 * 														   viewEntries -> grid.setHistoryItems(gridItems));
 * </pre>
 * @param <A>
 * @param <O>
 * @param <H>
 * @param <V>
 */
public class VaadinHistoryGrid<V extends VaadinViewHistoryEntry<?,?,?>>
     extends Composite 
  implements VaadinViewI18NMessagesCanBeUpdated {

	private static final long serialVersionUID = 2069334847582369557L;
/////////////////////////////////////////////////////////////////////////////////////////
//	UI FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	protected final Grid<V> _gridHistoryEntries = new Grid<>();	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinHistoryGrid(final UII18NService i18n) {
		////////// UI
		// grid
		_configureGrid(i18n);
		
		// Composition
		this.setCompositionRoot(_gridHistoryEntries);
		
		////////// Behavior
		_setBehavior();

		////////// I18N
		this.updateI18NMessages(i18n);
	}
	private void _setBehavior() {
		// nothing 
	}
	@SuppressWarnings("unused")
	private void _configureGrid(final UII18NService i18n) {
		// configure columns
		Grid.Column<V,LocalDateTime> colWhen = _gridHistoryEntries.addColumn(entry -> entry.getWhen())
													 	 		  .setRenderer(VaadinDates.createLocalDateTimeRenderer(i18n.getCurrentLanguage(),FormatStyle.FULL))
													 	 		  .setMinimumWidthFromContent(true)
													 	 		  .setExpandRatio(1)
													 	 		  .setResizable(false)
													 	 		  .setId("when");
		Grid.Column<V,String> colWhat = _gridHistoryEntries.addColumn(entry -> i18n.getMessage(entry.getWhat().getI18nKey()))
												  .setDescriptionGenerator(entry -> entry.getWhat().name())
												  .setResizable(false)
												  .setMinimumWidthFromContent(true)
												  .setExpandRatio(1)
												  .setId("what");
		Grid.Column<V,String> colAbout = _gridHistoryEntries.addColumn(entry -> entry.getAboutDetails())
												   .setDescriptionGenerator(entry -> entry.getAbout().asString())
												   .setResizable(true)
												   .setMinimumWidthFromContent(true)
												   .setExpandRatio(2)
												   .setId("about");
		Grid.Column<V,String> colWho = _gridHistoryEntries.addColumn(entry -> entry.getWhoDetails())
												 .setDescriptionGenerator(entry -> entry.getWho().asString())
												 .setMinimumWidthFromContent(true)
												 .setExpandRatio(2)
												 .setId("who");

		// set selection mode
		_gridHistoryEntries.setSelectionMode(SelectionMode.SINGLE);
		_gridHistoryEntries.setSizeFull();
		_gridHistoryEntries.setHeightByRows(5);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SET ITEMS
/////////////////////////////////////////////////////////////////////////////////////////
	public void setHistoryEntries(final Collection<V> gridItems) {
		ListDataProvider<V> listDataProvider = DataProvider.ofCollection(gridItems);
		_gridHistoryEntries.setDataProvider(listDataProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	I18N
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void updateI18NMessages(final UII18NService i18n) {
		// TODO update i18n
	}
}
