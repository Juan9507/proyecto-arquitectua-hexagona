package co.com.sofka.arquitectura.limpia.jpa.persona;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonaDataRepository extends CrudRepository<PersonaData, Integer>, QueryByExampleExecutor<PersonaData> {

}
