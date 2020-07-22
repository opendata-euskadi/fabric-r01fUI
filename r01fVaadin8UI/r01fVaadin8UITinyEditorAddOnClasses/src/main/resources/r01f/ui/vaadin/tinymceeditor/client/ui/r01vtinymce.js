function r01vOnCloseHTMLEditorDialog() { // Bug fixed for IE: focus in input fields is lost when Tinymce is opened in a popup window.
	try {		
		if (top.document.getElementsByClassName('mce-notification').length > 0) {
			top.document.body.removeChild(top.document.getElementsByClassName('mce-notification')[0]);
		};		
		document.getElementById( "r01v-phantom-field" ).focus();
		document.getElementById( "r01v-phantom-field" ).blur();
		
	} catch(e) {console.log(e)}
}