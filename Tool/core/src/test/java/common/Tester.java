package common;

import jatymon.diagnostics.AbstractDiagnostic;

import java.io.IOException;
import java.util.List;

public interface Tester {
    void success(final String... filesPath) throws IOException;
    void success(final boolean isSilent, final String... filesPath) throws IOException;
    void failure(final String... filesPath) throws IOException;
    void failure(final boolean isSilent, final String... filesPath) throws IOException;
    void hasDiagnostic(final List<String> filesPath, final List<Class<? extends AbstractDiagnostic>> diagnostics) throws IOException;
    void hasDiagnostic(final boolean isSilent, final List<String> filesPath, final List<Class<? extends AbstractDiagnostic>> diagnostics) throws IOException;
}
