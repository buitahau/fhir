package owt.training.fhir.util.mapper;

import org.hl7.fhir.r4.model.*;
import owt.training.fhir.constant.enums.EncounterClassCodeEnum;
import owt.training.fhir.constant.enums.EncounterStatusEnum;
import owt.training.fhir.domain.EncounterEntity;
import owt.training.fhir.domain.EpisodeOfCareEntity;
import owt.training.fhir.domain.PractitionerEntity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class EncounterMapper {
    public static EncounterEntity convert(Encounter theEncounter) {
        EncounterEntity entity = new EncounterEntity();
        entity.setStatus(EncounterStatusEnum.fromCode(theEncounter.getStatus().toCode()));
        entity.setClassCode(EncounterClassCodeEnum.fromCode(theEncounter.getClass_().getCode()));
        entity.setPeriodStart(theEncounter.getPeriod().getStart().toInstant());
        entity.setPeriodEnd(theEncounter.getPeriod().getEnd().toInstant());

        EpisodeOfCareEntity episodeOfCare = new EpisodeOfCareEntity();
        episodeOfCare.setId(theEncounter.getEpisodeOfCare().get(0).getId());
        entity.setEpisodeOfCare(episodeOfCare);

        entity.setReasonSystem(theEncounter.getReasonCodeFirstRep().getCodingFirstRep().getSystem());
        entity.setReasonCode(theEncounter.getReasonCodeFirstRep().getCodingFirstRep().getCode());
        entity.setReasonDisplay(theEncounter.getReasonCodeFirstRep().getCodingFirstRep().getDisplay());

        PractitionerEntity practitioner = new PractitionerEntity();
        practitioner.setId(theEncounter.getParticipantFirstRep().getIndividual().getId());
        entity.setPractitioner(practitioner);

        entity.setId(theEncounter.getId());

        return entity;
    }

    public static Encounter convert(EncounterEntity entity) {
        Encounter encounter = new Encounter();
        encounter.setId(entity.getId());
        encounter.setStatus(Encounter.EncounterStatus.fromCode(entity.getStatus().getCode()));

        Coding classCode = new Coding();
        classCode.setCode(entity.getClassCode().name());
        encounter.setClass_(classCode);

        Period period = new Period();
        period.setStart(Date.from(entity.getPeriodStart()));
        period.setEnd(Date.from(entity.getPeriodEnd()));
        encounter.setPeriod(period);

        List<Reference> episodeOfCares = new ArrayList<>();
        Reference episode = new Reference();
        episode.setId(entity.getEpisodeOfCare().getId());
        episode.setType("EpisodeOfCare");
        episodeOfCares.add(episode);
        encounter.setEpisodeOfCare(episodeOfCares);

        List<Encounter.EncounterParticipantComponent> participants = new ArrayList<>();
        Encounter.EncounterParticipantComponent practitionerComponent = new Encounter.EncounterParticipantComponent();
        participants.add(practitionerComponent);

        Reference practitioner = new Reference();
        practitioner.setType("Practitioner");
        practitioner.setId(entity.getPractitioner().getId());
        practitionerComponent.setIndividual(practitioner);
        encounter.setParticipant(participants);

        List<CodeableConcept> reasonCodes = new ArrayList<>();
        CodeableConcept reason = new CodeableConcept();
        reasonCodes.add(reason);

        Coding coding = new Coding();
        reason.addCoding(coding);
        coding.setCode(entity.getReasonCode());
        coding.setDisplay(entity.getReasonDisplay());
        coding.setSystem(entity.getReasonSystem());

        encounter.setReasonCode(reasonCodes);

        return encounter;
    }
}
