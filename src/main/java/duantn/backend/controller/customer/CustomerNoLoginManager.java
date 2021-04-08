package duantn.backend.controller.customer;

import duantn.backend.model.dto.output.ArticleOutputDTO;
import duantn.backend.service.ArticleService;
import duantn.backend.service.CustomerNoLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CustomerNoLoginManager {
    final
    CustomerNoLoginService customerNoLoginService;

    public CustomerNoLoginManager(CustomerNoLoginService customerNoLoginService) {
        this.customerNoLoginService = customerNoLoginService;
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
}
