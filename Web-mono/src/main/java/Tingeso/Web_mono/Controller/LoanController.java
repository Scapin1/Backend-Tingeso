package Tingeso.Web_mono.Controller;

import Tingeso.Web_mono.Controller.models.LoanDTO;
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

}
