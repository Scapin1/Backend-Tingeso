package Tingeso.Web_mono.Repository;

import Tingeso.Web_mono.Entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    @Modifying
    @Query("UPDATE ClientEntity c SET c.clientState = 'RESTRICTED' " +
            "WHERE EXISTS (" +
            "SELECT 1 FROM LoanEntity l " +
            "WHERE l.client.id = c.id AND l.returnDate < CURRENT_TIMESTAMP AND (l.status = 'NORMAL' OR l.status = 'OVERDUE') " +
            ") AND c.clientState != 'RESTRICTED'")
    void restrictClientsWithOverdueLoans();
}
