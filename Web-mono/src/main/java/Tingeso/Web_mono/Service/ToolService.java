package Tingeso.Web_mono.Service;

import Tingeso.Web_mono.Controller.models.ToolAvailableDTO;
import Tingeso.Web_mono.Entity.*;
import Tingeso.Web_mono.Repository.ClientRepository;
import Tingeso.Web_mono.Repository.FeeRepository;
import Tingeso.Web_mono.Repository.LoanRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import Tingeso.Web_mono.Repository.ToolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.parser.Entity;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ToolService {

    private final ToolRepository toolRepository;
    private final FeeRepository feeRepository;
    private final LoanRepository  loanRepository;
    private final ClientRepository clientRepository;
    private final ClientService clientService;

    public void save(HttpServletRequest request) {
        int quantity = Integer.parseInt(request.getParameter("quantity"));
        String name = request.getParameter("name");
        String category = request.getParameter("category");
        int repoFee = Integer.parseInt(request.getParameter("repoFee"));
        String state = request.getParameter("state");
        FeeEntity fee = new FeeEntity();
        fee.setRepoFee(repoFee);
        if( toolRepository.findTopByName(name) == null || toolRepository.findTopByName(name).getFee() == null){
            feeRepository.save(fee);
        }else{
            fee = toolRepository.findTopByName(name).getFee();
        }

        for (int i = 0; i < quantity; i++) {
            ToolEntity toolCopy = ToolEntity.builder()
                    .name(name)
                    .category(category)
                    .state(ToolStateType.valueOf(state))
                    .fee(fee)
                    .build();
            toolRepository.save(toolCopy);
        };
    }

    public List<ToolEntity> findAll() {
        return toolRepository.findAll();
    }

    public List<ToolAvailableDTO> findAllList(HttpServletRequest request) {
        List<Object[]> results = toolRepository.findAvailableToolsGrouped();
        List<ToolAvailableDTO> dtos = results.stream()
                .map(obj -> new ToolAvailableDTO(
                        (String) obj[0],
                        (String) obj[1],
                        ((Long) obj[2]).intValue()
                ))
                .collect(Collectors.toList());
        return dtos;
    }

    public ToolEntity sentMaintenance(Long toolId) {

        ToolEntity tool = toolRepository.findById(toolId).orElse(null);
        if (tool == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Tool not found");
        }
        tool.setState(ToolStateType.IN_REPAIR);
        return toolRepository.save(tool);
    }

    public ToolEntity writeOff(Long toolId) {
        ToolEntity tool = toolRepository.findById(toolId).orElse(null);

        if (tool == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Tool not found");
        }

        LoanEntity loan = loanRepository.findByToolLoaned_Id(toolId);
        if (loan != null) {
            ClientEntity client = loan.getClient();
            loan.setStatus(LoanState.FINISHED);
            loanRepository.save(loan);
            client.setDebt(client.getDebt() + tool.getFee().getRepoFee());
            if(client.getClientState() == ClientState.ACTIVE) {
                clientService.changeState(client.getId());
            }
            clientRepository.save(client);
        }

        tool.setState(ToolStateType.WRITTEN_OFF);
        return toolRepository.save(tool);
    }

    public FeeEntity getFees(Long ToolId) {
        ToolEntity tool = toolRepository.findById(ToolId).orElse(null);
        if (tool == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Tool not found");
        }
        return tool.getFee();
    }

    public FeeEntity changeFee(FeeEntity fees) {
        return feeRepository.save(fees);
    }


}