package jatymon.monitor;

import com.palantir.javapoet.JavaFile;
import jatymon.monitor.plans.types.MainClassPlan;
import jatymon.typestate.TypestateData;

import java.util.Objects;

/**
 * Class {@code Monitor} contains all relevant information regarding a generated monitor. It's name, the plan used to generate it,
 * typestate information, JavaFile instance to output...
 *
 * @author Francisco Parrinha
 */
public record MonitorData(String name,
                          MainClassPlan mainClassPlan,
                          TypestateData typestateData,
                          JavaFile javaFile) {
    public static final double DEFAULT_CONFIDENCE_LEVEL = 0.95;

    public String qualifiedName() {
        return typestateData.getPackageName().concat(".").concat(name);
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        return obj instanceof MonitorData(String name1, MainClassPlan classPlan, TypestateData data, JavaFile file) &&
                Objects.equals(this.name, name1) &&
                Objects.equals(this.mainClassPlan, classPlan) &&
                Objects.equals(this.typestateData, data) &&
                Objects.equals(this.javaFile, file);
    }
}
