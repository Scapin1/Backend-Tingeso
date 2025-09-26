package Tingeso.Web_mono.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.tools.Tool;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="tb_Fees")
public class FeeEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    private int repoFee = 0;
    private int rentalFee = 0;
    private int lateFee = 0;
    private int maintenanceFee = 0;

    @OneToMany(mappedBy = "fee")
    private List<ToolEntity> tools;
}
