package r01f.gwt.util;

public class GWTReflection {
	/**
	 * Checks if a type is assignable from otherType
	 * GWT does NOT support reflection methods
	 * @param type
	 * @param otherType
	 * @return
	 */
	public static boolean isAsignableFrom(final Class<?> type,
										  final Class<?> otherType) {
        if (type == null || otherType == null) return false;

        if (otherType.equals(type)) return true;

        Class<?> otherTypeSuperClass = otherType.getSuperclass();
        while (otherTypeSuperClass != null) {
            if (otherTypeSuperClass.equals(type)) return true;
            otherTypeSuperClass = otherTypeSuperClass.getSuperclass();
        }
        return false;		
	}
}
