package r01ui.base.components.geo;

import r01f.types.geo.GeoPosition;
import r01f.ui.viewobject.UIViewObjectWrappedBase;

public class VaadinViewGeoPosition 
     extends UIViewObjectWrappedBase<GeoPosition> {

	private static final long serialVersionUID = -4291550776181866804L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewGeoPosition() {
		this(new GeoPosition());
	}
	public VaadinViewGeoPosition(final GeoPosition geoPos) {
		super(geoPos);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public String getAddressText() {
		return _wrappedModelObject.getAddressText() != null ? _wrappedModelObject.getAddressText() : "";
	}
	public void setAddressText(final String text) {
		_wrappedModelObject.setAddressText(text);
	}
}
