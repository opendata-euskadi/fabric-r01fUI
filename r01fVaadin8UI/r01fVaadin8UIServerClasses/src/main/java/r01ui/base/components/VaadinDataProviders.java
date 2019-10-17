package r01ui.base.components;

import java.util.Collection;

import com.vaadin.data.HasDataProvider;
import com.vaadin.data.HasFilterableDataProvider;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.ui.Grid;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * Utils about vaadin {@link Grid}
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinDataProviders {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
    /**
     * Provides access to a {@link Collection}-backed {@link DataProvider}
     * <pre class='brush:java'>
     * 		Collection<T> items = VaadinDataProviders.collectionBackedOf(grid)
     * 												 .getUnderlyingItemsCollection();
     * </pre>
     * @param <T>
     * @param hasDataProvider
     * @return
     */
    public static <T> VaadinDataProviderAccessor<T> collectionBackedOf(final HasDataProvider<T> hasDataProvider) {
    	return new VaadinDataProviders() { /* nothing */ }
    				.new VaadinDataProviderAccessor<T>(hasDataProvider);
    }
    /**
     * Provides access to a {@link Collection}-backed {@link DataProvider}
     * <pre class='brush:java'>
     * 		Collection<T> items = VaadinDataProviders.collectionBackedOf(combo)
     * 												 .getUnderlyingItemsCollection();
     * </pre>
     * @param <T>
     * @param hasDataProvider
     * @return
     */
    public static <T> VaadinFilterableDataProviderAccessor<T> collectionBackedOf(final HasFilterableDataProvider<T,?> hasDataProvider) {
    	return new VaadinDataProviders() { /* nothing */ }
    				.new VaadinFilterableDataProviderAccessor<T>(hasDataProvider);
    }
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////    
    @RequiredArgsConstructor(access=AccessLevel.PRIVATE)
    public class VaadinDataProviderAccessor<T> {
    	private final HasDataProvider<T> _hasDataProvider;
    	
	    @SuppressWarnings("unchecked")
		public ListDataProvider<T> getDataProvider() {
			return (ListDataProvider<T>)_hasDataProvider.getDataProvider();
	    }
	    public Collection<T> getUnderlyingItemsCollection() {
	    	return this.getDataProvider()
	    			   .getItems();
	    }
	    public void addNewItem(final T item) {
	    	// add a new item to the underlying collection
	    	Collection<T> items = this.getUnderlyingItemsCollection();
	    	items.add(item);
	    	// refresh
	    	this.getDataProvider()
	    		.refreshAll();
	    }
    }
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////    
    @RequiredArgsConstructor(access=AccessLevel.PRIVATE)
    public class VaadinFilterableDataProviderAccessor<T> {
    	private final HasFilterableDataProvider<T,?> _hasDataProvider;
    	
	    @SuppressWarnings("unchecked")
		public ListDataProvider<T> getDataProvider() {
			return (ListDataProvider<T>)_hasDataProvider.getDataProvider();
	    }
	    public Collection<T> getUnderlyingItemsCollection() {
	    	return this.getDataProvider()
	    			   .getItems();
	    }
	    public void addNewItem(final T item) {
	    	// add a new item to the underlying collection
	    	Collection<T> items = this.getUnderlyingItemsCollection();
	    	items.add(item);
	    	// refresh
	    	this.getDataProvider()
	    		.refreshAll();
	    }
    }
}
