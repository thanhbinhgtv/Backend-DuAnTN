package duantn.backend.controller.admin;

import duantn.backend.model.dto.input.AdvertisementInsertDTO;
import duantn.backend.model.dto.input.AdvertisementUpdateDTO;
import duantn.backend.model.dto.output.AdvertisementOutputDTO;
import duantn.backend.service.AdvertisementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/")
public class AdvertisementManage {
    final AdvertisementService advertisementService;

    public AdvertisementManage(AdvertisementService advertisementService) {
        this.advertisementService = advertisementService;
    }

    @GetMapping("advertisement")
    public List<AdvertisementOutputDTO> listAdvertisement(@RequestParam(required = false) Integer page,
                                                          @RequestParam(required = false) Integer limit) {
        return advertisementService.listAdvertisement(page, limit);
    }
    @PostMapping("advertisement")
    public ResponseEntity<?>insertAdvertisement (@RequestBody AdvertisementInsertDTO advertisementInsertDTO) {
        return advertisementService.insertAdvertisement(advertisementInsertDTO);
    }

    @PutMapping("advertisement")
    public ResponseEntity<?> updateAdvertisement(@RequestBody AdvertisementUpdateDTO advertisementUpdateDTO) {
        return advertisementService.updateAdvertisement(advertisementUpdateDTO);
    }
    @DeleteMapping("advertisement/{id}")
    public ResponseEntity<String> blockAdvertisement(@PathVariable Integer id) {
        return advertisementService.deleteAdvertisement(id);

    }
}
