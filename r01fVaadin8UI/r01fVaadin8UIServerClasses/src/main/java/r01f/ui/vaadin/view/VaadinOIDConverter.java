package r01f.ui.vaadin.view;

import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;

import lombok.RequiredArgsConstructor;
import r01f.guids.OID;
import r01f.patterns.FactoryFrom;
import r01f.util.types.Strings;

/**
 * Vaadin OID converter
 * Used at vaadin {@link Binder} like:
 * <pre class='brush:java'>
 *		_binder.forField(txtOid)	// the TextField for the oid
 *			   .asRequired()
 *			   .withConverter(new VaadinOIDConverter<MyOID>(oidStr -> MyOID.forId(oidStr)))
 *			   .bind(MyViewObject::gatOid,MyViewObject::setOid);
 * </pre>
 * @param <O>
 */
@RequiredArgsConstructor
public class VaadinOIDConverter<O extends OID>
  implements Converter<String,O> {
	private static final long serialVersionUID = 5984066841711822014L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS  
/////////////////////////////////////////////////////////////////////////////////////////	
	private final FactoryFrom<String,O> _factory;
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public Result<O> convertToModel(final String value,final ValueContext context) {
		O oid = Strings.isNOTNullOrEmpty(value) 
			 && Strings.isNOTNullOrEmpty(value) 
							? _factory.from(value)
			  			    : null;
		return  Result.ok(oid);
	}

	@Override
	public String convertToPresentation(final O value,final ValueContext context) {
		return value == null ? null
							 : value.asString();
	}

}
