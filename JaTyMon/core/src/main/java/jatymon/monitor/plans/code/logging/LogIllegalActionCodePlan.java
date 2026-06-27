package jatymon.monitor.plans.code.logging;

import com.palantir.javapoet.ClassName;
import com.palantir.javapoet.CodeBlock;
import jatymon.actions.ActionType;
import jatymon.logging.logs.action.IllegalActionLog;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.code.CodePlan;
import jatymon.monitor.plans.fields.CurrentStateFieldPlan;
import jatymon.monitor.plans.fields.LoggerFieldPlan;
import jatymon.monitor.plans.fields.TypestateNameFieldPlan;
import jatymon.monitor.plans.types.StatesEnumPlan;

/**
 * Used to write a new log call for illegal action logs.
 * @param actionName action name
 */
public record LogIllegalActionCodePlan(String actionName, ActionType actionType) implements CodePlan {

    @Override
    public CodeBlock emit(final MonitorFactory.BuildContext ctx) {
        final CodeBlock firstState = CodeBlock.of("$T.$L", ctx.getClassName(StatesEnumPlan.ENUM_NAME), ctx.typestateData().getGraph().getStart().getName());
        return CodeBlock.builder().add("$L.log(new $T($L, $L.toString(), $S, $T.$L))",
                        LoggerFieldPlan.FIELD_NAME,
                        ClassName.get(IllegalActionLog.class),
                        TypestateNameFieldPlan.FIELD_NAME,
                        CurrentStateFieldPlan.getGetterCode(ctx.typestateData().getKey(), firstState),
                        actionName,
                        ClassName.get(ActionType.class),
                        actionType)
                .build();
    }
}
