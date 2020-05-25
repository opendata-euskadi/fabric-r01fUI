package r01f.ui.vaadin.security;

import java.util.Collection;
import java.util.regex.Pattern;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

import r01f.util.types.Strings;

/**
 * Used at r01f's LoginSessionServletFilter to set what resources are filtered or not 
 * at the security servlet filter
 */
public class VaadinLoginSessionServletFilterNotFilteredResourcesPredicate
  implements Predicate<ServletRequest> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	protected final Collection<Pattern> _notFilteredResourceUrlsPatterns;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinLoginSessionServletFilterNotFilteredResourcesPredicate(final String webAppContextName) {
		_notFilteredResourceUrlsPatterns = _notFilteredResourceUrlsPatternsFor(webAppContextName);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean apply(final ServletRequest req) {
		HttpServletRequest request = (HttpServletRequest)req;
		
		boolean outNotFiltered = false;
		
		// if the request "comes" from the [login] page DO NOT filter
		//		while in the [login] page, the url is like /xxxWar/login
		//		... but Vaadin issues ajax requests like /xxxWar/UIDL?xx
		//		these Vaadin ajax request should NOT be filtered since 
		//		we're at the [login] page
		//		... BUT how do we know if the /xxxWar/UIDL request comes from 
		//		the [login] (not protected] page or from another protected page???
		//		The solution is to use the [referer] header
		if (request.getHeader("Referer") != null && request.getHeader("Referer").contains("login") 
		 && request.getRequestURI().contains("UIDL")) {
			outNotFiltered = true;	// do not filter
		}
		
		// Do NOT filter common Vaadin resources
		if (!outNotFiltered) {
			String reqUri = request.getRequestURI();
			for (Pattern notFilteredPattern : _notFilteredResourceUrlsPatterns) {
				if (notFilteredPattern.matcher(reqUri).matches()) {
					outNotFiltered = true;	// not filter
					break;
				}
			}
		}
		return outNotFiltered;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	private static Collection<Pattern> _notFilteredResourceUrlsPatternsFor(final String webAppContextName) {
		return Lists.newArrayList(Pattern.compile(Strings.customized("/{}/VAADIN/?.*",
																	 webAppContextName)),
//						 		 Pattern.compile(Strings.customized("/{}/UIDL/?.*",
//						 				   							R01UIConstants.WEB_APP_NAME)),						 		 
						 		 Pattern.compile(Strings.customized("/{}/login/?",		// BEWARE! do not make this too loose
						 				   							webAppContextName)));
	}
}
