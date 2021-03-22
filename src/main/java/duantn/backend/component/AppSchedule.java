package duantn.backend.component;

import duantn.backend.dao.CustomerRepository;
import duantn.backend.dao.StaffRepository;
import duantn.backend.helper.DateHelper;
import duantn.backend.helper.Helper;
import duantn.backend.model.entity.Customer;
import duantn.backend.model.entity.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class AppSchedule {
    final
    CustomerRepository customerRepository;

    final
    StaffRepository staffRepository;

    public AppSchedule(CustomerRepository customerRepository, StaffRepository staffRepository) {
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void AutoRemoveAccount() {
        Date date=new Date(new Date().getTime()-2*24*3600*1000);
        Date expDate= DateHelper.changeTime(date, "23:59:59");
        List<Customer> customerList=customerRepository.
                findByEnabledFalseAndTimeCreatedLessThanEqual(expDate);
        List<Staff> staffList=staffRepository.findByEnabledFalseAndTimeCreatedLessThanEqual(expDate);
        if(staffList.size()>0){
            for (Staff staff:staffList){
                staffRepository.delete(staff);
            }
        }
        if(customerList.size()>0) {
            for (Customer customer : customerList) {
                customerRepository.delete(customer);
            }
        }
    }
}
