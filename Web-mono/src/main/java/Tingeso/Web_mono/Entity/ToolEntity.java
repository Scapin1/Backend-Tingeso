package Tingeso.Web_mono.Entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="tb_Tools")
public class ToolEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private String name;

    @Column(nullable=false)
    private String category;

    @Enumerated(EnumType.STRING)
    private ToolStateType state;

    @OneToMany(mappedBy = "toolLoaned")
    private List<LoanEntity> loans;

    @ManyToOne
    @JoinColumn(name="fee_id", nullable=false)
    @JsonBackReference(
            value="fee-tool"
    )
    private FeeEntity fee;
}
