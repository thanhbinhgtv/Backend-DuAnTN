package duantn.backend.service.impl;

import duantn.backend.dao.AdvertisementRepository;
import duantn.backend.dao.StaffRepository;
import duantn.backend.model.dto.input.AdvertisementInsertDTO;
import duantn.backend.model.dto.input.AdvertisementUpdateDTO;
import duantn.backend.model.dto.output.AdvertisementOutputDTO;
import duantn.backend.model.dto.output.Message;
import duantn.backend.model.entity.Advertisement;
import duantn.backend.model.entity.Staff;
import duantn.backend.service.AdvertisementService;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public  class AdvertisementServiceIplm implements AdvertisementService {
    final
    AdvertisementRepository adverRepository;

    //tiem nek
    final
    StaffRepository staffRepository;

    public AdvertisementServiceIplm(AdvertisementRepository adverRepository, StaffRepository staffRepository) {
        this.adverRepository = adverRepository;
        this.staffRepository = staffRepository;
    }

    @Override
    public List<AdvertisementOutputDTO> listAdvertisement(Integer page, Integer limit) {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        List<Advertisement> advertisementList;
        if (page != null && limit != null) {
            Page<Advertisement> pages = adverRepository.findByDeletedFalse(PageRequest.of(page, limit));
            advertisementList = pages.toList();
        } else
            advertisementList = adverRepository.findByDeletedFalse();
        List<AdvertisementOutputDTO> advertisementOutputDTOS = new ArrayList<>();
        for (Advertisement advertisement : advertisementList) {
            advertisementOutputDTOS.add(modelMapper.map(advertisement, AdvertisementOutputDTO.class));
        }

        return advertisementOutputDTOS;
    }

    @Override
    public ResponseEntity<?> insertAdvertisement(AdvertisementInsertDTO advertisementInsertDTO) {
        try {
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
            Advertisement advertisement = modelMapper.map(advertisementInsertDTO, Advertisement.class);

            //truy van ve staff theo staff id
            Optional<Staff> staff=staffRepository.findById(advertisementInsertDTO.getStaffId());
            //gan staff cho advertisement
            if(staff.isPresent()){
                advertisement.setStaff(staff.get());
            }
            Advertisement newAdvertisement = adverRepository.save(advertisement);
            return ResponseEntity.ok(new AdvertisementOutputDTO(newAdvertisement));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new Message("Insert failed"));
        }

    }

    @Override
    public ResponseEntity<?> updateAdvertisement(AdvertisementUpdateDTO advertisementUpdateDTO) {
        try{
            ModelMapper modelMapper = new ModelMapper();
            Advertisement advertisement = modelMapper.map(advertisementUpdateDTO, Advertisement.class);

            //truy van old advertisement
            Optional<Advertisement> optionalAdvertisement=adverRepository.findById(advertisementUpdateDTO.getAdvertisementId());
            Advertisement oldAdvertisement=optionalAdvertisement.get();

            //gan staff id cu
            advertisement.setStaff(oldAdvertisement.getStaff());

            Advertisement newAdvertisement = adverRepository.save(advertisement);
            return ResponseEntity.ok(new AdvertisementOutputDTO(newAdvertisement));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(new Message("Update failed"));
        }
    }

    @Override
    public ResponseEntity<String> deleteAdvertisement(Integer id) {
        Advertisement advertisement = adverRepository.findByAdvertisementIdAndDeletedFalse(id);
        if (advertisement == null){
            return ResponseEntity.badRequest().body("Id: " +id+" does not exist");
        }else{
            advertisement.setDeleted(true);
            adverRepository.save(advertisement);
        }
        return ResponseEntity.ok("Success full");
    }

}
