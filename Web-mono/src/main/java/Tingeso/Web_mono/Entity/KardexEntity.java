package Tingeso.Web_mono.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="tb_Kardex")
public class KardexEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long toolId;
    private String user;
    private int quantity;
    private LocalDateTime movementDate;
    @Enumerated(EnumType.STRING)
    private KardexMovementType type;
}
