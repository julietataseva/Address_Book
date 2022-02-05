package nbu.java.repositories;

import nbu.java.model.pojo.Contact;
import nbu.java.model.pojo.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository<C> extends CrudRepository<Contact,Long> {
    List<Contact> findAll();

    List<Contact> findByUserId(int id);

    Contact findById(int id);

    List<Contact> findByFirstNameAndLastNameAndUserId(String firstName, String lastName, int id);

   @Query(value = "Select * from (SELECT * FROM contacts WHERE user_id = :id) as t Where first_name IN " +
           "(SELECT first_name FROM (SELECT * FROM contacts WHERE user_id = :id) as t GROUP BY first_name HAVING COUNT(distinct last_name) > 1)", nativeQuery = true)
   List<Contact> findBySameFirstNameAndDistinctLastName(int id);

    @Query(value = "Select * from (SELECT * FROM contacts WHERE user_id = :id) as t Where last_name IN " +
            "(SELECT last_name FROM (SELECT * FROM contacts WHERE user_id = :id) as t GROUP BY last_name HAVING COUNT(distinct first_name) > 1)", nativeQuery = true)
    List<Contact> findBySameLastNameAndDistinctFirstName(int id);
}
