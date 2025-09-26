package Tingeso.Web_mono.Repository;

import Tingeso.Web_mono.Entity.FeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRepository extends JpaRepository<FeeEntity, Integer> {
}
