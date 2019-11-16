package r01ui.base.components;

import java.util.Collection;
import java.util.List;

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
public abstract class VaadinListDataProviders {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Provides access to a {@link Collection}-backed {@link DataProvider}
	 * <pre class='brush:java'>
	 * 		Collection<T> items = VaadinListDataProviders.collectionBackedOf(grid)
	 * 												 	 .getUnderlyingItemsCollection();
	 * </pre>
	 * @param <T>
	 * @param hasDataProvider
	 * @return
	 */
	public static <T> VaadinHasDataListProviderAccessor<T> collectionBackedOf(final HasDataProvider<T> hasDataProvider) {
		return new VaadinListDataProviders() { /* nothing */ }
					.new VaadinHasDataListProviderAccessor<T>(hasDataProvider);
	}
	/**
	 * Provides access to a {@link Collection}-backed {@link DataProvider}
	 * <pre class='brush:java'>
	 * 		Collection<T> items = VaadinListDataProviders.collectionBackedOf(combo)
	 * 												 	 .getUnderlyingItemsCollection();
	 * </pre>
	 * @param <T>
	 * @param hasDataProvider
	 * @return
	 */
	public static <T> VaadinFilterableListDataProviderAccessor<T> collectionBackedOf(final HasFilterableDataProvider<T,?> hasDataProvider) {
		return new VaadinListDataProviders() { /* nothing */ }
					.new VaadinFilterableListDataProviderAccessor<T>(hasDataProvider);
	}
	/**
	 * Provides access to a {@link DataProvider}
	 * @param <T>
	 * @param dataProvider
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> VaadinListDataProviderAccessor<T> ofListDataProvider(final DataProvider<T,?> dataProvider) {
		return VaadinListDataProviders.ofListDataProvider((ListDataProvider<T>)dataProvider);
	}
	/**
	 * Provides access to a {@link ListDataProvider}
	 * @param <T>
	 * @param dataProvider
	 * @return
	 */
	public static <T> VaadinListDataProviderAccessor<T> ofListDataProvider(final ListDataProvider<T> dataProvider) {
		return new VaadinListDataProviders() { /* nothing */ }
					.new VaadinListDataProviderAccessor<T>(dataProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class VaadinHasDataListProviderAccessor<T> {
		private final HasDataProvider<T> _hasDataProvider;

		@SuppressWarnings("unchecked")
		public ListDataProvider<T> getDataProvider() {
			return (ListDataProvider<T>)_hasDataProvider.getDataProvider();
		}
		public Collection<T> getUnderlyingItemsCollection() {
			return this.getDataProvider()
					   .getItems();
		}
		public List<T> getUnderlyingItemsCollectionAsList() {
			return (List<T>)this.getUnderlyingItemsCollection();
		}
		public int getUnderlyingItemsCollectionSize() {
			Collection<T> col = this.getUnderlyingItemsCollection();
			return col != null ? col.size() : 0;
		}
		public VaadinHasDataListProviderAccessor<T> refreshItem(final T item) {
			this.getDataProvider()
				.refreshItem(item);
			return this;
		}
		public VaadinHasDataListProviderAccessor<T> refreshAll() {
			this.getDataProvider()
				.refreshAll();
			return this;
		}
		public VaadinHasDataListProviderAccessor<T> setItems(final Collection<T> newItems) {
			// add a new item to the underlying collection
			this.getUnderlyingItemsCollection()
				.clear();
			return this.addNewItems(newItems);
		}
		public VaadinHasDataListProviderAccessor<T> addNewItem(final T item) {
			// add a new item to the underlying collection
			this.getUnderlyingItemsCollection()
				.add(item);
			// refresh
			this.getDataProvider()
				.refreshAll();
			return this;
		}
		public VaadinHasDataListProviderAccessor<T> addNewItems(final Collection<T> newItems) {
			// add a new item to the underlying collection
			this.getUnderlyingItemsCollection()
				.addAll(newItems);
			// refresh
			this.getDataProvider()
				.refreshAll();
			return this;
		}
		public VaadinHasDataListProviderAccessor<T> removeItem(final T item) {
			this.getUnderlyingItemsCollection()
				.remove(item);
			// refresh 
			this.getDataProvider()
				.refreshAll();
			return this;
		}
		public VaadinHasDataListProviderAccessor<T> removeAll() {
			this.getUnderlyingItemsCollection()
				.clear();
			// refresh 
			this.getDataProvider()
				.refreshAll();
			return this;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class VaadinFilterableListDataProviderAccessor<T> {
		private final HasFilterableDataProvider<T,?> _hasDataProvider;

		@SuppressWarnings("unchecked")
		public ListDataProvider<T> getDataProvider() {
			return (ListDataProvider<T>)_hasDataProvider.getDataProvider();
		}
		public Collection<T> getUnderlyingItemsCollection() {
			return this.getDataProvider()
					   .getItems();
		}
		public List<T> getUnderlyingItemsCollectionAsList() {
			return (List<T>)this.getUnderlyingItemsCollection();
		}
		public int getUnderlyingItemsCollectionSize() {
			Collection<T> col = this.getUnderlyingItemsCollection();
			return col != null ? col.size() : 0;
		}
		public VaadinFilterableListDataProviderAccessor<T> refreshItem(final T item) {
			this.getDataProvider()
				.refreshItem(item);
			return this;
		}
		public VaadinFilterableListDataProviderAccessor<T> refreshAll() {
			this.getDataProvider()
				.refreshAll();
			return this;
		}
		public VaadinFilterableListDataProviderAccessor<T> setItems(final Collection<T> newItems) {
			// add a new item to the underlying collection
			this.getUnderlyingItemsCollection()
				.clear();
			return this.addNewItems(newItems);
		}		
		public VaadinFilterableListDataProviderAccessor<T> addNewItem(final T item) {
			// add a new item to the underlying collection
			this.getUnderlyingItemsCollection()
				.add(item);
			// refresh
			this.getDataProvider()
				.refreshAll();
			return this;
		}
		public VaadinFilterableListDataProviderAccessor<T> addNewItems(final Collection<T> newItems) {
			// add a new item to the underlying collection
			this.getUnderlyingItemsCollection()
				.addAll(newItems);
			// refresh
			this.getDataProvider()
				.refreshAll();
			return this;
		}
		public VaadinFilterableListDataProviderAccessor<T> removeItem(final T item) {
			this.getUnderlyingItemsCollection()
				.remove(item);
			// refresh 
			this.getDataProvider()
				.refreshAll();
			return this;
		}
		public VaadinFilterableListDataProviderAccessor<T> removeAll() {
			this.getUnderlyingItemsCollection()
				.clear();
			// refresh 
			this.getDataProvider()
				.refreshAll();
			return this;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class VaadinListDataProviderAccessor<T> {
		private final ListDataProvider<T> _dataProvider;

		public Collection<T> getUnderlyingItemsCollection() {
			return _dataProvider.getItems();
		}
		public List<T> getUnderlyingItemsCollectionAsList() {
			return (List<T>)this.getUnderlyingItemsCollection();
		}
		public int getUnderlyingItemsCollectionSize() {
			Collection<T> col = this.getUnderlyingItemsCollection();
			return col != null ? col.size() : 0;
		}
		public void addNewItem(final T item) {
			// add a new item to the underlying collection
			Collection<T> items = this.getUnderlyingItemsCollection();
			items.add(item);
			// refresh
			_dataProvider.refreshAll();
		}
	}
}
