package Tingeso.Web_mono.Service;


import Tingeso.Web_mono.Entity.*;
import Tingeso.Web_mono.Repository.ClientRepository;
import Tingeso.Web_mono.Repository.LoanRepository;
import Tingeso.Web_mono.Repository.ToolRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
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

    public List<LoanEntity> getAllLoans() {
        return loanRepository.findAll();
    }

    public LoanEntity addLoan(LoanEntity loan) {
        Long clientId = loan.getClient().getId();
        LocalDate loanDate = LocalDate.now();
        LocalDate returnDate = loan.getReturnDate();
        String toolName = loan.getToolLoaned().getName();
        ToolEntity newTool = toolRepository.findTopByNameAndState(toolName,ToolStateType.AVAILABLE);
        ClientEntity newClient = clientRepository.getReferenceById(clientId);

        if(toolRepository.getStock(newTool.getName()) < 1) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Not enough stock for the tool");
        }else if(newClient.getClientState().toString().equals("RESTRICTED") || loanRepository.existsOverdueLoanByClientId(newClient.getId()) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Client is restricted");
        }else  if(loanDate.isAfter(returnDate)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Return date must be after loan date");
        } else if (loanRepository.countByClientIdAndStatus(newClient.getId(), LoanState.NORMAL) >= 5) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Client has reached the maximum number of loans");
        } else if (loanRepository.existsByToolAndClient(newTool.getName(), newClient)) {
            throw new ResponseStatusException(HttpStatus.LOCKED,"Client already has this tool on loan");
        } else if (ChronoUnit.DAYS.between(loanDate, returnDate) < 1
        ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Loan period must be at least 1 day");
        } else if (newClient.getDebt() > 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Client has outstanding debt");
        }
        loan.setToolLoaned(newTool);
        newTool.setState(ToolStateType.LOANED);
        toolRepository.save(newTool);

        return loanRepository.save(loan);

    }

    public LoanEntity returnLoan(HttpServletRequest request) {
        Long loanId = Long.parseLong(request.getParameter("loanId"));
        boolean damage = Boolean.parseBoolean(request.getParameter("damage"));
        int lateFee = 0;
        int damageFee = 0;

        LoanEntity loan = loanRepository.findById(loanId).orElse(null);
        if (loan == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Loan not found");
        } else if(loan.getStatus().equals(LoanState.IN_REPAIR) || loan.getStatus().equals(LoanState.FINISHED)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"Loan already returned");
        }
        if (loan.getReturnDate().isBefore(LocalDate.now())) {
            long daysLate = Duration.between(loan.getReturnDate(), LocalDateTime.now()).toDays();
            lateFee = (int) (daysLate * loan.getToolLoaned().getFee().getLateFee());
        }
        ToolEntity tool = loan.getToolLoaned();
        if(damage) {
            damageFee = tool.getFee().getMaintenanceFee();
            request.setAttribute("toolId",tool.getId());
            toolService.sentMaintenance(request);
            loan.setStatus(LoanState.IN_REPAIR);
        }else{
            tool.setState(ToolStateType.AVAILABLE);
            loan.setStatus(LoanState.FINISHED);
        }
        toolRepository.save(tool);
        ClientEntity client = loan.getClient();

        //change state if the client has debt
        if(client.getDebt() + lateFee + damageFee > 0) {
            clientService.changeState(client.getId());
        }
        client.setDebt(lateFee + damageFee + client.getDebt());
        clientRepository.save(client);
        return loanRepository.save(loan);
    }
}
