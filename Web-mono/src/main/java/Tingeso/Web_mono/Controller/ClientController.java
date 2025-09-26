package Tingeso.Web_mono.Controller;

import Tingeso.Web_mono.Entity.ClientEntity;
import Tingeso.Web_mono.Service.ClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    @GetMapping("/getClients")
    public List<ClientEntity> getClients() {
        return clientService.getAll();
    }

    @PostMapping("/addClient")
    public ClientEntity addClient(@RequestBody ClientEntity client) {
        return clientService.save(client);
    }

    @PostMapping("/changeState/{id}")
    public ClientEntity changeState(@PathVariable Long id) {
        return clientService.changeState(id);
    }

    @PutMapping("/updateClient")
    public ClientEntity updateClient(@RequestBody ClientEntity client) {
        return clientService.updateClient(client);
    }
}
