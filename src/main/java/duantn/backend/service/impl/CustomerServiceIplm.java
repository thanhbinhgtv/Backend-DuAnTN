package duantn.backend.service.impl;

import duantn.backend.dao.CustomerRepository;
import duantn.backend.model.dto.input.CustomerInsertDTO;
import duantn.backend.model.dto.input.CustomerUpdateDTO;
import duantn.backend.model.dto.output.CustomerOutputDTO;
import duantn.backend.model.dto.output.Message;
import duantn.backend.model.entity.Customer;
import duantn.backend.service.CustomerService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CustomerServiceIplm implements CustomerService {
    final CustomerRepository customerRepository;

    public CustomerServiceIplm(CustomerRepository customerRepository){
        this.customerRepository = customerRepository;
    }

    @Override
    public List<CustomerOutputDTO> listCustomer(String search, String sort, Integer page, Integer limit){
        //find all
        List<Customer> customerList = new ArrayList<>();
        if (sort != null && !sort.equals("")) {
            if (sort.equalsIgnoreCase("desc")) {
                customerList = customerRepository.findByDeletedFalse(Sort.by("name").descending());
            } else {
                customerList = customerRepository.findByDeletedFalse(Sort.by("name").ascending());
            }
        } else customerList = customerRepository.findByDeletedFalse();

        //search
        if (search != null && !search.equals("")) {
            Set<Customer> searchCustomer = new HashSet<>();
            List<Customer> customerName = customerRepository.findByNameLikeAndDeletedFalse("%" + search + "%");
            List<Customer> customersNameFilter = filter(customerName, customerList);
            searchCustomer.addAll(customersNameFilter);

            List<Customer> customerEmail = customerRepository.findByNameLikeAndDeletedFalse("%" + search + "%");
            List<Customer> customersEmailFilter = filter(customerEmail, customerList);
            searchCustomer.addAll(customersEmailFilter);

            List<Customer> customersPhone = customerRepository.findByNameLikeAndDeletedFalse("%" + search + "%");
            List<Customer> customersPhoneFilter = filter(customersPhone, customerList);
            searchCustomer.addAll(customersPhoneFilter);

            List<Customer> searchCustomerList = new ArrayList<>();
            customerList = filter(customerList, searchCustomerList);
        }
//        if (page!=null && limit!=null){
//
//        }

        //convert
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
       List<CustomerOutputDTO> customerOutputDTOList = new ArrayList<>();
       for(Customer customer : customerList){
           customerOutputDTOList.add(modelMapper.map(customer,CustomerOutputDTO.class));
       }
        return customerOutputDTOList;
    }

    public List<Customer> filter(List<Customer> minList, List<Customer> maxList){
        List<Customer> newCustomers = new ArrayList<>();
        for (Customer customer : minList) {
            if (maxList.contains(customer)) newCustomers.add(customer);
        }
        return newCustomers;
    }

    @Override
    public ResponseEntity<?> insertCustomer(CustomerInsertDTO customerInsertDTO){
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            Customer customer = modelMapper.map(customerInsertDTO, Customer.class);
            Customer newCustomer = customerRepository.save(customer);
            return ResponseEntity.ok(modelMapper.map(newCustomer, CustomerOutputDTO.class));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok(new Message("Insert failed"));
        }
    }

    @Override
    public ResponseEntity<?> updateCustomer(CustomerUpdateDTO customerUpdateDTO){
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            Customer customer = modelMapper.map(customerUpdateDTO, Customer.class);
            Customer oldCustomer = customerRepository.findByCustomerIdAndDeletedFalse(customerUpdateDTO.getCustomerId());
            Customer newCustomer = customerRepository.save(customer);
            return ResponseEntity.ok(modelMapper.map(newCustomer,CustomerOutputDTO.class));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.ok(new Message("Update failed"));
        }
    }

    @Override
    public Message blockCustomer(Integer id){
        Customer customer = customerRepository.findByCustomerIdAndDeletedFalse(id);
        if (customer == null)return new Message("Block customer id: "+id+" failed");
        else{
            customer.setDeleted(true);
            customerRepository.save(customer);
            return new Message("Block customer id: "+id+" successfully");
        }
    }

    @Override
    public Message activeCustomer(Integer id){
        Optional<Customer> optionalCustomer = customerRepository.findById(id);
        if(!optionalCustomer.isPresent()) return new Message("CustomerId: " + id + " is not found");
        else {
            optionalCustomer.get().setDeleted(false);
            customerRepository.save(optionalCustomer.get());
            return new Message("CustomerId: "+id+" successfully");
        }
    }
}
