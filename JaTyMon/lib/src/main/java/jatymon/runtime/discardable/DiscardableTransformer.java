package jatymon.runtime.discardable;

import jatymon.annotations.Discardable;
import net.bytebuddy.agent.builder.AgentBuilder;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.matcher.ElementMatchers;
import net.bytebuddy.utility.JavaModule;

import java.security.ProtectionDomain;

public class DiscardableTransformer implements AgentBuilder.Transformer {

    @Override
    public DynamicType.Builder<?> transform(final DynamicType.Builder<?> builder,
                                            final TypeDescription typeDescription,
                                            final ClassLoader classLoader,
                                            final JavaModule module,
                                            final ProtectionDomain protectionDomain) {
        return builder.visit(Advice.to(DiscardableAdvice.class)
                        .on(ElementMatchers.isAnnotatedWith(ElementMatchers.nameEndsWith(Discardable.NAME)))
        );
    }
}