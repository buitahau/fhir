package owt.training.fhir.util.mapper;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r4.model.*;
import owt.training.fhir.constant.enums.GenderEnum;
import owt.training.fhir.constant.enums.MaritalStatusEnum;
import owt.training.fhir.domain.PatientEntity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

public class PatientMapper {

    public static PatientEntity convert(Patient patient) {
        PatientEntity entity = new PatientEntity();
        if (CollectionUtils.isNotEmpty(patient.getName())) {
            entity.setName(patient.getName().stream().map(HumanName::getFamily).collect(Collectors.joining()));
        }
        if (CollectionUtils.isNotEmpty(patient.getTelecom())) {
            entity.setTelecom(patient.getTelecom().stream().map(ContactPoint::getValue)
                    .collect(Collectors.joining(",")));
        }
        entity.setGender(GenderEnum.fromCode(patient.getGender().toCode()));
        if (CollectionUtils.isNotEmpty(patient.getAddress())) {
            entity.setAddress(patient.getAddress().stream().map(Address::getCity).collect(Collectors.joining()));
        }
        entity.setMaritalStatus(MaritalStatusEnum.fromCode(patient.getMaritalStatus().getCodingFirstRep().getCode()));
        entity.setId(patient.getId());
        return entity;
    }

    public static Patient convert(PatientEntity entity) {
        Patient patient = new Patient();
        patient.setId(entity.getId());

        List<HumanName> names = new ArrayList<>();
        names.add(new HumanName().setFamily(entity.getName()));
        patient.setName(names);

        if (StringUtils.isNotBlank(entity.getTelecom())) {
            List<ContactPoint> contactPoints = new ArrayList<>();
            contactPoints.add(new ContactPoint().setValue(entity.getTelecom()));
            patient.setTelecom(contactPoints);
        }

        if (entity.getGender() != null) {
            patient.setGender(Enumerations.AdministrativeGender.fromCode(entity.getGender().getCode().toLowerCase()));
        }

        if (StringUtils.isNotBlank(entity.getAddress())) {
            List<Address> addresses = new ArrayList<>();
            addresses.add(new Address().setCity(entity.getAddress()));
            patient.setAddress(addresses);
        }

        if (entity.getMaritalStatus() != null) {
            CodeableConcept maritalStatus = new CodeableConcept();
            List<Coding> maritalStatusCodes = new ArrayList<>();
            maritalStatusCodes.add(new Coding().setCode(entity.getMaritalStatus().getCode()));
            maritalStatus.setCoding(maritalStatusCodes);
            patient.setMaritalStatus(maritalStatus);
        }

        Meta meta = new Meta();
        meta.setLastUpdatedElement(new InstantType(Calendar.getInstance()));
        patient.setMeta(meta);

        return patient;
    }
}
