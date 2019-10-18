package r01f.ui.vaadin.view;

import com.google.common.base.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.locale.Language;
import r01f.ui.viewobject.UIViewObject;

@Accessors(prefix="_")
@NoArgsConstructor @AllArgsConstructor
public class VaadinViewLanguageText
  implements UIViewObject {

    private static final long serialVersionUID = 2910423930147043949L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
    @Getter @Setter private Language _language;
    @Getter @Setter private String _text;
/////////////////////////////////////////////////////////////////////////////////////////
//	EQUALS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
    @Override
	public boolean equals(final Object other) {
    	if (other == null) return false;
    	if (this == other) return true;
    	if (!(other instanceof VaadinViewLanguageText)) return false;
    	VaadinViewLanguageText otherLangText = (VaadinViewLanguageText)other;
    	boolean langEqs = _language != null ? _language.equals(otherLangText.getLanguage())
    										: _language == null && otherLangText.getLanguage() == null ? true
    																			  		 			   : false;
    	boolean textEqs = _text != null ? _text.equals(otherLangText.getText())
    									: _text == null && otherLangText.getText() == null ? true
    																					   : false;
    	return langEqs && textEqs;
    }
    @Override
	public int hashCode() {
    	return Objects.hashCode(_language,_text);
    }
}
