package Tingeso.Web_mono.Repository;

import Tingeso.Web_mono.Entity.KardexEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KardexRepository extends JpaRepository<KardexEntity, Long> {
}
