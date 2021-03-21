package duantn.backend.service.impl;

import duantn.backend.authentication.CustomException;
import duantn.backend.dao.CustomerRepository;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class StaffServiceImpl implements StaffService {
    final
    StaffRepository staffRepository;

    final
    PasswordEncoder passwordEncoder;

    final
    CustomerRepository customerRepository;

    public StaffServiceImpl(StaffRepository staffRepository, PasswordEncoder passwordEncoder, CustomerRepository customerRepository) {
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<StaffOutputDTO> listStaff(String search, Boolean status, String sort,
                                          Integer page, Integer limit) {
        if(search==null || search.trim().equals("")) search="";
        Page<Staff> staffPage;
        if(sort==null || sort.equals("")){
            if(status!=null){
                if(status)
                    staffPage=staffRepository.
                            findByNameLikeAndDeletedTrueOrEmailLikeAndDeletedTrueOrPhoneLikeAndDeletedTrue(
                                    "%"+search+"%", "%"+search+"%", "%"+search+"%",
                                    PageRequest.of(page, limit)
                            );
                else
                    staffPage=staffRepository.
                            findByNameLikeAndDeletedFalseOrEmailLikeAndDeletedFalseOrPhoneLikeAndDeletedFalse(
                                    "%"+search+"%", "%"+search+"%", "%"+search+"%",
                                    PageRequest.of(page, limit)
                            );
            }else
                staffPage=staffRepository.
                        findByNameLikeOrEmailLikeOrPhoneLike(
                                "%"+search+"%", "%"+search+"%", "%"+search+"%",
                                PageRequest.of(page, limit)
                        );
        }else{
            if(sort.equalsIgnoreCase("desc")){
                if(status!=null){
                    if(status)
                        staffPage=staffRepository.
                                findByNameLikeAndDeletedTrueOrEmailLikeAndDeletedTrueOrPhoneLikeAndDeletedTrue(
                                        "%"+search+"%", "%"+search+"%", "%"+search+"%",
                                        PageRequest.of(page, limit, Sort.by("name").descending())
                                );
                    else
                        staffPage=staffRepository.
                                findByNameLikeAndDeletedFalseOrEmailLikeAndDeletedFalseOrPhoneLikeAndDeletedFalse(
                                        "%"+search+"%", "%"+search+"%", "%"+search+"%",
                                        PageRequest.of(page, limit, Sort.by("name").descending())
                                );
                }else
                    staffPage=staffRepository.
                            findByNameLikeOrEmailLikeOrPhoneLike(
                                    "%"+search+"%", "%"+search+"%", "%"+search+"%",
                                    PageRequest.of(page, limit, Sort.by("name").descending())
                            );
            }else{
                if(status!=null){
                    if(status)
                        staffPage=staffRepository.
                                findByNameLikeAndDeletedTrueOrEmailLikeAndDeletedTrueOrPhoneLikeAndDeletedTrue(
                                        "%"+search+"%", "%"+search+"%", "%"+search+"%",
                                        PageRequest.of(page, limit, Sort.by("name").ascending())
                                );
                    else
                        staffPage=staffRepository.
                                findByNameLikeAndDeletedFalseOrEmailLikeAndDeletedFalseOrPhoneLikeAndDeletedFalse(
                                        "%"+search+"%", "%"+search+"%", "%"+search+"%",
                                        PageRequest.of(page, limit, Sort.by("name").ascending())
                                );
                }else
                    staffPage=staffRepository.
                            findByNameLikeOrEmailLikeOrPhoneLike(
                                    "%"+search+"%", "%"+search+"%", "%"+search+"%",
                                    PageRequest.of(page, limit, Sort.by("name").ascending())
                            );
            }

        }

        List<Staff> staffList=staffPage.toList();

        //convert sang StaffOutputDTO
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        List<StaffOutputDTO> staffOutputDTOList = new ArrayList<>();
        for (Staff staff : staffList) {
            StaffOutputDTO staffOutputDTO=modelMapper.map(staff, StaffOutputDTO.class);
            staffOutputDTO.setBirthday(staff.getDob().getTime());
            staffOutputDTOList.add(staffOutputDTO);
        }
        return staffOutputDTOList;
    }

    @Override
    public ResponseEntity<?> insertStaff(StaffInsertDTO staffInsertDTO) throws Exception{
        //validation
        if(customerRepository.findByEmail(staffInsertDTO.getEmail())!=null)
            throw new CustomException("Email đã khách hàng sử dụng");
        if(staffRepository.findByEmail(staffInsertDTO.getEmail())!=null)
            throw new CustomException("Email đã được nhân viên sử dụng");
        String matchNumber="[0-9]+";
        if(!staffInsertDTO.getCardId().matches(matchNumber))
            throw new CustomException("Số CMND phải là số");
        if(!staffInsertDTO.getPhone().matches(matchNumber))
            throw new CustomException("Số điện thoại phải là số");
        if(staffInsertDTO.getBirthday()>=System.currentTimeMillis())
            throw new CustomException("Ngày sinh phải trong quá khứ");

        //insert
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STRICT);
            Staff staff = modelMapper.map(staffInsertDTO, Staff.class);
            staff.setDob(new Date((staffInsertDTO.getBirthday())));
            staff.setPass(passwordEncoder.encode(staffInsertDTO.getPass()));
            Staff newStaff = staffRepository.save(staff);
            StaffOutputDTO staffOutputDTO=modelMapper.map(newStaff, StaffOutputDTO.class);
            staffOutputDTO.setBirthday(newStaff.getDob().getTime());
            return ResponseEntity.ok(staffOutputDTO);
        } catch (Exception e) {
            //e.printStackTrace();
            throw new CustomException("Thêm mới nhân viên thất bại");
        }
    }

    @Override
    public ResponseEntity<?> updateStaff(StaffUpdateDTO staffUpdateDTO) throws CustomException{
        //validate
        String matchNumber="[0-9]+";
        if(!staffUpdateDTO.getCardId().matches(matchNumber))
            throw new CustomException("Số CMND phải là số");
        if(!staffUpdateDTO.getPhone().matches(matchNumber))
            throw new CustomException("Số điện thoại phải là số");
        if(staffUpdateDTO.getBirthday()>=System.currentTimeMillis())
            throw new CustomException("Ngày sinh phải trong quá khứ");

        //update
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STRICT);
            Optional<Staff> optionalStaff = staffRepository.findById(staffUpdateDTO.getStaffId());
            Staff staff= optionalStaff.get();
            staff.setName(staffUpdateDTO.getName());
            staff.setCardId(staffUpdateDTO.getCardId());
            staff.setDob(new Date(staffUpdateDTO.getBirthday()));
            staff.setGender(staffUpdateDTO.isGender());
            staff.setRole(staffUpdateDTO.isRole());
            staff.setAddress(staffUpdateDTO.getAddress());
            staff.setPhone(staffUpdateDTO.getPhone());
            staff.setImage(staffUpdateDTO.getImage());
            Staff newStaff = staffRepository.save(staff);
            StaffOutputDTO staffOutputDTO=modelMapper.map(newStaff, StaffOutputDTO.class);
            staffOutputDTO.setBirthday(newStaff.getDob().getTime());
            return ResponseEntity.ok(staffOutputDTO);
        } catch (Exception e) {
            //e.printStackTrace();
            throw new CustomException("Cập nhật nhân viên thất bại");
        }
    }

    @Override
    public Message blockStaff(Integer id) throws CustomException{
        Staff staff = staffRepository.findByStaffIdAndDeletedFalse(id);
        if (staff == null) throw new CustomException("Lỗi: id "+id+" không tồn tại");
        else {
            staff.setDeleted(true);
            staffRepository.save(staff);
            return new Message("Block nhân viên id " + id + " thành công");
        }
    }

    @Override
    public Message activeStaff(Integer id) throws CustomException{
        Optional<Staff> optionalStaff = staffRepository.findById(id);
        if (!optionalStaff.isPresent()) throw new CustomException("Lỗi: id " + id + " không tồn tại");
        else {
            optionalStaff.get().setDeleted(false);
            staffRepository.save(optionalStaff.get());
            return new Message("Kích hoạt nhân viên id: " + id + " thành công");
        }
    }

    @Override
    public ResponseEntity<?> findOneStaff(Integer id) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STRICT);
            Staff newStaff=staffRepository.findByStaffIdAndDeletedFalse(id);
            StaffOutputDTO staffOutputDTO=modelMapper.map(newStaff, StaffOutputDTO.class);
            staffOutputDTO.setBirthday(newStaff.getDob().getTime());
            return ResponseEntity.ok(staffOutputDTO);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new Message("Lỗi: nhân viên id " + id + " không tồn tại"));
        }
    }

    @Override
    public Message deleteAllStaffs() {
        List<Staff> staffList=staffRepository.findByDeletedTrue();
        for(Staff staff:staffList){
            staffRepository.delete(staff);
        }
        return new Message("Xóa tất cả nhân viên bị xóa mềm thành công");
    }

    @Override
    public Message deleteStaffs(List<Integer> list) throws CustomException{
        String mess="";
        for (Integer id : list) {
            Optional<Staff> optionalStaff=staffRepository.findById(id);
            if(!optionalStaff.isPresent() ||
                    !optionalStaff.get().isDeleted()) mess=mess+", "+id;
            else
                staffRepository.deleteById(id);
        }
        if(!mess.equals(""))
            throw new CustomException("Nhân viên có id: "+mess+" không tồn tại hoặc chưa bị xóa mềm");
        return new Message("Xóa cứng một số nhân viên bị xóa mềm thành công");
    }
}
