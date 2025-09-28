package Tingeso.Web_mono.Controller.models;

import Tingeso.Web_mono.Entity.ClientEntity;
import Tingeso.Web_mono.Entity.LoanState;
import Tingeso.Web_mono.Entity.ToolEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanDTO {
    private Long id;

    private LocalDate loanDate;

    private LocalDate returnDate;

    private LoanState status;

    private String toolLoaned;

    private String client;

}
