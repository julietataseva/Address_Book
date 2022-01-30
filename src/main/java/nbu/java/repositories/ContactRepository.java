package nbu.java.repositories;

import nbu.java.model.pojo.Contact;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ContactRepository<C> extends CrudRepository<Contact,Long> {
    List<Contact> findAll();

    List<Contact> findByUserId(int id);

    Contact findById(int id);

    List<Contact> findByFirstNameAndLastName(String firstName, String lastName);

   @Query("SELECT c FROM Contact c WHERE c.firstName IN (SELECT firstName FROM Contact GROUP BY firstName HAVING COUNT(distinct lastName) = 1)")
   List<Contact> findBySameFirstNameAndDistinctLastName();

    @Query("SELECT c FROM Contact c WHERE c.lastName IN (SELECT lastName FROM Contact GROUP BY lastName HAVING COUNT(distinct firstName) = 1)")
    List<Contact> findBySameLastNameAndDistinctFirstName();
}
