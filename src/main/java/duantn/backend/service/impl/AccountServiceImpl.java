package duantn.backend.service.impl;

import duantn.backend.authentication.CustomUserDetailsService;
import duantn.backend.authentication.JwtUtil;
import duantn.backend.component.MailSender;
import duantn.backend.dao.CustomerRepository;
import duantn.backend.dao.StaffRepository;
import duantn.backend.dao.TokenCustomerRepository;
import duantn.backend.model.dto.input.LoginDTO;
import duantn.backend.model.dto.input.SignupDTO;
import duantn.backend.model.dto.output.Message;
import duantn.backend.model.entity.Customer;
import duantn.backend.model.entity.TokenCustomer;
import duantn.backend.service.AccountService;
import org.apache.commons.lang3.RandomStringUtils;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class AccountServiceImpl implements AccountService {
    final
    PasswordEncoder passwordEncoder;

    final
    CustomerRepository customerRepository;

    final
    TokenCustomerRepository tokenCustomerRepository;

    final
    MailSender mailSender;

    final
    StaffRepository staffRepository;

    final
    AuthenticationManager authenticationManager;
    final
    CustomUserDetailsService userDetailsService;
    final
    JwtUtil jwtTokenUtil;

    public AccountServiceImpl(PasswordEncoder passwordEncoder, CustomerRepository customerRepository, TokenCustomerRepository tokenCustomerRepository, MailSender mailSender, StaffRepository staffRepository, AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtUtil jwtTokenUtil) {
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
        this.tokenCustomerRepository = tokenCustomerRepository;
        this.mailSender = mailSender;
        this.staffRepository = staffRepository;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }
    @Override
    public Message customerSignup(SignupDTO signupDTO, HttpServletRequest request) {
        ModelMapper modelMapper=new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT);

        if(customerRepository.findByEmail(signupDTO.getEmail())!=null
        || staffRepository.findByEmail(signupDTO.getEmail())!=null)
            return new Message("Email is already in use");

        //create customer
        Customer customer=modelMapper.map(signupDTO, Customer.class);
        customer.setAccountBalance(0);
        customer.setPass(passwordEncoder.encode(signupDTO.getPass()));

        //create token
        TokenCustomer tokenCustomer=new TokenCustomer();
        tokenCustomer.setCustomer(customer);
        String token;
        while (true){
            token= RandomStringUtils.random(15);
            if(tokenCustomerRepository.findByToken(token) ==null) break;
        }
        tokenCustomer.setToken(token);
        tokenCustomer.setType(false);
        tokenCustomerRepository.save(tokenCustomer);

        //send mail
        mailSender.send(
                signupDTO.getEmail(),
                "Xác nhận địa chỉ email",
                "<h2>Xác nhận địa chỉ email</h2>"+
                        "Click vào đường link sau để xác nhận email và kích hoạt tài khoản của bạn:<br/>"+
                        request.getServletContext().getContextPath()+"/confirm?token-customer="+token
        );
        return new Message("Please check your mail to confirm");
    }

    @Override
    public Message confirmEmail(String token) {
        TokenCustomer tokenCustomer=tokenCustomerRepository.findByToken(token);
        if(tokenCustomer!=null){
            Customer customer= tokenCustomer.getCustomer();
            customer.setEnabled(true);
            customerRepository.save(customer);
            return new Message("Confirm email successfully");
        }else return new Message("Confirm failed");
    }

    @Override
    public Map<String, String> login(LoginDTO loginDTO) throws Exception{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDTO.getEmail(), loginDTO.getPass()));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());

        final String token = jwtTokenUtil.generateToken(userDetails);

        String role;
        if(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_SUPER_ADMIN")))
            role="SUPER_ADMIN";
        else if(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")))
            role="ADMIN";
        else role="CUSTOMER";

        Map<String, String> returnMap=new HashMap<>();
        returnMap.put("role", role);
        returnMap.put("token", token);

        return returnMap;
    }
}
