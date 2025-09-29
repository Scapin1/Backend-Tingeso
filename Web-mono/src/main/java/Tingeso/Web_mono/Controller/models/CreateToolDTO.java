package Tingeso.Web_mono.Controller.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateToolDTO {
    public String name;
    public int quantity;
    public int repoFee;
    public String category;
}
