package space.astrocraft.astrobb;

public class Trace {
    public static String last() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        if (stackTrace.length > 1) {
            StackTraceElement element = stackTrace[3];
            return String.format("%s.%s:%d", element.getClassName(), element.getMethodName(), element.getLineNumber());
        }

        return "Class.method:??";
    }
}
