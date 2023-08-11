package owt.training.fhir.repository.elasticsearch;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import owt.training.fhir.domain.elasticsearch.PatientDocument;

@Repository
public interface PatientElasticsearchRepository extends ElasticsearchRepository<PatientDocument, String> {

    Page<PatientDocument> findByName(String name, Pageable pageable);
}
