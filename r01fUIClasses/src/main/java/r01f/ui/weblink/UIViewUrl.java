package r01f.ui.weblink;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.patterns.Factory;
import r01f.patterns.FactoryFrom;
import r01f.types.url.Url;

/**
 * The view object
 */
@Accessors(prefix="_")
@NoArgsConstructor @AllArgsConstructor 
public class UIViewUrl
  implements IsUIViewUrl {

	private static final long serialVersionUID = 7933615564586349838L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////		
	public static final String URL_FIELD = "url";		

	public static final Factory<UIViewUrl> FACTORY =  new Factory<UIViewUrl>() {
																@Override
																public UIViewUrl create() {
																	return new UIViewUrl(Url.from("")); // an empty url
																}
													  };
	public static final FactoryFrom<String,UIViewUrl> FACTORY_FROM_STRING = new FactoryFrom<String,UIViewUrl>() {
																					@Override
																					public UIViewUrl from(final String s) {
																						return new UIViewUrl(Url.from(s)); 
																					}
																			};
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter @Setter protected Url _url;
	
}