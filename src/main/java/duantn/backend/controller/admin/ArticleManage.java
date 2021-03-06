package duantn.backend.controller.admin;

import duantn.backend.model.dto.input.ArticleInsertDTO;
import duantn.backend.model.dto.input.ArticleUpdateDTO;
import duantn.backend.model.dto.output.ArticleOutputDTO;
import duantn.backend.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/")
public class ArticleManage {
    final
    ArticleService service;

    public ArticleManage(ArticleService service) {
        this.service = service;
    }


    @DeleteMapping("articles/{id}")
    public ResponseEntity<String> blockArticle(@PathVariable Integer id) {
        return service.deleteArticle(id);

    }

    @GetMapping("articles/active/{id}")
    public ResponseEntity<String> activeArticle(@PathVariable Integer id) {

        return service.activeArticle(id);
    }

    @PostMapping("articles")
    public ResponseEntity<?> insertArticle(@RequestBody ArticleInsertDTO articleInsertDTO) {
        return service.insertArticle(articleInsertDTO);
    }

    @PutMapping("articles")
    public ResponseEntity<?> updateArticle(@RequestBody ArticleUpdateDTO articleUpdateDTO) {
        return service.updateArticle(articleUpdateDTO);
    }

    @GetMapping("articles/{id}")
    public ResponseEntity<?> findbyID(@PathVariable Integer id) {
        return service.findOneArticle(id);
    }

    @GetMapping("articles")
    public List<ArticleOutputDTO> listFilterArticle(
         @RequestParam(required = false) Boolean status,
         @RequestParam(required = false) Long start, @RequestParam(required = false) Long end,
         @RequestParam(value = "ward-id", required = false) Integer wardId,
         @RequestParam(value = "district-id", required = false) Integer districtId,
         @RequestParam(value = "city-id", required = false) Integer cityId,
         @RequestParam(required = false) String sort,
         @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer limit
    ){
        return service.filterArticle(status,start,end,wardId,districtId,cityId,sort,page,limit);
    }

}
