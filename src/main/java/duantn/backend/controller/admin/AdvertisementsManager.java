package duantn.backend.controller.admin;

import duantn.backend.dao.AdvertisementRepository;
import duantn.backend.entity.Advertisements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdvertisementsManager {

    final
    AdvertisementRepository advertisementRepository;

    @Autowired
    public AdvertisementsManager(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    //    list quảng cáo	Get/admin/advertisements
    @GetMapping("/advertisements")
    public ResponseEntity<List<Advertisements>> listAdvertisements() {
        return ResponseEntity.ok(advertisementRepository.findAll());
    }

    //    tìm theo title	Get/admin/advertisements?title={title}
    @GetMapping(value = "/advertisements", params = "title")
    public ResponseEntity<List<Advertisements>>
    findAdvertisementsByTitle(@RequestParam("title") String title) {
        return ResponseEntity.ok(advertisementRepository.findByTitle(title));
    }

    //    đăng quản cáo	Post/admin/advertisements
    @PostMapping("/advertisements")
    public ResponseEntity<?> postAdvertisement(@RequestBody Advertisements advertisement) {
        Advertisements newAdvertisement = advertisementRepository.save(advertisement);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newAdvertisement.getAdvertisementId()).toUri();

        return ResponseEntity.created(location).build();
    }

    //    sửa quảng cáo	Put/admin/advertisements
    @PutMapping("/advertisements")
    public ResponseEntity<?> updateAdvertisement(@RequestBody Advertisements advertisement) {
        Advertisements newAdvertisement = advertisementRepository.save(advertisement);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}").buildAndExpand(newAdvertisement.getAdvertisementId()).toUri();

        return ResponseEntity.created(location).build();
    }

//    xóa quảng cáo	Delete/admin/advertisements/id
    @DeleteMapping("/advertisements/{id}")
    public void deleteAdvertisement(@PathVariable int id){
        advertisementRepository.deleteById(id);
    }

}
