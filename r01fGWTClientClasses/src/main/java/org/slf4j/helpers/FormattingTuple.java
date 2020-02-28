package org.slf4j.helpers; //r01f.gwt.emulated.org.slf4j.helpers;

import java.util.Arrays;

import lombok.Getter;
import lombok.experimental.Accessors;

/**
 * Holds the results of formatting done by {@link MessageFormatter}.
 */
@Accessors(prefix="_")
public class FormattingTuple {
/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
    @Getter private String _message;
    @Getter private Throwable _throwable;
    @Getter private Object[] _argArray;
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
    public FormattingTuple(final String message) {
        this(message, 
        	 null,
        	 null);
    }
    public FormattingTuple(final String message,final Object[] argArray,final  Throwable throwable) {
        _message = message;
        _throwable = throwable;
        if (throwable == null) {
            _argArray = argArray;
        } else {
            _argArray = trimmedCopy(argArray);
        }
    }
/////////////////////////////////////////////////////////////////////////////////////////
// 	
/////////////////////////////////////////////////////////////////////////////////////////    
    static Object[] trimmedCopy(final Object[] argArray) {
        if(argArray == null || argArray.length == 0) {
            throw new IllegalStateException("non-sensical empty or null argument array");
        }
        return Arrays.copyOf(argArray,argArray.length - 1);
/*
        final int trimemdLen = argArray.length -1;
        Object[] trimmed = new Object[trimemdLen];
        System.arraycopy(argArray, 0, trimmed, 0, trimemdLen);
        return trimmed;
*/
    }
}