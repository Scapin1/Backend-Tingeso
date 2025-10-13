package Tingeso.Web_mono.Service;

import Tingeso.Web_mono.Controller.models.CreateToolDTO;
import Tingeso.Web_mono.Controller.models.ToolAvailableDTO;
import Tingeso.Web_mono.Entity.*;
import Tingeso.Web_mono.Repository.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.parser.Entity;
import java.time.LocalDateTime;
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
    private final KardexRepository kardexRepository;

    public void save(CreateToolDTO tool, String username) {
        int repoFee = tool.getRepoFee();
        String name = tool.getName();
        String category = tool.getCategory();
        int quantity = tool.getQuantity();
        FeeEntity fee = new FeeEntity();
        fee.setRepoFee(repoFee);
        if(toolRepository.getStock(name) > 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Tool already exists");
        }

        feeRepository.save(fee);

        for (int i = 0; i < quantity; i++) {
            ToolEntity toolCopy = ToolEntity.builder()
                    .name(name)
                    .category(category)
                    .state(ToolStateType.AVAILABLE)
                    .fee(fee)
                    .build();
            toolRepository.save(toolCopy);
        };

        KardexEntity kardex = KardexEntity.builder()
                .type(KardexMovementType.INCOME)
                .quantity(quantity)
                .user(username)
                .toolName(tool.getName())
                .movementDate(LocalDateTime.now())
                .build();
        kardexRepository.save(kardex);


    }

    public List<ToolEntity> findAll() {
        return toolRepository.findAll();
    }

    public List<ToolAvailableDTO> findAllList() {
        return toolRepository.findAvailableToolsGrouped();
    }

    public ToolEntity sentMaintenance(Long toolId, String username) {

        ToolEntity tool = toolRepository.findById(toolId).orElse(null);
        if (tool == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Tool not found");
        }
        tool.setState(ToolStateType.IN_REPAIR);

        KardexEntity kardex = KardexEntity.builder()
                .type(KardexMovementType.REPAIR)
                .quantity(1)
                .user(username)
                .toolName(tool.getName())
                .movementDate(LocalDateTime.now())
                .toolId(toolId)
                .build();
        kardexRepository.save(kardex);

        return toolRepository.save(tool);
    }

    public ToolEntity writeOff(Long toolId, String username) {
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
            clientRepository.save(client);
        }

        tool.setState(ToolStateType.WRITTEN_OFF);

        KardexEntity kardex = KardexEntity.builder()
                .type(KardexMovementType.WRITE_OFF)
                .quantity(1)
                .user(username)
                .toolName(tool.getName())
                .movementDate(LocalDateTime.now())
                .toolId(toolId)
                .build();
        kardexRepository.save(kardex);

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