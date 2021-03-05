package duantn.backend.service;

import duantn.backend.model.dto.input.CustomerInsertDTO;
import duantn.backend.model.dto.input.CustomerUpdateDTO;
import duantn.backend.model.dto.output.CustomerOutputDTO;
import duantn.backend.model.dto.output.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface CustomerService {
    List<CustomerOutputDTO> listCustomer(@RequestParam(required = false) String search,
                                         @RequestParam(required = false) String sort,
                                         @RequestParam(required = false) Integer page,
                                         @RequestParam(required = false) Integer limit);

    ResponseEntity<?> insertCustomer(CustomerInsertDTO customerInsertDTO);
    ResponseEntity<?> updateCustomer(CustomerUpdateDTO customerUpdateDTO);
    Message blockCustomer(Integer id);
    Message activeCustomer(Integer id);
}
