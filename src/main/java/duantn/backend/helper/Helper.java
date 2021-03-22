package duantn.backend.helper;

import duantn.backend.dao.CustomerRepository;
import duantn.backend.dao.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class Helper {
    final
    CustomerRepository customerRepository;
    final
    StaffRepository staffRepository;

    public Helper(CustomerRepository customerRepository, StaffRepository staffRepository) {
        this.customerRepository = customerRepository;
        this.staffRepository = staffRepository;
    }


    public String getHostUrl(String url, String substring) {
        int index = url.indexOf(substring);
        String hostUrl = url.substring(0, index);
        return hostUrl;
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
}
