package r01f.ui.vaadin.history;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.shared.data.sort.SortDirection;
import com.vaadin.ui.Composite;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.renderers.DateRenderer;

import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.VaadinDates;
import r01f.ui.vaadin.view.VaadinViewI18NMessagesCanBeUpdated;
import r01f.util.types.Dates;


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
		this(i18n, 
			 VaadinHistoryGridConfig.builder()
								    .doNOTShowDetailsColumn()
								    .build());
	}
	
	public VaadinHistoryGrid(final UII18NService i18n,
							 final VaadinHistoryGridConfig config) {
		////////// UI
		// grid
		_configureGrid(i18n, config);
		
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
	private void _configureGrid(final UII18NService i18n,
								final VaadinHistoryGridConfig config) {
		// configure columns
		//Theres a bug formating LocalDateTime see https://bugs.openjdk.java.net/browse/JDK-8085887
		Column<V, Date> colWhen = _gridHistoryEntries.addColumn(entry ->  VaadinDates.dateFrom(entry.getWhen()))
													 .setRenderer(new DateRenderer(new SimpleDateFormat(Dates.DATE_HOURS_FORMATS_BY_LANG.get((i18n.getCurrentLanguage())))))
											 	 	 .setResizable(false)
											 	 	 .setCaption(i18n.getMessage("history.grid.date"))
											 	 	 .setWidth(200)
											 	 	 .setSortable(true)
											 	 	 .setId("when");
		Grid.Column<V,String> colWhat = _gridHistoryEntries.addColumn(entry -> i18n.getMessage(entry.getWhat().getI18nKey()))
												  .setDescriptionGenerator(entry -> entry.getWhat().name())
												  .setResizable(false)
												  .setCaption(i18n.getMessage("history.grid.action"))
												  .setWidth(100)
												  .setSortable(true)
												  .setId("what");
		Grid.Column<V,String> colAbout = _gridHistoryEntries.addColumn(entry -> entry.getAboutDetails())
												   .setDescriptionGenerator(entry -> entry.getAbout().asString())
												   .setResizable(true)
												   .setCaption(i18n.getMessage("history.grid.user"))
												   .setWidthUndefined()
												   .setSortable(true)
												   .setId("about");
		Grid.Column<V,String> colWho = _gridHistoryEntries.addColumn(entry -> entry.getWhoDetails())
												 .setDescriptionGenerator(entry -> entry.getWho().asString())
												 .setCaption(i18n.getMessage("history.grid.item"))
												 .setMinimumWidthFromContent(true)
												 .setExpandRatio(1)
												 .setResizable(true)
												 .setSortable(true)
												 .setId("who");
		if (config.isShowDetailsColumn()) {
			Grid.Column<V,String> colDetails = _gridHistoryEntries.addColumn(entry -> entry.getDetails())
																  .setDescriptionGenerator(entry -> entry.getDetails())
																  .setCaption(i18n.getMessage("history.grid.details"))
																  .setMinimumWidthFromContent(false)
																  .setExpandRatio(1)
																  .setResizable(true)
																  .setSortable(true)
																  .setId("details");
			colWho.setMinimumWidthFromContent(false);
		}


		
		// set selection mode
		_gridHistoryEntries.setSelectionMode(SelectionMode.SINGLE);
		_gridHistoryEntries.setSizeFull();
		_gridHistoryEntries.setHeightByRows(9);
		_gridHistoryEntries.sort(colWhen, SortDirection.DESCENDING);
	}
	public void setHeightByRows(final double rows) {
		_gridHistoryEntries.setHeightByRows(rows);
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
