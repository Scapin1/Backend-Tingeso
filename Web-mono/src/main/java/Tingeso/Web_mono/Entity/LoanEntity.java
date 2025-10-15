package Tingeso.Web_mono.Entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="tb_Loan")
public class LoanEntity {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false)
    private LocalDate loanDate;

    @Column(nullable=false)
    private LocalDate returnDate;

    @Column(nullable=false)
    @Enumerated(EnumType.STRING)
    private LoanState status;

    private Boolean LateStatus = false;

    @ManyToOne
    @JoinColumn(name="tool_loaned")
    @JsonBackReference(
            value="tool-loan"
    )
    private ToolEntity toolLoaned;

    @ManyToOne
    @JoinColumn(name="client", nullable=false)
    @JsonBackReference(
            value="client-loan"
    )
    private ClientEntity client;
}
