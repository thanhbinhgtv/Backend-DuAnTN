package duantn.backend.controller;

import duantn.backend.dao.StaffRepository;
import duantn.backend.entity.Staffs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

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
        Staffs staff=new Staffs();
        staff.setEmail("dskfjksdfj");
        staff.setPass("dskfjksdfj");
        staff.setName("dskfjksdfj");
        staff.setCardId("dskfjksdfj");
        staff.setDob(new Date());
        staff.setGender(true);
        staff.setPosition("dskfjksdfj");
        staff.setAddress("dskfjksdfj");
        staff.setPhone("dskfjksdfj");
        staff.setImage("dskfjksdfj");
        staffRepository.save(staff);

    }
}
