package duantn.backend.service.impl;

import duantn.backend.dao.StaffRepository;
import duantn.backend.model.dto.input.StaffInsertDTO;
import duantn.backend.model.dto.input.StaffUpdateDTO;
import duantn.backend.model.dto.output.Message;
import duantn.backend.model.dto.output.StaffOutputDTO;
import duantn.backend.model.entity.Staff;
import duantn.backend.service.StaffService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

    public StaffServiceImpl(StaffRepository staffRepository, PasswordEncoder passwordEncoder) {
        this.staffRepository = staffRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public List<StaffOutputDTO> listStaff(Integer page, Integer limit) {

        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        List<Staff> staffList;
        if (page != null && limit != null) {
            Page<Staff> pages = staffRepository.findByDeletedFalse(PageRequest.of(page, limit));
            staffList=pages.toList();
        }
        else staffList = staffRepository.findByDeletedFalse();
        List<StaffOutputDTO> staffOutputDTOList = new ArrayList<>();
        for (Staff staff : staffList) {
            staffOutputDTOList.add(modelMapper.map(staff, StaffOutputDTO.class));
        }
        return staffOutputDTOList;
    }

    @Override
    public ResponseEntity<?> insertStaff(StaffInsertDTO staffInsertDTO) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STRICT);
            Staff staff = modelMapper.map(staffInsertDTO, Staff.class);
            staff.setDob(new Date((staffInsertDTO.getBirthday())));
            staff.setPass(passwordEncoder.encode(staffInsertDTO.getPass()));
            Staff newStaff = staffRepository.save(staff);
            return ResponseEntity.ok(modelMapper.map(newStaff, StaffOutputDTO.class));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new Message("Insert failed"));
        }
    }

    @Override
    public ResponseEntity<?> updateStaff(StaffUpdateDTO staffUpdateDTO) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STRICT);
            Staff staff = modelMapper.map(staffUpdateDTO, Staff.class);
            Staff oldStaff = staffRepository.findByStaffIdAndDeletedFalse(staffUpdateDTO.getStaffId());
            staff.setPass(oldStaff.getPass());
            staff.setDob(new Date(staffUpdateDTO.getBirthday()));
            Staff newStaff = staffRepository.save(staff);
            return ResponseEntity.ok(modelMapper.map(newStaff, StaffOutputDTO.class));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new Message("Update failed"));
        }
    }

    @Override
    public Message blockStaff(Integer id) {
        Staff staff = staffRepository.findByStaffIdAndDeletedFalse(id);
        if (staff == null) return new Message("Block staff id: " + id + " failed");
        else {
            staff.setDeleted(true);
            staffRepository.save(staff);
            return new Message("Block staff id: " + id + " successfully");
        }
    }

    @Override
    public Message activeStaff(Integer id) {
        Optional<Staff> optionalStaff = staffRepository.findById(id);
        if (!optionalStaff.isPresent()) return new Message("StaffId: " + id + " is not found");
        else {
            optionalStaff.get().setDeleted(false);
            staffRepository.save(optionalStaff.get());
            return new Message("Active staff id: " + id + " successfully");
        }
    }

    @Override
    public List<StaffOutputDTO> searchStaff(String search, Integer page, Integer limit) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);
        List<Staff> staffList;
        if (page != null && limit != null) {
            Page<Staff> pages = staffRepository.findByNameLikeOrEmailLikeOrPhoneLikeAndDeletedFalse
                    ("%" + search + "%", "%" + search + "%", "%" + search + "%",
                            PageRequest.of(page, limit));
            staffList=pages.toList();
        }
        else staffList = staffRepository.findByNameLikeOrEmailLikeOrPhoneLikeAndDeletedFalse
                ("%" + search + "%", "%" + search + "%", "%" + search + "%");
        List<StaffOutputDTO> staffOutputDTOList = new ArrayList<>();
        for (Staff staff : staffList) {
            staffOutputDTOList.add(modelMapper.map(staff, StaffOutputDTO.class));
        }
        return staffOutputDTOList;
    }

    @Override
    public ResponseEntity<?> findOneStaff(Integer id) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration()
                    .setMatchingStrategy(MatchingStrategies.STRICT);
            return ResponseEntity.ok(modelMapper.map(staffRepository.findByStaffIdAndDeletedFalse(id),
                    StaffOutputDTO.class));
        } catch (Exception e) {
            return ResponseEntity.ok(new Message("StaffId: " + id + " is not found"));
        }
    }

    @Override
    public Message deleteStaffs() {
        List<Staff> staffList=staffRepository.findByDeletedTrue();
        for(Staff staff:staffList){
            staffRepository.delete(staff);
        }
        return new Message("Deleted successfully");
    }
}
