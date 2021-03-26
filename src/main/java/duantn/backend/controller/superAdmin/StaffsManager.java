package duantn.backend.controller.superAdmin;

import duantn.backend.authentication.CustomException;
import duantn.backend.dao.StaffRepository;
import duantn.backend.model.dto.input.StaffInsertDTO;
import duantn.backend.model.dto.input.StaffUpdateDTO;
import duantn.backend.model.dto.output.Message;
import duantn.backend.model.dto.output.StaffOutputDTO;
import duantn.backend.model.entity.Customer;
import duantn.backend.model.entity.Staff;
import duantn.backend.service.StaffService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/super-admin")
public class StaffsManager {
    final
    StaffService staffService;

    public StaffsManager(StaffService staffService) {
        this.staffService = staffService;
    }

    //page là trang mấy
    //limit là số bản ghi trong 1 trang
    //nếu ko nhập 2 tham số này thì ko phân trang
    //search theo name or mail or phone ko nhập thì trả về all
    //sort=asc or desc không nhập thì ko xếp
    @GetMapping("/staffs")
    public List<StaffOutputDTO> listStaffs
            (@RequestParam(required = false) String search,
             @RequestParam(required = false) Boolean deleted,
             @RequestParam(required = false) String sort,
             @RequestParam Integer page,
             @RequestParam Integer limit) {
        return staffService.listStaff(search, deleted, sort, page, limit);
    }

    //    thêm nhân viên	Post/super-admin/staffs
    @PostMapping("/staffs")
    public Message insertStaff(@Valid @RequestBody StaffInsertDTO staffInsertDTO,
                                         HttpServletRequest request)
            throws Exception {
        return staffService.insertStaff(staffInsertDTO, request);
    }

    //    cập nhật thông tin nhân viên	Put/super-admin/staffs
    @PutMapping("/staffs")
    public ResponseEntity<?> updateStaff(@Valid @RequestBody StaffUpdateDTO staffUpdateDTO)
    throws CustomException {
        return staffService.updateStaff(staffUpdateDTO);
    }

    //    block nhân viên	GET/super-admin/staffs/block/{id}
    @GetMapping("/staffs/block/{id}")
    public Message blockStaff(@PathVariable Integer id) throws CustomException{
        return staffService.blockStaff(id);
    }

    //    active nhân viên	DELETE/super-admin/staffs/block/{id}
    @GetMapping("/staffs/active/{id}")
    public Message activeStaff(@PathVariable Integer id) throws CustomException{
        return staffService.activeStaff(id);
    }

    //    xem thông tin nhân viên	GET/super-admin/staffs/{id}
    @GetMapping("/staffs/{id}")
    public ResponseEntity<?> findOneStaff(@PathVariable Integer id) {
        return staffService.findOneStaff(id);
    }

    // xóa toàn bộ những nhân viên đã bị xóa mềm
    @DeleteMapping("/delete")
    public Message deleteAllStaffs(){
        return staffService.deleteAllStaffs();
    }

    @PostMapping("/delete")
    public Message deleteStaffs(@RequestBody List<Integer> list) throws CustomException{
        return staffService.deleteStaffs(list);
    }
}
