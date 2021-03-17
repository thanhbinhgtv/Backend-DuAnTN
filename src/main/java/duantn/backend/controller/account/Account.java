package duantn.backend.controller.account;

import duantn.backend.authentication.CustomException;
import duantn.backend.authentication.CustomUserDetailsService;
import duantn.backend.authentication.JwtUtil;
import duantn.backend.model.dto.input.LoginDTO;
import duantn.backend.model.dto.input.ResetPasswordDTO;
import duantn.backend.model.dto.input.SignupDTO;
import duantn.backend.model.dto.output.Message;
import duantn.backend.service.AccountService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class Account {
    final
    AccountService accountService;

    private final AuthenticationManager authenticationManager;

    private final CustomUserDetailsService userDetailsService;

    private final JwtUtil jwtTokenUtil;

    public Account(AccountService accountService, AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JwtUtil jwtTokenUtil) {
        this.accountService = accountService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }


    @PostMapping("/sign-up")
    public Message customerSignup(@RequestBody SignupDTO signupDTO, HttpServletRequest request) throws CustomException{
        return accountService.customerSignup(signupDTO, request);
    }

    @GetMapping("/confirm")
    public Message confirmEmail(@RequestParam(value = "token-customer") String token,
                                @RequestParam String email) throws CustomException{
        return accountService.confirmEmail(token, email);
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody LoginDTO loginDTO) throws Exception {
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
    public Message resetPassword(@RequestBody ResetPasswordDTO resetPasswordDTO) throws CustomException{
        return accountService.resetPassword(resetPasswordDTO);
    }
}
