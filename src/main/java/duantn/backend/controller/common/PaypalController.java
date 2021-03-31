package duantn.backend.controller.common;

import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payment;
import com.paypal.base.rest.PayPalRESTException;
import duantn.backend.authentication.CustomException;
import duantn.backend.config.paypal.PaypalPaymentIntent;
import duantn.backend.config.paypal.PaypalPaymentMethod;
import duantn.backend.config.paypal.PaypalService;
import duantn.backend.dao.CustomerRepository;
import duantn.backend.helper.Helper;
import duantn.backend.model.dto.output.Message;
import duantn.backend.model.entity.Customer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@RestController
public class PaypalController {

    @Value("${duantn.rate}")
    private Integer rate;

    private String email;

    public static final String URL_PAYPAL_SUCCESS = "pay/success";
    public static final String URL_PAYPAL_CANCEL = "pay/cancel";
    private double value;

    private Logger log = LoggerFactory.getLogger(getClass());

    final
    Helper helper;

    final
    CustomerRepository customerRepository;

    private final PaypalService paypalService;

    public PaypalController(PaypalService paypalService, Helper helper, CustomerRepository customerRepository) {
        this.paypalService = paypalService;
        this.helper = helper;
        this.customerRepository = customerRepository;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/pay")
    public String pay(HttpServletRequest request, HttpServletResponse response,
                      @RequestParam("price") double price,
                      @RequestParam("email") String email)
            throws CustomException {
        String cancelUrl = helper.getBaseURL(request) + "/" + URL_PAYPAL_CANCEL;
        String successUrl = helper.getBaseURL(request) + "/" + URL_PAYPAL_SUCCESS;
        try {
            value = Math.round(price * 100.0) / 100.0;
            //email = helper.getEmailFromRequest(request);
            this.email=email;
            if (email == null || email.trim().equals("")) throw new CustomException("Token không hợp lệ");
            Payment payment = paypalService.createPayment(
                    value,
                    "USD",
                    PaypalPaymentMethod.paypal,
                    PaypalPaymentIntent.sale,
                    "payment description",
                    cancelUrl,
                    successUrl);
            for (Links links : payment.getLinks()) {
                if (links.getRel().equals("approval_url")) {
                    response.sendRedirect(links.getHref());
                    return links.getHref();
                }
            }
        } catch (PayPalRESTException | IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
            throw new CustomException("Thanh toán thất bại");
        }
        //return "redirect:/";
        throw new CustomException("Không xác định");
    }

    @GetMapping(URL_PAYPAL_CANCEL)
    public Message cancelPay() {
        return new Message("Thanh toán bị hủy bỏ");
    }

    @GetMapping(URL_PAYPAL_SUCCESS)
    public Message successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String payerId)
            throws CustomException {
        try {
            Payment payment = paypalService.executePayment(paymentId, payerId);
            if (payment.getState().equals("approved")) {
                Customer customer = customerRepository.findByEmail(email);
                if (customer == null)
                    throw new CustomException("Người dùng không hợp lệ");
                int increase = (int) (value * rate);
                customer.setAccountBalance(customer.getAccountBalance() + increase);
                customerRepository.save(customer);
                return new Message("Thanh toán thành công, số tiền: "+value+" USD - tức "+increase+" VNĐ");
            }
        } catch (PayPalRESTException e) {
            log.error(e.getMessage());
        }
        //return "redirect:/";
        throw new CustomException("Không xác định");
    }

}
