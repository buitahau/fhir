package owt.training.fhir.provider;

import ca.uhn.fhir.rest.api.server.IBundleProvider;
import ca.uhn.fhir.rest.server.IResourceProvider;
import ca.uhn.fhir.rest.server.exceptions.InvalidRequestException;
import ca.uhn.fhir.validation.FhirValidator;
import ca.uhn.fhir.validation.SingleValidationMessage;
import ca.uhn.fhir.validation.ValidationResult;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IPrimitiveType;
import org.hl7.fhir.r4.model.DomainResource;
import org.hl7.fhir.r4.model.InstantType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import owt.training.fhir.domain.BaseEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseProvider implements IResourceProvider {
    private FhirValidator fhirValidator;

    protected BaseProvider(FhirValidator fhirValidator) {
        this.fhirValidator = fhirValidator;
    }

    protected void validate(DomainResource domainResource) {
        ValidationResult validationResult = fhirValidator.validateWithResult(domainResource);
        if (validationResult.isSuccessful()) {
            return;
        }

        throw new InvalidRequestException(buildErrorMessage(validationResult.getMessages()));
    }

    private String buildErrorMessage(List<SingleValidationMessage> messages) {
        StringBuilder errorMessageBuilder = new StringBuilder();
        messages.forEach(message -> errorMessageBuilder.append(message.getLocationString())
                .append(" - ")
                .append(message.getMessage())
                .append("."));
        return errorMessageBuilder.toString();
    }

    protected <T extends BaseEntity> IBundleProvider buildIBundleProvider(Page<T> result,
                                                                          Function<T, IBaseResource> mapper) {

        return new IBundleProvider() {
            @Override
            public IPrimitiveType<Date> getPublished() {
                return InstantType.withCurrentTime();
            }

            @Nonnull
            @Override
            public List<IBaseResource> getResources(int theFromIndex, int theToIndex) {
                return result.get().map(mapper).collect(Collectors.toList());
            }

            @Nullable
            @Override
            public String getUuid() {
                return null;
            }

            @Override
            public Integer preferredPageSize() {
                return null;
            }

            @Nullable
            @Override
            public Integer size() {
                return result.getTotalPages();
            }
        };
    }

    protected Pageable buildPageable(Integer offset, Integer count) {
        return PageRequest.of(offset != null ? offset : 0, count != null ? count : 20);
    }
}
