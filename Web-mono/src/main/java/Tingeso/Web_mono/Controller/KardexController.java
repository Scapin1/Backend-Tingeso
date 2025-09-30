package Tingeso.Web_mono.Controller;

import Tingeso.Web_mono.Entity.KardexEntity;
import Tingeso.Web_mono.Service.KardexService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/kardex")
public class KardexController {

    private final KardexService kardexService;

    @GetMapping("/getAll")
    public List<KardexEntity> getAllKardex() {

        return kardexService.getAll();

    }

}
