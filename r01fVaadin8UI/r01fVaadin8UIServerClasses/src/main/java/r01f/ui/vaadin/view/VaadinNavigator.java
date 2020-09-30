package r01f.ui.vaadin.view;

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import com.vaadin.navigator.Navigator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.util.types.collections.CollectionUtils;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinNavigator {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Gets a Vaadin {@link Navigator} url parm
	 * @param paramMap
	 * @param paramName
	 * @return
	 */
	public static String getVaadinViewUrlParamFrom(final Map<String,String> paramMap,
												   final String paramName) {
		return CollectionUtils.hasData(paramMap)
							? paramMap.get(paramName)
							: null;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Creates a Vaadin {@link Navigator} url params from the view id and params
	 *
	 * Starting from Vaadin 8.1 multiple [parameters] can be used separated by & after the [view name]
	 * 		navigateTo(DashBoard.NAME + "/mail=" + email.getValue()+ "&param2=xyz")
	 * [parameters] can be get through ParametersViewChangeEvent.getParameterMap.
 	 * A different separator than & can be used and then call ViewChangeEvent.getParameterMap(separator)
	 * @param viewId
	 * @param navParams
	 * @return
	 */
	public static String vaadinViewUrlPathFragmentOf(final VaadinViewID viewId,final Map<String,String> navParams) {
		String outUrlPathParam = null;
		if (CollectionUtils.hasData(navParams)) {
			StringBuilder paramsStr = new StringBuilder();
			paramsStr.append(viewId)
					 .append("/");
			for (Iterator<Map.Entry<String,String>> meIt = navParams.entrySet().iterator(); meIt.hasNext(); ) {
				Map.Entry<String,String> me = meIt.next();
				paramsStr.append(me.getKey())
				   		 .append("=")
						 .append(me.getValue());
				if (meIt.hasNext()) paramsStr.append("&");
			}
			outUrlPathParam = paramsStr.toString();
		} else {
			outUrlPathParam = viewId.asString();
		}
		return outUrlPathParam;
	}
	/**
	 * Creates a Vaadin {@link Navigator} url params from the view id and params
	 * @param viewId
	 * @param navParams
	 * @return
	 */
	public static String vaadinViewUrlPathParamOf(final VaadinViewID viewId,final Collection<String> navParams) {
		String outUrlPathParam = null;
		if (CollectionUtils.hasData(navParams)) {
			StringBuilder paramsStr = new StringBuilder();
			paramsStr.append(viewId)
					 .append("/");
			for (Iterator<String> meIt = navParams.iterator(); meIt.hasNext(); ) {
				String param = meIt.next();
				paramsStr.append(param);
				if (meIt.hasNext()) paramsStr.append("/");
			}
			outUrlPathParam = paramsStr.toString();
		} else {
			outUrlPathParam = viewId.asString();
		}
		return outUrlPathParam;
	}
}
