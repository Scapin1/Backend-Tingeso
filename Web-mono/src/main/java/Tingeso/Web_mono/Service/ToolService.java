package Tingeso.Web_mono.Service;

import Tingeso.Web_mono.Controller.models.ToolAvailableDTO;
import Tingeso.Web_mono.Entity.*;
import Tingeso.Web_mono.Repository.FeeRepository;
import Tingeso.Web_mono.Repository.LoanRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import Tingeso.Web_mono.Repository.ToolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ToolService {

    private final ToolRepository toolRepository;
    private final FeeRepository feeRepository;
    private final LoanRepository  loanRepository;

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

    public ToolEntity findByToolName(String toolName) {
        return toolRepository.findByName(toolName);
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
        if (tool.getState() == ToolStateType.IN_REPAIR) {
            LoanEntity loan = loanRepository.findByToolLoaned_Id(toolId);

        }
        tool.setState(ToolStateType.WRITTEN_OFF);
        return toolRepository.save(tool);
    }

    public FeeEntity changeLateFee(HttpServletRequest request) {
        String toolName = request.getParameter("toolName");
        int lateFee = Integer.parseInt(request.getParameter("lateFee"));
        ToolEntity tool = findByToolName(toolName);
        FeeEntity fee = tool.getFee();
        fee.setLateFee(lateFee);
        return feeRepository.save(fee);
    }

    public FeeEntity changeRentFee(HttpServletRequest request) {
        String toolName = request.getParameter("toolName");
        int rentalFee = Integer.parseInt(request.getParameter("rentFee"));
        ToolEntity tool = findByToolName(toolName);
        FeeEntity fee = tool.getFee();
        fee.setRentalFee(rentalFee);
        return feeRepository.save(fee);
    }

    public FeeEntity changeRepoFee(HttpServletRequest request) {
        String toolName = request.getParameter("toolName");
        int repoFee = Integer.parseInt(request.getParameter("repoFee"));
        ToolEntity tool = findByToolName(toolName);
        FeeEntity fee = tool.getFee();
        fee.setRepoFee(repoFee);
        return feeRepository.save(fee);
    }

    public FeeEntity changeMaintenanceFee(HttpServletRequest request) {
        String toolName = request.getParameter("toolName");
        int maintenanceFee = Integer.parseInt(request.getParameter("maintenanceFee"));
        ToolEntity tool = findByToolName(toolName);
        FeeEntity fee = tool.getFee();
        fee.setMaintenanceFee(maintenanceFee);
        return feeRepository.save(fee);
    }


}