package owt.training.fhir.auth.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.fhirpath.IFhirPath;
import ca.uhn.fhir.parser.IParser;

public class FhirContextUtil {

    private static final IFhirPath iFhirPath;

    private static final IParser iParser;

    private FhirContextUtil() {
    }

    static {
        FhirContext ctx = FhirContext.forR5Cached();
        iFhirPath = ctx.newFhirPath();
        iParser = ctx.newJsonParser();
    }

    public static IFhirPath getiFhirPath() {
        return iFhirPath;
    }

    public static IParser getiParser() {
        return iParser;
    }
}
