package duantn.backend.helper;

import duantn.backend.authentication.CustomException;
import duantn.backend.authentication.CustomJwtAuthenticationFilter;
import duantn.backend.authentication.JwtUtil;
import duantn.backend.dao.CustomerRepository;
import duantn.backend.dao.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class Helper {
    final
    CustomerRepository customerRepository;
    final
    StaffRepository staffRepository;
    final
    JwtUtil jwtUtil;
    final
    CustomJwtAuthenticationFilter customJwtAuthenticationFilter;

    public Helper(CustomerRepository customerRepository, StaffRepository staffRepository, JwtUtil jwtUtil, CustomJwtAuthenticationFilter customJwtAuthenticationFilter) {
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
        this.jwtUtil = jwtUtil;
        this.customJwtAuthenticationFilter = customJwtAuthenticationFilter;
    }


    public String getHostUrl(String url, String substring) {
        int index = url.indexOf(substring);
        String hostUrl = url.substring(0, index);
        return hostUrl;
    }

    public String getBaseURL(HttpServletRequest request) {
        String scheme = request.getScheme();
        String serverName = request.getServerName();
        int serverPort = request.getServerPort();
        String contextPath = request.getContextPath();
        StringBuffer url =  new StringBuffer();
        url.append(scheme).append("://").append(serverName);
        if ((serverPort != 80) && (serverPort != 443)) {
            url.append(":").append(serverPort);
        }
        url.append(contextPath);
        if(url.toString().endsWith("/")){
            url.append("/");
        }
        return url.toString();
    }

    public String getEmailFromRequest(HttpServletRequest request) throws CustomException {
        try{
            String token= customJwtAuthenticationFilter.extractJwtFromRequest(request);
            String email=jwtUtil.getUsernameFromToken(token);
            return email;
        }catch (Exception e){
            throw new CustomException("Token bị thiếu, hoặc không hợp lệ");
        }
    }

    public String createToken(int sl) {
        //create token
        String token;
        while (true) {
            token = randomAlphaNumeric(sl);
            if (customerRepository.findByToken(token) == null
                    && staffRepository.findByToken(token) == null) break;
        }
        return token;
    }

    private final String alpha = "abcdefghijklmnopqrstuvwxyz"; // a-z
    private final String alphaUpperCase = alpha.toUpperCase(); // A-Z
    private final String digits = "0123456789"; // 0-9
    private final String ALPHA_NUMERIC = alpha + alphaUpperCase + digits;

    private Random generator = new Random();

    /**
     * Random string with a-zA-Z0-9, not included special characters
     */
    public String randomAlphaNumeric(int numberOfCharactor) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < numberOfCharactor; i++) {
            int number = randomNumber(0, ALPHA_NUMERIC.length() - 1);
            char ch = ALPHA_NUMERIC.charAt(number);
            sb.append(ch);
        }
        return sb.toString();
    }

    public int randomNumber(int min, int max) {
        return generator.nextInt((max - min) + 1) + min;
    }

    public String listToText(List<String> list){
        String string="";
        for (String s:list){
            string=string+s+"___";
        }
        return string;
    }

    public List<String> textToList(String text){
        String[] mang=text.split("___");
        return Arrays.asList(mang);
    }

    public Integer calculateDays(int number, String type, Date starTime){
        Calendar start=Calendar.getInstance();
        start.setTime(starTime);
        long millisecond;
        if(type.trim().equals("week")){
            Calendar end=Calendar.getInstance();
            end.setTime(starTime);
            end.add(Calendar.WEEK_OF_YEAR, number);
            millisecond=end.getTime().getTime()-start.getTime().getTime();
        }else if(type.trim().equals("month")){
            Calendar end=Calendar.getInstance();
            end.setTime(starTime);
            end.add(Calendar.MONTH, number);
            millisecond=end.getTime().getTime()-start.getTime().getTime();
        } else return null;
        int days= (int) (millisecond/(24*3600*1000));
        return days;
    }

    public Date addDayForDate(Integer days, Date date){
        Calendar calendar=Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }
}
