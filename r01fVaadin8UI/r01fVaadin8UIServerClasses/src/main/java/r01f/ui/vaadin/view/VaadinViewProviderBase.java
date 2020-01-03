package r01f.ui.vaadin.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewProvider;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class VaadinViewProviderBase
  		   implements ViewProvider {

	private static final long serialVersionUID = 4980630595486553732L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public interface ProvidesVaadinViews {
		public <V extends View> V provideViewOf(final Class<V> viewType);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	protected final transient ProvidesVaadinViews _viewProvider;
	
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	protected VaadinViewProviderBase(final ProvidesVaadinViews viewProvider) {
		_viewProvider = viewProvider;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public String getViewName(final String viewAndParameters) {
		String[] splitViewAndParameters = viewAndParameters.split( "/" );
		return splitViewAndParameters[0];
	}
	@Override
	public View getView(final String viewName) {
		log.info("...going to [{}] view",viewName);
		Class<? extends View> viewType = this.getViewTypeFor(viewName);
		if (viewType == null) throw new IllegalArgumentException("NO vaadin view for " + viewName);
		return _viewProvider.provideViewOf(viewType);
	}
	protected abstract Class<? extends View> getViewTypeFor(final String viewName);
}
