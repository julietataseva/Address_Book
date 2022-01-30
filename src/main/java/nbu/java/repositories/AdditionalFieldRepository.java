package nbu.java.repositories;

import nbu.java.model.pojo.AdditionalField;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AdditionalFieldRepository<A> extends CrudRepository<AdditionalField,Long> {
    List<AdditionalField> findByContactId(int id);

    AdditionalField findById(int id);
}
