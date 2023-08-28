package owt.training.fhir.auth.dto.wrapper;

import ca.uhn.fhir.rest.server.interceptor.auth.PolicyEnum;
import org.apache.commons.collections4.CollectionUtils;
import org.hl7.fhir.r5.model.CodeableConcept;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ListVerdictWrapper {

    private final List<VerdictWrapper> verdictWrappers = new ArrayList<>();

    public void addVerdictWrapper(VerdictWrapper verdictWrapper) {
        verdictWrappers.add(verdictWrapper);
    }

    public boolean isAllMatch(PolicyEnum policyEnum) {
        if (CollectionUtils.isEmpty(verdictWrappers)) {
            return false;
        }
        return verdictWrappers.stream().allMatch(verdict -> policyEnum == verdict.getTheDecision());
    }

    public Set<CodeableConcept> getAllowActions() {
        return getActions(PolicyEnum.ALLOW, verdictWrapper -> verdictWrapper.getAllowActions().stream());
    }

    public Set<CodeableConcept> getDenyActions() {
        return getActions(PolicyEnum.DENY, verdictWrapper -> verdictWrapper.getDenyActions().stream());
    }

    private Set<CodeableConcept> getActions(PolicyEnum policyEnum,
                                            Function<VerdictWrapper, Stream<CodeableConcept>> flatmapFunctional) {

        return verdictWrappers
                .stream()
                .filter(verdictWrapper -> policyEnum == verdictWrapper.getTheDecision())
                .flatMap(flatmapFunctional)
                .collect(Collectors.toSet());
    }
}
