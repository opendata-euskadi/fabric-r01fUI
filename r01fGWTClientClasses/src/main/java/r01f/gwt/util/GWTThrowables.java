package r01f.gwt.util;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.google.gwt.event.shared.UmbrellaException;

public class GWTThrowables {
	/**
	 * This simply logs an exception at the client-side
	 * @param e the exception
	 */
	public static void log(final Throwable e) {
		Throwable t = GWTThrowables.unwrap(e);
		Logger.getLogger("client").log(Level.SEVERE,"Exception caught in client: ",t);
	}
	/**
	 * Sometimes GWT exceptions as {@link UmbrellaException}. This method unwraps the {@link UmbrellaException}
	 * to find the original exception
	 * @param e exception
	 * @return the original exception
	 */
	public static Throwable unwrap(final Throwable e) {   
		if (e instanceof UmbrellaException) {   
			UmbrellaException ue = (UmbrellaException)e;  
			if (ue.getCauses().size() == 1) {   
				return unwrap(ue.getCauses().iterator().next());  
			}  
		}  
		return e;  
	}
}
