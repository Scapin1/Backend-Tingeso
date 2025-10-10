package Tingeso.Web_mono.Controller.models;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoansByMonthAndToolNameDTO {
    private String toolName;
    private String month;
    private Long count;

}
