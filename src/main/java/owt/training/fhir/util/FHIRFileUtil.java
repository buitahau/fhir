package owt.training.fhir.util;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.apache.commons.io.FileUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;

import java.io.File;
import java.io.IOException;

public class FHIRFileUtil {

    private static final IParser iParser;

    static {
        FhirContext context = FhirContext.forR5Cached();
        iParser = context.newJsonParser().setPrettyPrint(true);
    }

    public static <T extends IBaseResource> T readResource(String path, Class<T> clazz) {
        File file = new File(path);
        try {
            String data = FileUtils.readFileToString(file, "UTF-8");
            return iParser.parseResource(clazz, data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
