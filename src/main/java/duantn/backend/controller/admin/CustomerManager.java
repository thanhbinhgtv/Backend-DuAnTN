package duantn.backend.controller.admin;

import duantn.backend.authentication.CustomException;
import duantn.backend.model.dto.input.CustomerUpdateDTO;
import duantn.backend.model.dto.output.Message;
import duantn.backend.model.dto.output.CustomerOutputDTO;
import duantn.backend.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class CustomerManager {
    final
    CustomerService customerService;

    public CustomerManager(CustomerService customerService) {
        this.customerService = customerService;
    }


    //sắp xếp theo name, accountBalance (chỉ chọn 1 trong 2)
    @GetMapping("/customers")
    public List<CustomerOutputDTO> listCustomers
    (@RequestParam(required = false) String search,
     @RequestParam(required = false) Boolean deleted,
     @RequestParam(value = "name-sort", required = false) String nameSort,
     @RequestParam(value = "balance-sort", required = false) String balanceSort,
     @RequestParam Integer page,
     @RequestParam Integer limit) {
        return customerService.listCustomer(search, deleted, nameSort, balanceSort, page, limit);
    }

    //cập nhật thông tin nhân viên	POST/admin/customers
    @PostMapping("/customers/{id}")
    public ResponseEntity<?> updateCustomer(@Valid @RequestBody CustomerUpdateDTO customerUpdateDTO,
                                            @PathVariable Integer id)
            throws CustomException {
        return customerService.updateCustomer(customerUpdateDTO, id);
    }

    //    block nhân viên	GET/admin/customers/block/{id}
    @GetMapping("/customers/block/{id}")
    public Message blockCustomer(@PathVariable Integer id) throws CustomException{
        return customerService.blockCustomer(id);
    }

    //    active nhân viên	DELETE/admin/customers/block/{id}
    @GetMapping("/customers/active/{id}")
    public Message activeCustomer(@PathVariable Integer id) throws CustomException{
        return customerService.activeCustomer(id);
    }

    //    xem thông tin nhân viên	GET/admin/customers/{id}
    @GetMapping("/customers/{id}")
    public ResponseEntity<?> findOneCustomer(@PathVariable Integer id) {
        return customerService.findOneCustomer(id);
    }

    // xóa toàn bộ những nhân viên đã bị xóa mềm
    @DeleteMapping("/customer")
    public Message deleteAllCustomers(){
        return customerService.deleteAllCustomers();
    }

    @DeleteMapping("/customer/{id}")
    public Message deleteCustomers(@PathVariable Integer id) throws CustomException{
        return customerService.deleteCustomers(id);
    }
}
