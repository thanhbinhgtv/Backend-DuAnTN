package duantn.backend.service;

import duantn.backend.authentication.CustomException;
import duantn.backend.model.dto.input.StaffInsertDTO;
import duantn.backend.model.dto.input.StaffUpdateDTO;
import duantn.backend.model.dto.output.Message;
import duantn.backend.model.dto.output.StaffOutputDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface StaffService {

    //Tìm kiếm nhân viên = email, phone, name
    //sắp xếp theo name sort=asc, sort=desc
    //phân trang
    //nếu không truyền vào tham số thì trả về all list
    List<StaffOutputDTO> listStaff(@RequestParam(required = false) String search,
                                   Boolean status, @RequestParam(required = false) String sort,
                                   @RequestParam(required = false) Integer page,
                                   @RequestParam(required = false) Integer limit);

    //    thêm nhân viên	Post/super-admin/staffs
    ResponseEntity<?> insertStaff(StaffInsertDTO staffInsertDTO) throws Exception;

    //    cập nhật thông tin nhân viên	Put/super-admin/staffs
    ResponseEntity<?> updateStaff(StaffUpdateDTO staffUpdateDTO) throws CustomException;

    //    block nhân viên	DELETE/super-admin/staffs/{id}
    Message blockStaff(Integer id) throws CustomException;

    //    active nhân viên
    Message activeStaff(Integer id) throws CustomException;

    //    xem thông tin nhân viên	GET/super-admin/staffs/{id}
    ResponseEntity<?> findOneStaff(Integer id);

    //xóa cứng tất cả staff bị xóa mềm
    Message deleteStaffs();
}
