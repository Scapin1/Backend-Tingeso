package Tingeso.Web_mono.Service;

import Tingeso.Web_mono.Entity.ClientEntity;
import Tingeso.Web_mono.Entity.ClientState;
import Tingeso.Web_mono.Repository.ClientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClientService {

    private final ClientRepository clientRepository;

    @Transactional
    public List<ClientEntity> getAll() {
        clientRepository.restrictClientsWithOverdueLoans();
        return clientRepository.findAll();

    }

    public ClientEntity save(ClientEntity client) {
        return clientRepository.save(client);
    }

    public ClientEntity changeState(Long id) {
        ClientEntity client = clientRepository.findById(id).orElse(null);

        if(client == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Client not found");
        } else if(client.getClientState() == ClientState.ACTIVE) {
            client.setClientState(ClientState.RESTRICTED);

        } else {

            client.setClientState(ClientState.ACTIVE);

        }

        return clientRepository.save(client);
    }

    public ClientEntity updateClient(ClientEntity client) {
        return clientRepository.save(client);
    }


}
