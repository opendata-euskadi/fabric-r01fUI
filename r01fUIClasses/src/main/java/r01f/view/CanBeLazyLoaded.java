package r01f.view;

import r01f.types.lazy.LazyLoaded;



/**
 * Interface for the view components that can be lazyly loaded 
 * Exposes methods for giving chance to the interface to display a loading... indicator while lazy loading is in progress
 */
public interface CanBeLazyLoaded
	     extends LazyLoaded {
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @return true if the view component is being loaded lazyly
	 */
	public boolean isLazyLoaded();
	/**
	 * Sets if the component is being loaded lazyly
	 * @param lazy true if the component is loaded lazyly
	 */
	public void setLazyLoaded(boolean lazy);
}
