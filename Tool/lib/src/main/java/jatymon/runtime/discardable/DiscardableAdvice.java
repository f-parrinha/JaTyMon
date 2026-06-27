package jatymon.runtime.discardable;

import jatymon.exceptions.discarding.MonitorDiscardException;
import jatymon.runtime.Monitor;
import net.bytebuddy.asm.Advice;

public class DiscardableAdvice {

    @Advice.OnMethodExit(onThrowable = Exception.class, backupArguments = false)
    public static void onExit(final @Advice.This Object target,
                              @Advice.Thrown(readOnly = false) Throwable throwable) {
        if (!(throwable instanceof MonitorDiscardException) || target instanceof Monitor) {
            return;
        }

        // We catch the exception by setting throwable to null. All other condition throw the exception again
        throwable = null;
    }
}
