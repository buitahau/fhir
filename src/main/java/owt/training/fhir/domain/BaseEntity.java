package owt.training.fhir.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @Id
    @Column(name = "id", columnDefinition = "VARCHAR(36)", nullable = false)
    private String id = UUID.randomUUID().toString();
}
