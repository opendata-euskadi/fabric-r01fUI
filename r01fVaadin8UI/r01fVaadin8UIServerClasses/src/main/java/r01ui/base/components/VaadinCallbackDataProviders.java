package r01ui.base.components;

import com.vaadin.data.HasDataProvider;
import com.vaadin.data.provider.CallbackDataProvider;
import com.vaadin.data.provider.ConfigurableFilterDataProvider;
import com.vaadin.data.provider.DataProvider;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;


@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinCallbackDataProviders {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Provides access to a {@link CallbackDataProvider}-backed {@link DataProvider}
     * <pre class='brush:java'>
     * 		DataProvider<T,F> dp = VaadinCallbackDataProviders.collectionBackedOf(grid)
     * 												 		  .getDataProvider();
     * </pre>
     * @param <T>
     * @param hasDataProvider
     * @return
     */
	public static <T,F> VaadinCallbackDataProviderAccessor<T,F> ofCallbackDataProvider(final HasDataProvider<T> hasDataProvider) {
		return new VaadinCallbackDataProviders() {/* nothing */}
					.new VaadinCallbackDataProviderAccessor<>(hasDataProvider);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class VaadinCallbackDataProviderAccessor<T,F> {
		private final HasDataProvider<T> _hasDataProvider;
		
		@SuppressWarnings("unchecked")
		public CallbackDataProvider<T,F>  getDataProvider() {
			return (CallbackDataProvider<T,F>)_hasDataProvider.getDataProvider();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public static <T,F> VaadinFiltrableDataProviderAccessor<T,F> ofFiltrableDataProvider(final HasDataProvider<T> hasDataProvider) {
		return new VaadinCallbackDataProviders() {/* nothing */}
					.new VaadinFiltrableDataProviderAccessor<>(hasDataProvider);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class VaadinFiltrableDataProviderAccessor<T,F> {
		private final HasDataProvider<T> _hasDataProvider;
		
		@SuppressWarnings("unchecked")
		public ConfigurableFilterDataProvider<T,Void,F>  getFiltrableDataProvider() {
			return (ConfigurableFilterDataProvider<T,Void,F>)_hasDataProvider.getDataProvider();
		}
		@SuppressWarnings("unchecked")
		public <D extends ConfigurableFilterDataProvider<T,Void,F>> D getFiltrableDataProviderAs(final Class<D> dataProviderType) {
			return (D)_hasDataProvider.getDataProvider();
		}
		@SuppressWarnings("unchecked")
		public CallbackDataProvider<T,F> getCallbackDataProvider() {
			return (CallbackDataProvider<T,F>)_hasDataProvider.getDataProvider();
		}
		@SuppressWarnings("unchecked")
		public <D extends CallbackDataProvider<T,F>> D getCallbackDataProviderAs(final Class<D> dataProviderType) {
			return (D)_hasDataProvider.getDataProvider();
		}
	}
}
