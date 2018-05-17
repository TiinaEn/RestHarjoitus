package fi.academy;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface OppilasRepository extends CrudRepository<Oppilas, Integer> {
    Iterable<Oppilas> findByNimiContains(String hakusana);

    @Modifying
    @Query("update Oppilas o set o.koulu = null where o.koulu = :koulu")
        //voidaan vertailla kahta entiteettiä
    void nollaaKaikkiJoillaOnTamaKOulu(@Param("koulu") Koulu koulu);
    //   Iterable<Oppilas> findById(int id);

}
