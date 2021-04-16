package duantn.backend.controller.customer;

import duantn.backend.authentication.CustomException;
import duantn.backend.model.dto.output.ArticleOutputDTO;
import duantn.backend.model.dto.output.NewspaperOutputDTO;
import duantn.backend.service.ArticleService;
import duantn.backend.service.CustomerNoLoginService;
import duantn.backend.service.NewspaperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerNoLoginManager {
    final
    CustomerNoLoginService customerNoLoginService;
    final
    NewspaperService newspaperService;

    public CustomerNoLoginManager(CustomerNoLoginService customerNoLoginService, NewspaperService newspaperService) {
        this.customerNoLoginService = customerNoLoginService;
        this.newspaperService = newspaperService;
    }

    @GetMapping("/article")
    public List<ArticleOutputDTO> listArticle(
            @RequestParam(required = false) Long start, @RequestParam(required = false) Long end,
            @RequestParam(required = false) Integer ward,
            @RequestParam(required = false) Integer district,
            @RequestParam(required = false) Integer city,
            @RequestParam(required = false) Boolean roommate,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer minAcreage,
            @RequestParam(required = false) Integer maxAcreage,
            @RequestParam Integer page,
            @RequestParam Integer limit
    ) {
        return customerNoLoginService.listArticle(start,end,ward,district,city,
                roommate,search,minAcreage,maxAcreage,page,limit);
    }

    @GetMapping("/article/{id}")
    public ArticleOutputDTO findOneArticle(@PathVariable Integer id) throws CustomException {
        return customerNoLoginService.findOneArticle(id);
    }

    @GetMapping("/new")
    public List<NewspaperOutputDTO> listNewspaper(@RequestParam(required = false) String sort,
                                                  @RequestParam(required = false) Boolean hidden,
                                                  @RequestParam(required = false) String title,
                                                  @RequestParam Integer page,
                                                  @RequestParam Integer limit) {
        return newspaperService.listNewspaper(sort, hidden, title, page, limit);
    }

    @GetMapping("/new/{id}")
    public NewspaperOutputDTO newspaperDetail(@PathVariable Integer id) throws CustomException {
        return newspaperService.findOneNewspaper(id);
    }
}
