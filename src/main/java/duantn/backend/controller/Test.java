package duantn.backend.controller;

import duantn.backend.dao.StaffRepository;
import duantn.backend.entity.Staffs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

@Component
public class Test implements CommandLineRunner {
    final
    StaffRepository staffRepository;

    @Autowired
    public Test(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Optional<Staffs> staff=staffRepository.findById(1);
        //staff.setStaffId(1);
        staff.get().setPhone("12345678");
        staffRepository.save(staff.get());
    }
}
