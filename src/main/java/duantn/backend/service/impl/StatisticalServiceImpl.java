package duantn.backend.service.impl;

import duantn.backend.authentication.CustomException;
import duantn.backend.dao.ArticleRepository;
import duantn.backend.dao.CustomerRepository;
import duantn.backend.dao.TransactionRepository;
import duantn.backend.model.dao.ArticleOfDate;
import duantn.backend.service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with YourComputer.
 * User: DUC_PRO
 * Date: 27/04/2021
 * Time: 9:41 CH
 */

@Service
public class StatisticalServiceImpl implements StatisticalService {
    final
    ArticleRepository articleRepository;
    final
    TransactionRepository transactionRepository;
    final
    CustomerRepository customerRepository;

    public StatisticalServiceImpl(ArticleRepository articleRepository, TransactionRepository transactionRepository, CustomerRepository customerRepository) {
        this.articleRepository = articleRepository;
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    public Map<String, Object> statisticArticle(Integer month, Integer year,
                                                      Integer page, Integer limit) throws CustomException {
        if(month!=null && year==null){
            throw new CustomException("Lỗi có tháng thì phải có năm");
        } else if(month!=null && year!=null){
            Calendar start=Calendar.getInstance();
            start.set(year, month-1, 1, 0, 0, 0);
            Calendar end=Calendar.getInstance();
            end.set(year, month, 1, 0, 0, 0);
            Page<Object[]> objectsPage=articleRepository.countArticleOfDate(start.getTime(), end.getTime(), PageRequest.of(page, limit));
            List<Object[]> objectsList= objectsPage.toList();
            List<Map<String, Object>> returnList=new ArrayList<>();
            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
            for (Object[] object: objectsList){
                Map<String, Object> map=new HashMap<>();
                map.put("date", sdf.format((Date)object[0]));
                map.put("number", object[1]);
                returnList.add(map);
            }
            Map<String, Object> returnMap=new HashMap<>();
            returnMap.put("pages",objectsPage.getTotalPages());
            returnMap.put("elements", objectsPage.getTotalElements());
            returnMap.put("data", returnList);
            return returnMap;
        } else if(month==null && year !=null){
            Calendar start=Calendar.getInstance();
            start.set(year, 0, 1, 0, 0, 0);
            Calendar end=Calendar.getInstance();
            end.set(year+1, 0, 1, 0, 0, 0);
            Page<Object[]> objectsPage=articleRepository.countArticleOfMonth(start.getTime(), end.getTime(), PageRequest.of(page, limit));
            List<Object[]> objectsList= objectsPage.toList();
            List<Map<String, Object>> returnList=new ArrayList<>();
            for (Object[] object: objectsList){
                Map<String, Object> map=new HashMap<>();
                map.put("month", object[0]);
                map.put("number", object[1]);
                returnList.add(map);
            }
            Map<String, Object> returnMap=new HashMap<>();
            returnMap.put("pages",objectsPage.getTotalPages());
            returnMap.put("elements", objectsPage.getTotalElements());
            returnMap.put("data", returnList);
            return returnMap;
        }else {
            Page<Object[]> objectsPage=articleRepository.countArticleOfYear(PageRequest.of(page, limit));
            List<Object[]> objectsList= objectsPage.toList();
            List<Map<String, Object>> returnList=new ArrayList<>();
            for (Object[] object: objectsList){
                Map<String, Object> map=new HashMap<>();
                map.put("year", object[0]);
                map.put("number", object[1]);
                returnList.add(map);
            }
            Map<String, Object> returnMap=new HashMap<>();
            returnMap.put("pages",objectsPage.getTotalPages());
            returnMap.put("elements", objectsPage.getTotalElements());
            returnMap.put("data", returnList);
            return returnMap;
        }
    }

    @Override
    public Map<String, Object> statisticRevenue(Integer month, Integer year, Integer page, Integer limit) throws CustomException {
        if(month!=null && year==null){
            throw new CustomException("Lỗi có tháng thì phải có năm");
        } else if(month!=null && year!=null){
            Calendar start=Calendar.getInstance();
            start.set(year, month-1, 1, 0, 0, 0);
            Calendar end=Calendar.getInstance();
            end.set(year, month, 1, 0, 0, 0);
            Page<Object[]> objectsPage=transactionRepository.sumRevenueByDate(start.getTime(), end.getTime(), PageRequest.of(page, limit));
            List<Object[]> objectsList= objectsPage.toList();
            List<Map<String, Object>> returnList=new ArrayList<>();
            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
            for (Object[] object: objectsList){
                Map<String, Object> map=new HashMap<>();
                map.put("date", sdf.format((Date)object[0]));
                map.put("sum", object[1]);
                returnList.add(map);
            }
            Map<String, Object> returnMap=new HashMap<>();
            returnMap.put("pages",objectsPage.getTotalPages());
            returnMap.put("elements", objectsPage.getTotalElements());
            returnMap.put("data", returnList);
            return returnMap;
        } else if(month==null && year !=null){
            Calendar start=Calendar.getInstance();
            start.set(year, 0, 1, 0, 0, 0);
            Calendar end=Calendar.getInstance();
            end.set(year+1, 0, 1, 0, 0, 0);
            Page<Object[]> objectsPage=transactionRepository.sumRevenueByMonth(start.getTime(), end.getTime(), PageRequest.of(page, limit));
            List<Object[]> objectsList= objectsPage.toList();
            List<Map<String, Object>> returnList=new ArrayList<>();
            for (Object[] object: objectsList){
                Map<String, Object> map=new HashMap<>();
                map.put("month", object[0]);
                map.put("sum", object[1]);
                returnList.add(map);
            }
            Map<String, Object> returnMap=new HashMap<>();
            returnMap.put("pages",objectsPage.getTotalPages());
            returnMap.put("elements", objectsPage.getTotalElements());
            returnMap.put("data", returnList);
            return returnMap;
        }else {
            Page<Object[]> objectsPage=transactionRepository.sumRevenueByYear(PageRequest.of(page, limit));
            List<Object[]> objectsList= objectsPage.toList();
            List<Map<String, Object>> returnList=new ArrayList<>();
            for (Object[] object: objectsList){
                Map<String, Object> map=new HashMap<>();
                map.put("year", object[0]);
                map.put("sum", object[1]);
                returnList.add(map);
            }
            Map<String, Object> returnMap=new HashMap<>();
            returnMap.put("pages",objectsPage.getTotalPages());
            returnMap.put("elements", objectsPage.getTotalElements());
            returnMap.put("data", returnList);
            return returnMap;
        }
    }

    @Override
    public Map<String, Object> statisticCustomer(Integer month, Integer year, Integer page, Integer limit) throws CustomException {
        if(month!=null && year==null){
            throw new CustomException("Lỗi có tháng thì phải có năm");
        } else if(month!=null && year!=null){
            Calendar start=Calendar.getInstance();
            start.set(year, month-1, 1, 0, 0, 0);
            Calendar end=Calendar.getInstance();
            end.set(year, month, 1, 0, 0, 0);
            Page<Object[]> objectsPage=customerRepository.countCustomerOfDate(start.getTime(), end.getTime(), PageRequest.of(page, limit));
            List<Object[]> objectsList= objectsPage.toList();
            List<Map<String, Object>> returnList=new ArrayList<>();
            SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
            for (Object[] object: objectsList){
                Map<String, Object> map=new HashMap<>();
                map.put("date", sdf.format((Date)object[0]));
                map.put("number", object[1]);
                returnList.add(map);
            }
            Map<String, Object> returnMap=new HashMap<>();
            returnMap.put("pages",objectsPage.getTotalPages());
            returnMap.put("elements", objectsPage.getTotalElements());
            returnMap.put("data", returnList);
            return returnMap;
        } else if(month==null && year !=null){
            Calendar start=Calendar.getInstance();
            start.set(year, 0, 1, 0, 0, 0);
            Calendar end=Calendar.getInstance();
            end.set(year+1, 0, 1, 0, 0, 0);
            Page<Object[]> objectsPage=customerRepository.countCustomerOfMonth(start.getTime(), end.getTime(), PageRequest.of(page, limit));
            List<Object[]> objectsList= objectsPage.toList();
            List<Map<String, Object>> returnList=new ArrayList<>();
            for (Object[] object: objectsList){
                Map<String, Object> map=new HashMap<>();
                map.put("month", object[0]);
                map.put("number", object[1]);
                returnList.add(map);
            }
            Map<String, Object> returnMap=new HashMap<>();
            returnMap.put("pages",objectsPage.getTotalPages());
            returnMap.put("elements", objectsPage.getTotalElements());
            returnMap.put("data", returnList);
            return returnMap;
        }else {
            Page<Object[]> objectsPage=customerRepository.countCustomerOfYear(PageRequest.of(page, limit));
            List<Object[]> objectsList= objectsPage.toList();
            List<Map<String, Object>> returnList=new ArrayList<>();
            for (Object[] object: objectsList){
                Map<String, Object> map=new HashMap<>();
                map.put("year", object[0]);
                map.put("number", object[1]);
                returnList.add(map);
            }
            Map<String, Object> returnMap=new HashMap<>();
            returnMap.put("pages",objectsPage.getTotalPages());
            returnMap.put("elements", objectsPage.getTotalElements());
            returnMap.put("data", returnList);
            return returnMap;
        }
    }
}
