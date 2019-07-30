package r01f.ui.vaadin.view;

import com.google.common.base.Objects;
import com.vaadin.data.Binder;
import com.vaadin.data.Converter;
import com.vaadin.data.Result;
import com.vaadin.data.ValueContext;
import com.vaadin.ui.ComboBox;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.guids.OID;
import r01f.patterns.Provider;

@Accessors(prefix="_")
public class VaadinViewMultiValueItem {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter @Setter private String _id;
	@Getter @Setter private String _value;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewMultiValueItem() {
		// default no-args constructor
	}
	public VaadinViewMultiValueItem(final String id,final String value) {
		_id = id;
		_value = value;
	}
	public <O extends OID> VaadinViewMultiValueItem(final O oid,final String value) {
		this(oid.asString(),value);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Vaadin {@link Converter} to be used when binding the combo at a vaadin {@link Binder}
	 * Usually the view object contains an id for the combo value:
	 * <pre class='brush:java'>
	 * 		@Accessors(prefix="_")
	 * 		public class InvoiceView {
	 * 			@Getter @Setter private String _oid;
	 * 			@Getter @Setter private float _ammount;
	 * 			...
	 * 			@Getter @Setter private String _vendorId;		// this is the combo item id
	 * 			@Getter @Setter private String _vendorName;		// this is the combo item caption
	 * 		}
	 * </pre>
	 * When a {@link ComboBox} is used to select the [vendor] and vaadin {@link Binder} is used to bind the view object
	 * <pre class='brush:java'>
	 * 		// Create the form
	 * 		final TextField txtOid = new TextField();
	 * 		final TextField txtAmmount = new TextField();
	 * 		...
	 * 		final ComboBox<VaadinViewMultiValueItem> cmbVendor = new ComboBox<>();
	 *
	 * 		// bind
	 * 		_binder.forField( txtOid )
	 * 			   .bind(InvoiceView::getOid,InvoiceView::setOid)
	 * 		_binder.forField( txtAmmount )
	 * 			   .bind(InvoiceView::getAmmount,InvoiceView::setAmmount)
	 * 		_binder.forField( cmbVendor )
	 *		  .bind(InvoiceView::getVendorId,InvoiceView::setVendorId)
 	 *		  .withConverter(VaadinViewMultiValueItem.converterFor(() -> _viewObj.getVendorName()))
	 *
	 * </pre>
	 * @param valueProvider
	 * @return
	 */
	public static Converter<VaadinViewMultiValueItem,String> converterFor(final Provider<String> valueProvider) {
		return new Converter<VaadinViewMultiValueItem,String>() {
		  					private static final long serialVersionUID = -7013881683281931641L;

							@Override
							public Result<String> convertToModel(final VaadinViewMultiValueItem cmbValue,
																 final ValueContext context) {
								String id = cmbValue.getId();
								return Result.ok(id);
							}
							@Override
							public VaadinViewMultiValueItem convertToPresentation(final String id,
																				 final ValueContext context) {
								// a value provider is used to get the value from the id
								// ... the value provider can issue a database call to load the value from the DB
								return new VaadinViewMultiValueItem(id,
																   valueProvider.provideValue());
							}
			  	};
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  EQUALS & HASHCODE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (!(obj instanceof VaadinViewMultiValueItem)) return false;
		VaadinViewMultiValueItem other = (VaadinViewMultiValueItem)obj;
		boolean idEq = this.getId() != null && other.getId() != null
							? this.getId().equals(other.getId())
							: this.getId() != null && other.getId() == null
									? false
									: this.getId() == null && other.getId() != null
											? false
											: true;		// both null
		boolean valEq = this.getValue() != null && other.getValue() != null
							? this.getValue().equals(other.getValue())
							: this.getValue() != null && other.getValue() == null
									? false
									: this.getValue() == null && other.getValue() != null
											? false
											: true;		// both null
		return idEq && valEq;
	}
	@Override
	public int hashCode() {
		return Objects.hashCode(_id,_value);
	}
}
