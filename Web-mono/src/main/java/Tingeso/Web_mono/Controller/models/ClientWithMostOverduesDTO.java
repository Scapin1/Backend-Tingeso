package Tingeso.Web_mono.Controller.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientWithMostOverduesDTO {
    private String rut;
    private Long overdueCount;
}
