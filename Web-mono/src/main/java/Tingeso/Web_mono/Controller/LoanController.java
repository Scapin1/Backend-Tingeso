package Tingeso.Web_mono.Controller;

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
    public List<LoanEntity> getAllLoans() {
        return loanService.getAllLoans();
    }

    @PostMapping("/addLoan")
    public LoanEntity addLoan(@RequestBody LoanEntity loanEntity) {
        return loanService.addLoan(loanEntity);
    }

    @PutMapping("/returnLoan")
    public LoanEntity returnLoan(HttpServletRequest request) {
        return loanService.returnLoan(request);
    }

}
