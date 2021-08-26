package r01f.ui.vaadin.history;

import java.time.LocalDateTime;

import lombok.experimental.Accessors;
import r01f.model.facets.HistoryTrackedObjectOID;
import r01f.model.history.HistoryActionAboutObject;
import r01f.model.history.HistoryEntry;
import r01f.model.history.oids.HistoryOIDs.HistoryEntryOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.ui.i18n.UII18NService;
import r01f.ui.vaadin.VaadinDates;
import r01f.ui.viewobject.UIViewObjectWrappedBase;

@Accessors(prefix="_")
public abstract class VaadinViewHistoryEntry<A extends HistoryActionAboutObject,
							  				 O extends HistoryTrackedObjectOID,H extends HistoryEntry<A,O>>
	 		  extends UIViewObjectWrappedBase<H> {

	private static final long serialVersionUID = -2129390001586971529L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final String _aboutDetails;
	private final String _whoDetails;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public VaadinViewHistoryEntry(final H historyEntry,
								  final String aboutDetails,final String whoDetails) {
		super(historyEntry);
		_aboutDetails = aboutDetails;
		_whoDetails = whoDetails;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	OID 
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String OID_FIELD = "oid";
	public HistoryEntryOID getOid() {
		return _wrappedModelObject.getOid();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	WHAT / ABOUT / WHO / WHEN
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String WHAT_FIELD = "what";
	public A getWhat() {
		return _wrappedModelObject.getWhat();
	}
	public String getWhatUsing(final UII18NService i18n) {
		return i18n.getMessage(this.getWhat().getI18nKey());
	}
	
	public static final String ABOUT_FIELD = "about";
	public O getAbout() {
		return _wrappedModelObject.getAbout();
	}
	
	public static final String ABOUT_DETAILS_FIELD = "aboutDetails";
	public String getAboutDetails() {
		return _aboutDetails;
	}
	
	public static final String WHO_FIELD = "who";
	public UserOID getWho() {
		return _wrappedModelObject.getWho();
	}
	
	public static final String WHO_DETAILS_FIELD = "whoDetails";
	public String getWhoDetails() {
		return _whoDetails;
	}
	
	public static final String WHEN_FIELD = "when";
	public LocalDateTime getWhen() {
		return VaadinDates.localDateTimeFrom(_wrappedModelObject.getWhen());
	}
	public static final String DETAILS_FIELD = "details";
	public String getDetails() {
		return _wrappedModelObject.getDetails();
	}	
}
