package r01f.ui.vaadin.view;

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
}
