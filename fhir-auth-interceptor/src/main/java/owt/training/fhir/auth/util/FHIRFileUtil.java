package owt.training.fhir.auth.util;

import ca.uhn.fhir.parser.IParser;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.hl7.fhir.instance.model.api.IBaseResource;
import owt.training.fhir.auth.exception.FhirVaultException;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FHIRFileUtil {

    private static final IParser iParser;

    static {
        iParser = FhirContextUtil.getiParser().setPrettyPrint(true);
    }

    public static <T extends IBaseResource> T readResource(String path, Class<T> clazz) {
        return readResource(new File(path), clazz);
    }

    public static <T extends IBaseResource> T readResource(File file, Class<T> clazz) {
        try {
            String data = FileUtils.readFileToString(file, "UTF-8");
            return iParser.parseResource(clazz, data);
        } catch (IOException e) {
            throw new FhirVaultException("Error in reading resource", e);
        }
    }

    public static List<File> findFiles(String path, String prefix) {
        return FileUtils.listFiles(new File(path),
                new RegexFileFilter("^(" + prefix + ").*"), null).stream().toList();
    }
}
