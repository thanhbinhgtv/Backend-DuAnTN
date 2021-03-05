package duantn.backend.controller.admin;

import duantn.backend.model.dto.input.CustomerInsertDTO;
import duantn.backend.model.dto.input.CustomerUpdateDTO;
import duantn.backend.model.dto.output.CustomerOutputDTO;
import duantn.backend.model.dto.output.Message;
import duantn.backend.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/")
public class CustomerManage {
    final CustomerService customerService;

    public CustomerManage(CustomerService customerService){
        this.customerService = customerService;
    }
    @GetMapping("customer")
    public List<CustomerOutputDTO> listCustomer
            (@RequestParam(required = false) String search,
             @RequestParam(required = false) String sort,
             @RequestParam(required = false) Integer page,
             @RequestParam(required = false) Integer limit){
        return customerService.listCustomer(search,sort,page,limit);
    }

    @PostMapping("customer")
    public ResponseEntity<?> insertCustomer(@RequestBody CustomerInsertDTO customerInsertDTO){
        return customerService.insertCustomer(customerInsertDTO);
    }

    @PutMapping("customer")
    public ResponseEntity<?> updateCustomer(@RequestBody CustomerUpdateDTO customerUpdateDTO){
        return customerService.updateCustomer(customerUpdateDTO);
    }
    @DeleteMapping("customer/block/{id}")
    public Message blockCustomer(@PathVariable Integer id){
        return customerService.blockCustomer(id);
    }
    @GetMapping("customer/active/{id}")
    public Message activeCustomer(@PathVariable Integer id){
        return customerService.activeCustomer(id);
    }
}
