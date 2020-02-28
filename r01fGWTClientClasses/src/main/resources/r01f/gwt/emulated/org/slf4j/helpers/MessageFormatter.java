package r01f.gwt.emulated.org.slf4j.helpers; //r01f.gwt.emulated.org.slf4j.helpers;

import java.util.HashSet;
import java.util.Set;

import com.google.gwt.core.client.GWT;

/**
 * Formats messages according to very simple substitution rules. Substitutions
 * can be made 1, 2 or more arguments.
 * For example,
 * <pre class='brush:java'>
 * 		MessageFormatter.format(&quot;Hi {}.&quot;, &quot;there&quot;)
 * </pre>
 *
 * will return the string "Hi there.".
 * <p>
 * The {} pair is called the <em>formatting anchor</em>. It serves to designate
 * the location where arguments need to be substituted within the message
 * pattern.
 * <p>
 * In case your message contains the '{' or the '}' character, you do not have
 * to do anything special unless the '}' character immediately follows '{'. For
 * example,
 *
 * <pre class='brush:java'>
 * 		MessageFormatter.format(&quot;Set {1,2,3} is not equal to {}.&quot;, &quot;1,2&quot;);
 * </pre>
 *
 * will return the string "Set {1,2,3} is not equal to 1,2.".
 *
 * <p>
 * If for whatever reason you need to place the string "{}" in the message
 * without its <em>formatting anchor</em> meaning, then you need to escape the
 * '{' character with '\', that is the backslash character. Only the '{'
 * character should be escaped. There is no need to escape the '}' character.
 * For example,
 *
 * <pre class='brush:java'>
 * 		MessageFormatter.format(&quot;Set \\{} is not equal to {}.&quot;, &quot;1,2&quot;);
 * </pre>
 *
 * will return the string "Set {} is not equal to 1,2.".
 *
 * <p>
 * The escaping behavior just described can be overridden by escaping the escape
 * character '\'. Calling
 *
 * <pre class='brush:java'>
 * MessageFormatter.format(&quot;File name is C:\\\\{}.&quot;, &quot;file.zip&quot;);
 * </pre>
 *
 * will return the string "File name is C:\file.zip".
 *
 * <p>
 * The formatting conventions are different than those of {@link java.text.MessageFormat}
 * which ships with the Java platform. This is justified by the fact that
 * SLF4J's implementation is 10 times faster than that of {@link java.text.MessageFormat}.
 * This local performance difference is both measurable and significant in the
 * larger context of the complete logging processing chain.
 *
 * <p>
 * See also {@link #format(String, Object)},
 * {@link #format(String, Object, Object)} and
 * {@link #arrayFormat(String, Object[])} methods for more details.
 */
public class MessageFormatter {
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////	
    private static final char DELIM_START = '{';
    private static final char DELIM_STOP = '}';
    private static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';
    private static final String ARRAY_JOIN_STR = ", ";
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
    private MessageFormatter() {
    	// no args constructor
    }
    /**
     * Performs single argument substitution for the 'messagePattern' passed as
     * parameter.
     * For example,
     * <pre class='brush:java'>
     * 		MessageFormatter.format(&quot;Hi {}.&quot;, &quot;there&quot;);	
     * </pre>
     * will return the string "Hi there.".
     * @param messagePattern The message pattern which will be parsed and formatted
     * @param arg The argument to be substituted in place of the formatting anchor
     * @return The formatted message
     */
    public static FormattingTuple format(String messagePattern, Object arg) {
        return arrayFormat(messagePattern, arg);
    }
    /**
     * Performs a two argument substitution for the 'messagePattern' passed as
     * parameter.
     * For example,
     * <pre class='brush:java'>
     * 		MessageFormatter.format(&quot;Hi {}. My name is {}.&quot;, &quot;Alice&quot;, &quot;Bob&quot;);
     * </pre>
     * will return the string "Hi Alice. My name is Bob.".
     * @param messagePattern The message pattern which will be parsed and formatted
     * @param arg1 The argument to be substituted in place of the first formatting anchor
     * @param arg2 The argument to be substituted in place of the second formatting anchor
     * @return The formatted message
     */
    public static FormattingTuple format(final String messagePattern,
                                         final Object arg1,final Object arg2) {
        return arrayFormat(messagePattern,arg1,arg2);
    }
/////////////////////////////////////////////////////////////////////////////////////////
// 	
/////////////////////////////////////////////////////////////////////////////////////////
    static Throwable getThrowableCandidate(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return null;
        }
        final Object lastEntry = argArray[argArray.length - 1];
        if (lastEntry instanceof Throwable) {
            return (Throwable) lastEntry;
        }
        return null;
    }
    /**
     * Same principle as the {@link #format(String, Object)} and
     * {@link #format(String, Object, Object)} methods except that any number of
     * arguments can be passed in an array.
     *
     * @param messagePattern The message pattern which will be parsed and formatted
     * @param argArray An array of arguments to be substituted in place of formatting anchors
     * @return The formatted message
     */
    public static FormattingTuple arrayFormat(final String messagePattern,
                                              final Object... argArray) {

        Throwable throwableCandidate = MessageFormatter.getThrowableCandidate(argArray);
        if (messagePattern == null) {
            return new FormattingTuple(null,argArray,throwableCandidate);
        }
        if (argArray == null) {
            return new FormattingTuple(messagePattern);
        }

        StringBuilder sbuf = new StringBuilder(messagePattern.length() + 50);

        Set<Object> seenSet = null;
        int L;
        int i = 0;
        for (L = 0; L < argArray.length; L++) {
            int j = messagePattern.indexOf(DELIM_STR,i);

            if (j == -1) {
                // no more variables
                if (i == 0) { // this is a simple string
                    return new FormattingTuple(messagePattern,argArray,
                            				   throwableCandidate);
                } else { // add the tail string which contains no variables and return
                    // the result.
                    sbuf.append(messagePattern.substring(i,messagePattern.length()));
                    return new FormattingTuple(sbuf.toString(),argArray,
                            				   throwableCandidate);
                }
            } else {
                if (isEscapedDelimeter(messagePattern,j)) {
                    if (!isDoubleEscaped(messagePattern,j)) {
                        L--; // DELIM_START was escaped, thus should not be incremented
                        sbuf.append(messagePattern.substring(i,j - 1));
                        sbuf.append(DELIM_START);
                        i = j + 1;
                    } else {
                        // The escape character preceding the delimiter start is
                        // itself escaped: "abc x:\\{}"
                        // we have to consume one backward slash
                        sbuf.append(messagePattern.substring(i,j - 1));
                        if (seenSet == null) {
                            seenSet = new HashSet<Object>(); // TODO: use IdentityHashSet?
                        }
                        deeplyAppendParameter(sbuf,argArray[L],seenSet);
                        i = j + 2;
                    }
                } else {
                    // normal case
                    sbuf.append(messagePattern.substring(i,j));
                    if (seenSet == null) {
                        seenSet = new HashSet<Object>(); // TODO: use IdentityHashSet?
                    }
                    deeplyAppendParameter(sbuf,argArray[L],seenSet);
                    i = j + 2;
                }
            }
        }
        // append the characters following the last {} pair.
        sbuf.append(messagePattern.substring(i,messagePattern.length()));
        if (L < argArray.length - 1) {
            return new FormattingTuple(sbuf.toString(),argArray,throwableCandidate);
        } else {
            return new FormattingTuple(sbuf.toString(),argArray,null);
        }
    }
    static boolean isEscapedDelimeter(final String messagePattern,
                                      final int delimeterStartIndex) {
        if (delimeterStartIndex == 0) {
            return false;
        }
        char potentialEscape = messagePattern.charAt(delimeterStartIndex - 1);
        return potentialEscape == ESCAPE_CHAR;
    }
    static boolean isDoubleEscaped(final String messagePattern,
                                   final int delimeterStartIndex) {
        return (delimeterStartIndex >= 2
             && messagePattern.charAt(delimeterStartIndex - 2) == ESCAPE_CHAR);
    }

    // special treatment of array values was suggested by 'lizongbo'
    private static void deeplyAppendParameter(final StringBuilder sbuf,final Object o,final Set<Object> seenSet) {
        if (o == null) {
            sbuf.append("null");
        } else if (!o.getClass().isArray()) {
            safeObjectAppend(sbuf, o);
        } else {
            // check for primitive array types because they
            // unfortunately cannot be cast to Object[]
            sbuf.append('[');
            if (o instanceof boolean[]) {
                booleanArrayAppend(sbuf, (boolean[]) o);
            } else if (o instanceof byte[]) {
                byteArrayAppend(sbuf, (byte[]) o);
            } else if (o instanceof char[]) {
                charArrayAppend(sbuf, (char[]) o);
            } else if (o instanceof short[]) {
                shortArrayAppend(sbuf, (short[]) o);
            } else if (o instanceof int[]) {
                intArrayAppend(sbuf, (int[]) o);
            } else if (o instanceof long[]) {
                longArrayAppend(sbuf, (long[]) o);
            } else if (o instanceof float[]) {
                floatArrayAppend(sbuf, (float[]) o);
            } else if (o instanceof double[]) {
                doubleArrayAppend(sbuf, (double[]) o);
            } else {
                objectArrayAppend(sbuf, (Object[]) o, seenSet);
            }
            sbuf.append(']');
        }
    }
    private static void safeObjectAppend(final StringBuilder sbuf,final Object o) {
        try {
            sbuf.append(o.toString());
        } catch (Throwable t) {
            GWT.log("SLF4J: Failed toString() invocation on an object of type ["
                    + o.getClass().getName() + "]", t);
/*
            System.err
                    .println("SLF4J: Failed toString() invocation on an object of type ["
                            + o.getClass().getName() + "]");
            t.printStackTrace();
*/
            sbuf.append("[FAILED toString()]");
        }

    }
    private static void objectArrayAppend(final StringBuilder sbuf,final Object[] a,final Set<Object> seenSet) {
        if (!seenSet.contains(a)) {
            seenSet.add(a);
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                deeplyAppendParameter(sbuf, a[i], seenSet);
                if (i != len - 1) {
                    sbuf.append(ARRAY_JOIN_STR);
                }
            }
            // allow repeats in siblings
            seenSet.remove(a);
        } else {
            sbuf.append("...");
        }
    }
    private static void booleanArrayAppend(final StringBuilder sbuf,final boolean[] a) {
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(ARRAY_JOIN_STR);
            }
        }
    }
    private static void byteArrayAppend(final StringBuilder sbuf,final byte[] a) {
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(ARRAY_JOIN_STR);
            }
        }
    }

    private static void charArrayAppend(final StringBuilder sbuf,final char[] a) {
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(ARRAY_JOIN_STR);
            }
        }
    }
    private static void shortArrayAppend(final StringBuilder sbuf,final short[] a) {
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(ARRAY_JOIN_STR);
            }
        }
    }

    private static void intArrayAppend(final StringBuilder sbuf,final int[] a) {
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(ARRAY_JOIN_STR);
            }
        }
    }
    private static void longArrayAppend(final StringBuilder sbuf,final long[] a) {
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(ARRAY_JOIN_STR);
            }
        }
    }
    private static void floatArrayAppend(final StringBuilder sbuf,final float[] a) {
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(ARRAY_JOIN_STR);
            }
        }
    }
    private static void doubleArrayAppend(final StringBuilder sbuf,final double[] a) {
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sbuf.append(a[i]);
            if (i != len - 1) {
                sbuf.append(ARRAY_JOIN_STR);
            }
        }
    }
}