package jatymon.runtime;

import jatymon.annotations.Discardable;
import jatymon.runtime.discardable.DiscardableTransformer;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.matcher.ElementMatcher;
import net.bytebuddy.matcher.ElementMatchers;

import java.lang.instrument.Instrumentation;

public class JaTyMonAgent {

    public static void premain(final String agentArgs, final Instrumentation inst) {
        final ElementMatcher<MethodDescription> discardableMethod = ElementMatchers.isAnnotatedWith(ElementMatchers.nameEndsWith(Discardable.NAME));

        // This is because if there are subclasses, non-overridden methods should remain instrumented
        final ElementMatcher.Junction<TypeDescription> hasDiscardableMethod = ElementMatchers.hasSuperType(ElementMatchers.declaresMethod(discardableMethod));

        // Monitors are subclasses and should not be instrumented
        final ElementMatcher.Junction<TypeDescription> isNotMonitor = ElementMatchers.not(ElementMatchers.isSubTypeOf(Monitor.class));

        new AgentBuilder.Default()
                .type(hasDiscardableMethod.and(isNotMonitor))
                .transform(new DiscardableTransformer())
                .installOn(inst);
    }
}