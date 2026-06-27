package jatymon.monitor.plans.methods;

import com.palantir.javapoet.*;
import jatymon.monitor.MonitorFactory;
import jatymon.monitor.plans.code.logging.LogInvalidRatioCodePlan;
import jatymon.monitor.plans.code.logging.RemoveRatioLogsCodePlan;
import jatymon.monitor.plans.fields.ActionsCounterFieldPlan;
import jatymon.monitor.plans.fields.ConfidenceLevelFieldPlan;
import jatymon.math.intervals.Interval;
import jatymon.math.JaTyMonMath;
import jatymon.actions.ActionId;

import javax.lang.model.element.Modifier;

/**
 * Class {@code ValidateRatioMethodPlan} is an internal representation of the method responsible for validating and creating
 *  logs for invalid ratios
 * @author Francisco Parrinha
 */
public record ValidateRatioMethodPlan() implements MethodPlan {
    public static final String METHOD_NAME = "validateRatio";

    // Method in-comments
    private static final String CREATE_CI_COMMENT = "\n// Confidence interval calculation\n";
    private static final String VERIFY_RATIO_COMMENT = "\n// Verify ratio with confidence interval\n";

    // Default method variable names
    private static final String ACT_ID = "actionId";
    private static final String STATE_ACT_COUNT = "stateActionsCount";
    private static final String ACT_COUNT = "actionCount";
    private static final String RATIO = "ratio";
    private static final String TRUE_RATIO = "trueRatio";
    private static final String SE = "standardError";
    private static final String CI = "confidenceInterval";

    // Default lib classes' ClassName
    private static final ClassName MATH_UTILS_CLASS = ClassName.get(JaTyMonMath.class);
    private static final ClassName ACT_ID_CLASS = ClassName.get(ActionId.class);
    private static final ClassName INTERVAL_CLASS = ClassName.get(Interval.class);

    @Override
    public MethodSpec emit(final MonitorFactory.BuildContext ctx) {
        return MethodSpec.methodBuilder(METHOD_NAME)
                .addModifiers(Modifier.PRIVATE)
                .returns(TypeName.VOID)
                .addParameter(ParameterSpec.builder(ACT_ID_CLASS, ACT_ID, Modifier.FINAL).build())
                .addParameter(ParameterSpec.builder(TypeName.DOUBLE, TRUE_RATIO, Modifier.FINAL).build())
                .addCode(CodeBlock.builder()
                        .addStatement("$L.sample($L)", ActionsCounterFieldPlan.FIELD_NAME, ACT_ID)
                        .add(CREATE_CI_COMMENT)
                        .addStatement("final int $L = $L.getStateActionsCount($L.stateName())", STATE_ACT_COUNT, ActionsCounterFieldPlan.FIELD_NAME, ACT_ID)
                        .addStatement("final int $L = $L.getActionCount($L)", ACT_COUNT, ActionsCounterFieldPlan.FIELD_NAME, ACT_ID)
                        .addStatement("final double $L = (double) $L / $L", RATIO, ACT_COUNT, STATE_ACT_COUNT)
                        .addStatement("final double $L = $T.getStandardError($L, $L)", SE, MATH_UTILS_CLASS, TRUE_RATIO, STATE_ACT_COUNT)
                        .addStatement("final $T $L = $T.getConfidenceInterval($L, $L, $L)", INTERVAL_CLASS, CI, MATH_UTILS_CLASS, ConfidenceLevelFieldPlan.FIELD_NAME, SE, TRUE_RATIO)
                        .add(VERIFY_RATIO_COMMENT)
                        .beginControlFlow("if ($L.contains($L))", CI, RATIO)
                        .addStatement(new RemoveRatioLogsCodePlan(ACT_ID).emit(ctx))
                        .nextControlFlow("else")
                        .addStatement(new LogInvalidRatioCodePlan(ACT_ID, RATIO, CI).emit(ctx))
                        .endControlFlow()
                        .build())
                .build();
    }
}
