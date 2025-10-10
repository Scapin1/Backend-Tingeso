package Tingeso.Web_mono.Controller.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientWithMostLoansDTO {
    private String clientRut;
    private Long loanCount;
}

