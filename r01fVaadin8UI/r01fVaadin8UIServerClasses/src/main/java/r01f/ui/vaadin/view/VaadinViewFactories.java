package r01f.ui.vaadin.view;

import com.vaadin.ui.AbstractComponent;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.ui.i18n.UII18NService;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinViewFactories {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Interface for factory types
	 * @param <V> the view type to create
	 */
	public interface R01UIViewFactory<V extends AbstractComponent> {
		/**
		 * @return an instance of type T built from other
		 */
		public V from(final UII18NService i18n);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Interface for factory types
	 * @param <T> the type neede to create the view
	 * @param <V> the view type to create
	 */
	public interface R01UIViewFactoryFrom<T,V extends AbstractComponent> {
		/**
		 * @return an instance of type T built from other
		 */
		public V from(final UII18NService i18n,
					  final T other);
	}
}
