package r01ui.base.components.grid;

import java.util.Collection;

import com.vaadin.ui.Grid;

import r01f.ui.viewobject.UIViewObject;

/**
 * Provides vaadin's {@link Grid} columns
 * <pre class='brush:java'>
 * 		Grid<MyViewObj> theGrid = new Grid<>();
 * 		VaadinGridColumnProvider<MyViewOjb> gridColProvider = (grid) -> {
 * 																	Collection<Grid.Column<V,?>> outCols = Lists.newArrayList();
 * 																	outCols.add(grid.addColumn(MyViewObj::getMyField));
 * 																	outCols.add(grid.addColumn(MyViewObj::getMyOtherField));
 * 																	...
 * 																	return outCols;
 * 															  };
 * 		
 * </pre>
 * @param <V>
 */
@FunctionalInterface
public interface VaadinGridColumnProvider<V extends UIViewObject> {
	
	public Collection<Grid.Column<V,?>> provideColumnsFor(final Grid<V> grid);
	
	/**
	 * Utility method to create a {@link VaadinGridColumnProvider} using [view object]'s property names
	 * BEWARE!	In order to use this {@link VaadinGridColumnProvider}, the vaadin {@link Grid} MUST have been created 
	 * 			with the [view object] type: Grid<MyViewObj> grid = new Grid<>(MyViewObj.class);
	 * @param <V>
	 * @param viewObjPropertyNames
	 * @return
	 */
	public static <V extends UIViewObject> VaadinGridColumnProvider<V> createColumnProviderUsingViewObjectPropertiesWithNames(final String... viewObjPropertyNames) {
		return (grid) -> {
					grid.setColumns(viewObjPropertyNames);
					return grid.getColumns();
			   };
	}
}
