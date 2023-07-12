package owt.training.fhir.util.mapper;

import org.apache.commons.collections4.CollectionUtils;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Practitioner;
import owt.training.fhir.domain.PractitionerEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class PractitionerMapper {
    public static PractitionerEntity convert(Practitioner thePractitioner) {
        PractitionerEntity entity = new PractitionerEntity();
        if (CollectionUtils.isNotEmpty(thePractitioner.getName())) {
            entity.setName(thePractitioner.getName().stream().map(HumanName::getFamily).collect(Collectors.joining()));
        }
        entity.setId(thePractitioner.getId());
        return entity;
    }

    public static Practitioner convert(PractitionerEntity entity) {
        Practitioner practitioner = new Practitioner();
        practitioner.setId(entity.getId());

        List<HumanName> names = new ArrayList<>();
        names.add(new HumanName().setFamily(entity.getName()));
        practitioner.setName(names);

        return practitioner;
    }
}
