package r01f.ui.vaadin.clipboard;

import com.vaadin.server.Page;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.util.types.StringEncodeUtils;
import r01f.util.types.Strings;

/**
 * Clipboard usage from vaadin: see https://vaadin.com/forum/thread/17006395/contextmenu-in-grid-how-to-copy-text-from-cell-to-clipboard
 * 
 * Copying to the [clipboard] from vaadin involves some trickery since the java code is executed at the server side
 * and the [clipboard] copying is a CLIENT function
 * [1] - Use a JS function that puts the text to be copied at the [clipboard] as a var in the [Window.sessionStorage] in base64-encoded format
 * [2] - Use a JS function that gets the text to be copied at the [clipboard] from the [Window.sessionStorage] var and copy it to the clipboard
 * 
 * <pre class='brush:java'>
 *		// [copy]
 *		Button btnCopy = new Button(VaadinIcons.COPY);
 *		btnCopy.addStyleName(ValoTheme.BUTTON_BORDERLESS);
 *		btnCopy.setEnabled(false);
 *		btnCopy.setId("btnCopyToClipboard");
 *		btnCopy.addClickListener(event -> {		// [1] - create an script that puts the text into a base64-encoded var 
 *												//		 at the js [Window.sessionStorage]
 *												VaadinClipboard.setTextInJSSessionStorage(this.getValue());	// <<< the value to be copied to the clipboard
 *												
 *												// ... when done, show a notification
 *												Notification.show(i18n.getMessage("copied"));
 *										   });
 *		// [2] - create an script that reads the text from the [Window.sessionStorage] and copies it 
 *		// 		 to the [clipboard]
 *		VaadinClipboard.addCopyToClipboardScriptWhenClikckAnElementWithId("btnCopyToClipboard");			// create the common clipboard copying js functions
 * </pre> 
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class VaadinClipboard {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	public static void setTextInJSSessionStorage(final String textToBeCopiedToTheClipboard) {
		Page.getCurrent().getJavaScript().execute(_composeSetTextInJSSessionStorageStript(textToBeCopiedToTheClipboard));
	}
	public static void addCopyToClipboardScriptWhenClikckAnElementWithId(final String btnId) {
		Page.getCurrent().getJavaScript().execute(_composeCopyToClipboardScript(btnId));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	private static String _composeSetTextInJSSessionStorageStript(final String textToBeCopiedToTheClipboard) {
		 String textToBeCopiedToTheClipboardB64 = StringEncodeUtils.encodeBase64(textToBeCopiedToTheClipboard)
				 												   .toString();
		 return Strings.customized(// put the received data b64-encoded into the sessionStorage
				 				   "sessionStorage.setItem('r01VaadinTextToCopyToClipboard','{}');\n" +
				 				   "console.log('[vaadin-clipboard]: a js session storage var with name [r01VaadinTextToCopyToClipboard] was set with the url');\n",
				 				   textToBeCopiedToTheClipboardB64);
	 }
	 private static String _composeCopyToClipboardScript(final String btnId) {
		 return Strings.customized(// add a click listener on an item with a given id
				 				   "addClickListenerOnItemWithId('{}');\n" +
				 				   // the click listener function:
				 				   //		a) retrieve from the session storage
				 				   //		b) put the data into the clipboard
				 				   "function clickHandlerForVaadinCopyToClipboard() {\n" +
				 				   "	var valBase64 = sessionStorage.getItem('r01VaadinTextToCopyToClipboard');\n" +
				 				   "	var val = atob(valBase64)\n" + 
				 				   "	console.log('[vaadin-clipboard]: a var with name [r01VaadinTextToCopyToClipboard] was retrieved from the js session storage');\n" +
				 				   "	copyTextToClipboard(val);\n" +		 // see http://stackoverflow.com/questions/400212/how-do-i-copy-to-the-clipboard-in-javascript
				 				   "}\n" +
				 				   // add click listener logic
				 				   "function addClickListenerOnItemWithId(itemId) {\n" +
				 				   "	var v = document.getElementById(itemId);\n" +
				 				   "	if (v != null) {\n" +
				 				   "		v.removeEventListener('click',clickHandlerForVaadinCopyToClipboard);\n" +
				 				   "		v.addEventListener('click',clickHandlerForVaadinCopyToClipboard);\n" +
	 							   "	} else {\n" +
	 							   "		console.error('[vaadin-clipboard]: addClickListenerOnItemWithId: Unable to find ' + itemId);\n" +
	 							   "	}\n" +
	 							   "}\n" + 
	 							   // add the copy text to clipboard logic
	 							   // see http://stackoverflow.com/questions/400212/how-do-i-copy-to-the-clipboard-in-javascript
	 							   "function copyTextToClipboard(text) {\n" +
	 							   "	if (!navigator.clipboard) {\n" +
	 							   "		fallbackCopyTextToClipboard(text);\n" +
	 							   "		return;\n" +
	 							   "	}\n" +
	 							   "	navigator.clipboard.writeText(text)\n" +
	 							   "					   .then(function() {\n" +
	 							   "								console.log('[vaadin-clipboard]: Async: Copying to clipboard was successful!');\n" +
	 							   "							 },\n" +
	 							   "							 function(err) {\n" +
	 							   "								console.error('[vaadin-clipboard]: Async: Could not copy text: ', err);\n" +
	 							   "							 });\n" +
	 							   "}\n" +
	 							   "function fallbackCopyTextToClipboard(text) {\n" +
	 							   "	var textArea = document.createElement('textarea');\n" +
	 							   "	textArea.value = text;\n" +
	 							   "	document.body.appendChild(textArea);\n" +
	 							   "	textArea.focus();\n" +
	 							   "	textArea.select();\n" +
	 							   "	try {\n" +
	 							   "		var successful = document.execCommand('copy');\n" +
	 							   "		var msg = successful ? 'successful' : 'unsuccessful';\n" +
	 							   "		console.log('[vaadin-clipboard]: Fallback: Copying text command was ' + msg);\n" +
	 							   "	} catch (err) {\n" +
	 							   "		console.error('[vaadin-clipboard]: Fallback: Oops, unable to copy', err);\n" +
	 							   "	}\n" +
	 							   "	document.body.removeChild(textArea);\n" +
	 							   "}\n",
				 				   btnId);
	 }
}
