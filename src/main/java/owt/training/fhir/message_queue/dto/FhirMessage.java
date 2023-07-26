package owt.training.fhir.message_queue.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FhirMessage implements Serializable {

    private static final long serialVersionUID = -7555493202353536234L;

    private String action;

    private String resourceName;

    private String callbackUrl;

    @JsonIgnore
    private Serializable body;
}
