package duantn.backend.controller.account;

import duantn.backend.authentication.CustomException;
import duantn.backend.authentication.CustomJwtAuthenticationFilter;
import duantn.backend.authentication.CustomUserDetailsService;
import duantn.backend.authentication.JwtUtil;
import duantn.backend.dao.StaffRepository;
import duantn.backend.model.dto.input.*;
import duantn.backend.model.dto.output.CustomerOutputDTO;
import duantn.backend.model.dto.output.Message;
import duantn.backend.model.dto.output.StaffOutputDTO;
import duantn.backend.model.entity.Staff;
import duantn.backend.service.AccountService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class Account {
    final
    AccountService accountService;

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService userDetailsService;

    private final JwtUtil jwtTokenUtil;

    final
    StaffRepository staffRepository;

    public Account(AccountService accountService, AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtUtil jwtTokenUtil, StaffRepository staffRepository) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
        this.staffRepository = staffRepository;
    }


    @PostMapping("/sign-up")
    public Message customerSignup(@Valid @RequestBody SignupDTO signupDTO, HttpServletRequest request) throws CustomException{
        return accountService.customerSignup(signupDTO, request);
    }

    @GetMapping("/confirm")
    public Message confirmEmail(@RequestParam(value = "token-customer") String token,
                                @RequestParam String email) throws CustomException{
        return accountService.confirmEmail(token, email);
    }

    @PostMapping("/login")
    public Map<String, String> login(@Valid @RequestBody LoginDTO loginDTO) throws Exception {
        //login danh cho khach hang ->role: customer
        //login danh cho nhan vien -> role: admin, super admin
        return accountService.login(loginDTO);
    }

    //Brear Token
    //isRefreshToken = true (Header)
    @GetMapping("/refreshtoken")
    public Map<String, String> refreshtoken(HttpServletRequest request) throws Exception {
        return accountService.refreshtoken(request);
    }

    @GetMapping("/forgot")
    public Message forgotPassword(@RequestParam String email) throws CustomException {
        return accountService.forgotPassword(email);
    }

    @PostMapping("/reset-password")
    public Message resetPassword(@Valid @RequestBody ResetPasswordDTO resetPasswordDTO) throws CustomException{
        return accountService.resetPassword(resetPasswordDTO);
    }

    @GetMapping("/admin/{id}")
    public StaffOutputDTO staffProfile(@PathVariable Integer id, HttpServletRequest request)
    throws CustomException{
        return accountService.staffDetail(id, request);
    }

    @PostMapping("/admin/update-profile")
    public StaffOutputDTO staffUpdateProfile(@RequestBody StaffPersonUpdateDTO staffPersonUpdateDTO,
                                             HttpServletRequest request)
        throws CustomException{
        return accountService.staffUpdateProfile(staffPersonUpdateDTO, request);
    }

    @GetMapping("/customer/{id}")
    public CustomerOutputDTO customerProfile(@PathVariable Integer id, HttpServletRequest request)
            throws CustomException{
        return accountService.customerProfile(id, request);
    }

    @PostMapping("/customer/update-profile")
    public CustomerOutputDTO customerUpdateProfile(@RequestBody CustomerUpdateDTO customerUpdateDTO,
                                             HttpServletRequest request)
            throws CustomException{
        return accountService.customerUpdateProfile(customerUpdateDTO, request);
    }
}
