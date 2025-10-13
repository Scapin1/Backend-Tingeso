package Tingeso.Web_mono.Controller.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ToolWithMostOverduesDTO {
    private String toolName;
    private Long overdueCount;
}

