package duantn.backend.controller.superAdmin;

import duantn.backend.dao.StaffRepository;
import duantn.backend.entity.Staffs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/super-admin")
public class StaffsManager {

    final
    StaffRepository staffRepository;

    @Autowired
    public StaffsManager(StaffRepository staffRepository) {
        this.staffRepository = staffRepository;
    }

    @GetMapping("/staffs")
    public ResponseEntity<List<Staffs>> listStaffs() {
        List<Staffs> staffs = staffRepository.findAll();
        return ResponseEntity.ok(staffs);
    }

    //    thêm nhân viên	Post/super-admin/staffs
    @PostMapping("/staffs")
    public ResponseEntity<?> insertStaff(@RequestBody Staffs staff) {
        Staffs newStaff = staffRepository.save(staff);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newStaff.getStaffId()).toUri();

        return ResponseEntity.created(location).build();
    }

    //    cập nhật thông tin nhân viên	Put/super-admin/staffs
    @PutMapping("/staffs")
    public ResponseEntity<?> updateStaff(@RequestBody Staffs staff) {
        Staffs newStaff = staffRepository.save(staff);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newStaff.getStaffId()).toUri();

        return ResponseEntity.created(location).build();
    }

    //    block nhân viên	GET/super-admin/staffs/block/{id}
    @GetMapping("/staffs/block/{id}")
    public boolean blockStaff(@PathVariable int id) {
        Optional<Staffs> staff = staffRepository.findById(id);
        if (staff.isPresent()) {
            staff.get().setStatus(false);
            staffRepository.save(staff.get());
            return true;
        } else return false;
    }

    //    active nhân viên	DELETE/super-admin/staffs/block/{id}
    @GetMapping("/staffs/active/{id}")
    public boolean activeStaff(@PathVariable int id) {
        Optional<Staffs> staff = staffRepository.findById(id);
        if (staff.isPresent()) {
            staff.get().setStatus(true);
            staffRepository.save(staff.get());
            return true;
        }
        return false;
    }

    //    tìm kiếm nhân viên bằng email	GET/super-admin/staffs?email={email}
    @GetMapping(value = "/staffs", params = "email")
    public ResponseEntity<List<Staffs>> findStaffByEmail(@RequestParam("email") String email) {
        List<Staffs> staffs = staffRepository.findByEmail(email);
        return ResponseEntity.ok(staffs);
    }

    //    tìm kiếm nhân viên bằng sdt	GET/super-admin/staffs?phone={phone}
    @GetMapping(value = "/staffs", params = "phone")
    public ResponseEntity<List<Staffs>> findStaffByPhone(@RequestParam("phone") String phone) {
        List<Staffs> staffs = staffRepository.findByPhone(phone);
        return ResponseEntity.ok(staffs);
    }

//    xem thông tin nhân viên	GET/super-admin/staffs/{id}
    @GetMapping("/staffs/{id}")
    public ResponseEntity<Staffs> findOneStaff(@PathVariable int id){
        Optional<Staffs> staff=staffRepository.findById(id);
        return ResponseEntity.ok(staff.get());
    }

}
