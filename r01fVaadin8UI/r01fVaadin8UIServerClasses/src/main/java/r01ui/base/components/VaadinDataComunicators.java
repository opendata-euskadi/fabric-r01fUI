package r01ui.base.components;

import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.vaadin.data.provider.HierarchicalDataCommunicator;
import com.vaadin.data.provider.HierarchicalDataProvider;
import com.vaadin.data.provider.HierarchicalQuery;
import com.vaadin.ui.Grid;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinDataComunicators {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static <T> Collection<T> allItemsOf(final Grid<T> grid) {
		// fetch all
		if (grid.getDataCommunicator() instanceof HierarchicalDataCommunicator<?>) {
			HierarchicalDataCommunicator<T> communicator = (HierarchicalDataCommunicator<T>)grid.getDataCommunicator();
			communicator.fetchItemsWithRange(0,communicator.getDataProviderSize())
						.stream()
						.forEach(item -> {
									if (communicator.hasChildren(item)) {
										// expand
										communicator.expand(item, 
															false);// sync and refresh
										// recurse!
										_expandRecursively(grid,
														   _childsOf(grid,
																   	 item),		// parent
														   Integer.MAX_VALUE);
									}
								});
		}
		return grid.getDataCommunicator()
				   .fetchItemsWithRange(0,grid.getDataCommunicator().getDataProviderSize())
				   .stream()
				   .collect(Collectors.toList());
	}
	protected static <T> void _expandRecursively(final Grid<T> grid,
												 final Stream<T> items,
								  	  			 final int depth) {
		if (depth < 0) return;
		HierarchicalDataCommunicator<T> dataCommunicator = (HierarchicalDataCommunicator<T>)grid.getDataCommunicator();
		items.forEach(item -> {
							if (dataCommunicator.hasChildren(item)) {
								// expand
								dataCommunicator.expand(item, 
														false);	// sync and refresh
								// recurse!
								_expandRecursively(grid,
												   _childsOf(grid,
															 item),		// parent
												   depth - 1);
							}
					  });
	}
	private static <T> Stream<T> _childsOf(final Grid<T> grid,
										   final T item) {
		HierarchicalDataProvider<T,?> hierarchicalDataProvider = (HierarchicalDataProvider<T,?>)grid.getDataProvider();
		return hierarchicalDataProvider.fetchChildren(new HierarchicalQuery<>(null,		// filter
																			  item));	// parent
	}
}
