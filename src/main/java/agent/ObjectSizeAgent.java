package agent;

import java.lang.instrument.Instrumentation;

/**
 * Created by thuy on 18/05/16.
 */
public class ObjectSizeAgent {
    private static Instrumentation instrumentation;

    public static void premain(String args, Instrumentation inst) {
        instrumentation = inst;
    }

    public static long getObjectSize(Object o) {
        return instrumentation.getObjectSize(o);
    }
}
