package r01ui.base.components.grid;

import com.vaadin.data.ValueProvider;
import com.vaadin.ui.Component;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.renderers.AbstractRenderer;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Extension intended to be used with lombok's extension
 * (see https://projectlombok.org/features/experimental/ExtensionMethod)
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinGridExtension {
/////////////////////////////////////////////////////////////////////////////////////////
//	COLUMN PREPEND
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public static <T> Column<T,?> prependColumn(final Grid<T> grid,
									  		 	final String propertyName) {
		Column<T,?> addedColumn = grid.addColumn(propertyName);
		grid.setColumnOrder(addedColumn);	// all columns NOT listed here will remain in the order they were before
		return addedColumn;
	}
	@SuppressWarnings("unchecked")
	public static <T> Column<T,?> prependColumn(final Grid<T> grid,
												final String propertyName,
            						  			final AbstractRenderer<? super T,?> renderer) {
		Column<T,?> addedColumn = grid.addColumn(propertyName,
												 renderer);
		grid.setColumnOrder(addedColumn);	// all columns NOT listed here will remain in the order they were before
		return addedColumn;
	}
	@SuppressWarnings("unchecked")
	public static <T> Column<T,?> prependColumn(final Grid<T> grid,
												final String propertyName,
            					  	  			final AbstractRenderer<? super T,?> renderer,
            					  	  			final Column.NestedNullBehavior nestedNullBehavior) {
		Column<T,?> addedColumn = grid.addColumn(propertyName,
												  renderer,
												  nestedNullBehavior);
		grid.setColumnOrder(addedColumn);	// all columns NOT listed here will remain in the order they were before
		return addedColumn;    	
	}
	@SuppressWarnings("unchecked")
	public static <T,V> Column<T,V> prependColumn(final Grid<T> grid,
												  final ValueProvider<T,V> valueProvider) {
		Column<T,V> addedColumn = grid.addColumn(valueProvider);
		grid.setColumnOrder(addedColumn);	// all columns NOT listed here will remain in the order they were before
		return addedColumn; 
	}
	@SuppressWarnings("unchecked")
	public static <T,V> Column<T,V> prependColumn(final Grid<T> grid,
												  final ValueProvider<T,V> valueProvider,
												  final AbstractRenderer<? super T,? super V> renderer) {
		Column<T,V> addedColumn = grid.addColumn(valueProvider,
												  renderer);
		grid.setColumnOrder(addedColumn);	// all columns NOT listed here will remain in the order they were before
		return addedColumn; 
	}
	@SuppressWarnings("unchecked")
	public static <T,V> Column<T,V> prependColumn(final Grid<T> grid,
												  final ValueProvider<T,V> valueProvider,
												  final ValueProvider<V,String> presentationProvider) {
		Column<T,V> addedColumn = grid.addColumn(valueProvider,
												  presentationProvider);
		grid.setColumnOrder(addedColumn);	// all columns NOT listed here will remain in the order they were before
		return addedColumn; 		
	}
	@SuppressWarnings("unchecked")
	public static <T,V,P> Column<T,V> prependColumn(final Grid<T> grid,
													final ValueProvider<T, V> valueProvider,
													final ValueProvider<V,P> presentationProvider,
													final AbstractRenderer<? super T,? super P> renderer) {
		Column<T,V> addedColumn = grid.addColumn(valueProvider,
												  presentationProvider,
												  renderer);
		grid.setColumnOrder(addedColumn);	// all columns NOT listed here will remain in the order they were before
		return addedColumn;
	}
	@SuppressWarnings("unchecked")
	public static <T,V extends Component> Column<T, V> prependComponentColumn(final Grid<T> grid,
																			  final ValueProvider<T,V> componentProvider) {
		Column<T,V> addedColumn = grid.addComponentColumn(componentProvider);
		grid.setColumnOrder(addedColumn);	// all columns NOT listed here will remain in the order they were before
		return addedColumn;
	}
}
