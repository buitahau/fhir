package owt.training.fhir.util.mapper;

import org.hl7.fhir.r4.model.EpisodeOfCare;
import org.hl7.fhir.r4.model.Period;
import org.hl7.fhir.r4.model.Reference;
import owt.training.fhir.constant.enums.EpisodeOfCareStatusEnum;
import owt.training.fhir.domain.EpisodeOfCareEntity;
import owt.training.fhir.domain.PatientEntity;

import java.util.Date;

public class EpisodeOfCareMapper {
    public static EpisodeOfCareEntity convert(EpisodeOfCare theEpisodeOfCare) {
        EpisodeOfCareEntity entity = new EpisodeOfCareEntity();
        entity.setId(theEpisodeOfCare.getId());
        entity.setPeriodStart(theEpisodeOfCare.getPeriod().getStart().toInstant());
        entity.setPeriodEnd(theEpisodeOfCare.getPeriod().getEnd().toInstant());
        entity.setStatus(EpisodeOfCareStatusEnum.fromCode(theEpisodeOfCare.getStatus().getDisplay()));
        PatientEntity patient = new PatientEntity();
        patient.setId(theEpisodeOfCare.getPatient().getId());
        entity.setPatient(patient);
        return entity;
    }

    public static EpisodeOfCare convert(EpisodeOfCareEntity entity) {
        EpisodeOfCare episodeOfCare = new EpisodeOfCare();
        episodeOfCare.setId(entity.getId());

        Period period = new Period();
        period.setStart(Date.from(entity.getPeriodStart()));
        period.setEnd(Date.from(entity.getPeriodEnd()));
        episodeOfCare.setPeriod(period);

        Reference patientReference = new Reference();
        patientReference.setType("Patient");
        patientReference.setId(entity.getPatient().getId());
        episodeOfCare.setPatient(patientReference);

        episodeOfCare.setStatus(EpisodeOfCare.EpisodeOfCareStatus.fromCode(entity.getStatus().getCode()));

        return episodeOfCare;
    }
}
