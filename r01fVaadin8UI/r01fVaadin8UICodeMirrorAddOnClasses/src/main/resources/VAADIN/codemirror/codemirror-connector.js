window.r01f_ui_vaadin_codemirror_CodeMirrorField = function() {

	var connector = this;
	var gap = 0;
	var sends = [];
	var e = connector.getElement();
	
	var readOnly = false;
	if (connector.getState().readOnly === 'TRUE') {
		readOnly = true;
	} else if (connector.getState().readOnly === 'NOCURSOR') {
		readOnly = 'nocursor';
	}
	
	var config = {
		value : connector.getState().value,
		mode : connector.getState().mode,
		indentUnit : connector.getState().indentUnit,
	    lineNumbers: connector.getState().lineNumbers,
	    lineWrapping: connector.getState().lineWrapping,
	    foldGutter: connector.getState().foldGutter,
	    gutters: connector.getState().gutters,
	    readOnly: readOnly,
	};
	// previous synced value
	connector.value = connector.getState().value;
	connector.internalValueChange = false;
	connector.cm = new CodeMirror(e, config);
	connector.valuePropagationTimeout = null;
	connector.lazyValuePropagationInterval = null;
	connector.sizeCheckTimeout = null;

	// workaround to react to the new size after Vaadin's layouting phase
	sizeCheckTimeout = window.setTimeout(function() {
		connector.checkSize();
	}, 200);
	
	connector.autoFoldImports = function () {
		for (var i = 0; i < connector.cm.lastLine(); ++i) {
			if (connector.cm.getLine(i).trim().lastIndexOf("import", 0) === 0) {
			    connector.cm.foldCode(CodeMirror.Pos(i, 0));
				break;
			}
		}
	}
	
	if (config.mode === "text/x-java") connector.autoFoldImports();

	connector.cm.on("changes", function() {
		if (connector.internalValueChange) {
			connector.value = connector.cm.getValue();	// FIX by PCI
			return;
		}
		
		if (connector.getState().valueChangeMode === 'EAGER') {
		
			if (connector.valuePropagationTimeout != null) {
				window.clearTimeout(connector.valuePropagationTimeout);
				connector.valuePropagationTimeout = null;
			}
		
			connector.valuePropagationTimeout = window.setTimeout(function() {
				connector.value = connector.cm.getValue();
				if (gap < 2)
					gap++;
				connector.onValueChange(connector.value);
			}, 20);
			
		}		
	});
	
	connector.cm.on("blur", function() {
		if (connector.getState().valueChangeMode === 'BLUR') {
			connector.valuePropagationTimeout = window.setTimeout(
				function() {
					if (connector.value != connector.cm.getValue()) {
						connector.value = connector.cm.getValue();
						connector.onValueChange(connector.value);
					}
				}, 
				20
			);
		}
	});
	
	if (connector.getState().valueChangeMode === 'LAZY' || connector.getState().valueChangeMode === 'TIMEOUT') {
		connector.lazyValuePropagationInterval = window.setInterval(
			function () {
				if (connector.value != connector.cm.getValue()) {
					connector.value = connector.cm.getValue();
					connector.onValueChange(connector.value);
				}
			},
			connector.getState().valueChangeTimeout
		);
	}

	connector.checkSize = function () {
		if (connector.cm.display.lastWrapHeight != connector.cm.display.wrapper.clientHeight) {
			connector.cm.refresh();
		}
	};
	connector.onStateChange = function() {
		var state = connector.getState();
		var oldValue = connector.value;
		if ((connector.cm.getValue() != connector.getState().value) && gap == 0 ) {
			connector.internalValueChange = true;
			connector.cm.setValue(state.value);
			connector.internalValueChange = false;
		}
		if (gap > 0)
			gap--;
	};

};