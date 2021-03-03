package duantn.backend.service;

import duantn.backend.model.dto.input.LoginDTO;
import duantn.backend.model.dto.input.SignupDTO;
import duantn.backend.model.dto.output.Message;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AccountService {
    Message customerSignup(SignupDTO signupDTO, HttpServletRequest request);

    Message confirmEmail(String token);

    Map<String, String> login(LoginDTO loginDTO) throws Exception;
}
