package duantn.backend.controller.superAdmin;

import duantn.backend.model.dao.ArticleOfDate;
import duantn.backend.service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created with YourComputer.
 * User: DUC_PRO
 * Date: 27/04/2021
 * Time: 9:32 CH
 */

@RestController
@RequestMapping("/super-admin")
public class Statistical {
    final
    StatisticalService statisticalService;

    public Statistical(StatisticalService statisticalService) {
        this.statisticalService = statisticalService;
    }

    //    thống kê bài số đăng theo ngày trong tháng/tháng trong năm/ theo năm
    @GetMapping("/statistic/article")
    public Map<String, Object> StatisticArticle(@RequestParam(required = false) Integer month,
                                                @RequestParam(required = false) Integer year,
                                                @RequestParam Integer page,
                                                @RequestParam Integer limit) {
        return statisticalService.statisticArticle(month, year, page, limit);
    }

    //    thống kê doanh thu theo ngày trong tháng/tháng trong năm/ theo năm
    @GetMapping("/statistic/revenue")
    public Map<String, Object> StatisticRevenue(@RequestParam(required = false) Integer month,
                                                @RequestParam(required = false) Integer year,
                                                @RequestParam Integer page,
                                                @RequestParam Integer limit) {
        return statisticalService.statisticRevenue(month, year, page, limit);
    }

    //    thống kê số tài khoản mới theo ngày trong tháng/tháng trong năm/ theo năm
    @GetMapping("/statistic/customer")
    public Map<String, Object> StatisticCustomer(@RequestParam(required = false) Integer month,
                                                 @RequestParam(required = false) Integer year,
                                                 @RequestParam Integer page,
                                                 @RequestParam Integer limit) {
        return statisticalService.statisticCustomer(month, year, page, limit);
    }
//    thống kê số lượt truy cập theo ngày tháng năm
//    thống kê hoạt động của nhân viên theo ngày/tháng
}
