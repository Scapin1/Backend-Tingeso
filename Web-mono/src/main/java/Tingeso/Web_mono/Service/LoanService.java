package Tingeso.Web_mono.Service;

import Tingeso.Web_mono.Controller.models.ClientWithMostLoansDTO;
import Tingeso.Web_mono.Controller.models.ClientWithMostOverduesDTO;
import Tingeso.Web_mono.Controller.models.LoanDTO;
import Tingeso.Web_mono.Controller.models.ToolWithMostOverduesDTO;
import Tingeso.Web_mono.Entity.*;
import Tingeso.Web_mono.Repository.ClientRepository;
import Tingeso.Web_mono.Repository.KardexRepository;
import Tingeso.Web_mono.Repository.LoanRepository;
import Tingeso.Web_mono.Repository.ToolRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanService {

    private final LoanRepository loanRepository;
    private final ClientRepository clientRepository;
    private final ToolRepository toolRepository;
    private final ToolService toolService;
    private final ClientService clientService;
    private final KardexRepository kardexRepository;

    @Transactional
    public List<LoanDTO> getAllLoans() {

        loanRepository.markOverdueLoans();
        return loanRepository.findAllLoan();
    }

    public LoanEntity addLoan(LoanEntity loan, String username) {
        Long clientId = loan.getClient().getId();
        LocalDate loanDate = LocalDate.now();
        LocalDate returnDate = loan.getReturnDate();
        String toolName = loan.getToolLoaned().getName();
        ToolEntity newTool = toolRepository.findTopByNameAndState(toolName,ToolStateType.AVAILABLE);
        ClientEntity newClient = clientRepository.getReferenceById(clientId);

        if(toolRepository.getStock(newTool.getName()) < 1) {
            throw new ResponseStatusException(HttpStatus.LOCKED,"Not enough stock for the tool");
        }else if(newClient.getClientState().toString().equals("RESTRICTED") || loanRepository.existsOverdueLoanByClientId(newClient.getId()) ) {
            throw new ResponseStatusException(HttpStatus.LOCKED,"Client is restricted");
        }else  if(loanDate.isAfter(returnDate)) {
            throw new ResponseStatusException(HttpStatus.LOCKED,"Return date must be after loan date");
        } else if (loanRepository.countByClientIdAndStatus(newClient.getId(), LoanState.NORMAL) >= 5) {
            throw new ResponseStatusException(HttpStatus.LOCKED,"Client has reached the maximum number of loans");
        } else if (loanRepository.existsByToolAndClient(newTool.getName(), newClient)) {
            throw new ResponseStatusException(HttpStatus.LOCKED,"Client already has this tool on loan");
        } else if (ChronoUnit.DAYS.between(loanDate, returnDate) < 1
        ) {
            throw new ResponseStatusException(HttpStatus.LOCKED,"Loan period must be at least 1 day");
        } else if (newClient.getDebt() > 0) {
            throw new ResponseStatusException(HttpStatus.LOCKED,"Client has outstanding debt");
        }
        loan.setToolLoaned(newTool);
        newTool.setState(ToolStateType.LOANED);
        toolRepository.save(newTool);

        KardexEntity kardex = KardexEntity.builder()
                .type(KardexMovementType.LOAN)
                .quantity(1)
                .user(username)
                .toolName(newTool.getName())
                .movementDate(LocalDateTime.now().withSecond(0).withNano(0))
                .toolId(newTool.getId())
                .build();
        kardexRepository.save(kardex);

        return loanRepository.save(loan);

    }

    public LoanEntity returnLoan(Long loanId, boolean damage, String username) {
        int lateFee = 0;
        int damageFee = 0;
        LoanEntity loan = loanRepository.findById(loanId).orElse(null);

        if (loan == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Loan not found");
        } else if(loan.getStatus().equals(LoanState.IN_REPAIR) || loan.getStatus().equals(LoanState.FINISHED) || loan.getStatus().equals(LoanState.LATE_RETURN)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Loan already returned");
        }

        ToolEntity tool = loan.getToolLoaned();

        if (loan.getReturnDate().isBefore(LocalDate.now())) {
            long daysLate = ChronoUnit.DAYS.between(loan.getReturnDate(), LocalDate.now());
            lateFee = (int) (daysLate * tool.getFee().getLateFee());
        }

        if(damage) {

            damageFee = tool.getFee().getMaintenanceFee();
            toolService.sentMaintenance(tool.getId(), username);
            loan.setStatus(LoanState.IN_REPAIR);

        }else if (loan.getReturnDate().isBefore(LocalDate.now())) {

            tool.setState(ToolStateType.AVAILABLE);
            loan.setStatus(LoanState.LATE_RETURN);
            toolRepository.save(tool);

        }else{

            tool.setState(ToolStateType.AVAILABLE);
            loan.setStatus(LoanState.FINISHED);
            toolRepository.save(tool);

        }

        ClientEntity client = loan.getClient();

        //change state if the client has debt
        if(client.getDebt() + lateFee + damageFee > 0) {

            client.setDebt(lateFee + damageFee + client.getDebt());

        }

        if(client.getClientState().toString().equals("RESTRICTED")) {

            client.setClientState(ClientState.ACTIVE);

        }

        clientRepository.save(client);

        KardexEntity kardex = KardexEntity.builder()
                .type(KardexMovementType.RETURN)
                .quantity(1)
                .user(username)
                .toolName(tool.getName())
                .movementDate(LocalDateTime.now().withSecond(0).withNano(0))
                .toolId(tool.getId())
                .build();
        kardexRepository.save(kardex);

        return loanRepository.save(loan);
    }

    public List<ClientWithMostLoansDTO> getClientWithMostLoans() {

        return loanRepository.findClientsWithMostLoans();

    }

    public List<ClientWithMostOverduesDTO> getClientsWithMostOverdues() {
        return loanRepository.findClientsWithMostOverdues();
    }

    public ToolWithMostOverduesDTO getToolWithMostOverdues() {
        return loanRepository.findToolWithMostOverdues();
    }
}
