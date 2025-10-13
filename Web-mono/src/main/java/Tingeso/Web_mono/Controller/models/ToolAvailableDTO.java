package Tingeso.Web_mono.Controller.models;


import Tingeso.Web_mono.Entity.FeeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ToolAvailableDTO {
    private String name;
    private String category;
    private Long stock;
    private int repoFee;
    private int maintenanceFee;
    private int rentalFee;
    private int lateFee;
}
