package Tingeso.Web_mono.Controller;

import Tingeso.Web_mono.Controller.models.ClientWithMostLoansDTO;
import Tingeso.Web_mono.Controller.models.ClientWithMostOverduesDTO;
import Tingeso.Web_mono.Controller.models.LoanDTO;
import Tingeso.Web_mono.Controller.models.ToolWithMostOverduesDTO;
import Tingeso.Web_mono.Entity.LoanEntity;
import Tingeso.Web_mono.Service.LoanService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/loans")
public class LoanController {

    private final LoanService loanService;

    @GetMapping("/getAll")
    public List<LoanDTO> getAllLoans() {
        return loanService.getAllLoans();
    }

    @PostMapping("/addLoan/{username}")
    public LoanEntity addLoan(@RequestBody LoanEntity loanEntity, @PathVariable String username) {
        return loanService.addLoan(loanEntity, username);
    }

    @PutMapping("/returnLoan/{loanId}/{damaged}/{username}")
    public LoanEntity returnLoan(@PathVariable Long loanId, @PathVariable Boolean damaged, @PathVariable String username) {
        return loanService.returnLoan(loanId, damaged, username);
    }

    @GetMapping("/clientWithMostLoans")
    public List<ClientWithMostLoansDTO> getClientWithMostLoans() {
        return loanService.getClientWithMostLoans();
    }

    @GetMapping("/clientsWithMostOverdues")
    public List<ClientWithMostOverduesDTO> getClientsWithMostOverdues() {
        return loanService.getClientsWithMostOverdues();
    }

    @GetMapping("/toolWithMostOverdues")
    public ToolWithMostOverduesDTO getToolWithMostOverdues() {
        return loanService.getToolWithMostOverdues();
    }

    @GetMapping("/mostLoansInRange")
    public ClientWithMostLoansDTO getMostLoansInRange(@RequestParam String startDate, @RequestParam String endDate) {
        java.time.LocalDate start = java.time.LocalDate.parse(startDate);
        java.time.LocalDate end = java.time.LocalDate.parse(endDate);
        List<ClientWithMostLoansDTO> results = loanService.getClientsWithMostLoansInRange(start, end);
        return (results != null && !results.isEmpty()) ? results.get(0) : null;
    }

    @GetMapping("/clientsWithMostLoansInRange")
    public List<ClientWithMostLoansDTO> getClientsWithMostLoansInRange(@RequestParam String startDate, @RequestParam String endDate) {
        java.time.LocalDate start = java.time.LocalDate.parse(startDate);
        java.time.LocalDate end = java.time.LocalDate.parse(endDate);
        return loanService.getClientsWithMostLoansInRange(start, end);
    }

    @GetMapping("/clientsWithMostOverduesInRange")
    public List<ClientWithMostOverduesDTO> getClientsWithMostOverduesInRange(@RequestParam String startDate, @RequestParam String endDate) {
        java.time.LocalDate start = java.time.LocalDate.parse(startDate);
        java.time.LocalDate end = java.time.LocalDate.parse(endDate);
        return loanService.getClientsWithMostOverduesInRange(start, end);
    }

    @GetMapping("/toolWithMostOverduesInRange")
    public ToolWithMostOverduesDTO getToolWithMostOverduesInRange(@RequestParam String startDate, @RequestParam String endDate) {
        java.time.LocalDate start = java.time.LocalDate.parse(startDate);
        java.time.LocalDate end = java.time.LocalDate.parse(endDate);
        return loanService.getToolWithMostOverduesInRange(start, end);
    }

}
