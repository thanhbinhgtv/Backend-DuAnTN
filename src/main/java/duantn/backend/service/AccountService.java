package duantn.backend.service;

import duantn.backend.authentication.CustomException;
import duantn.backend.model.dto.input.LoginDTO;
import duantn.backend.model.dto.input.ResetPasswordDTO;
import duantn.backend.model.dto.input.SignupDTO;
import duantn.backend.model.dto.output.Message;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface AccountService {
    Message customerSignup(SignupDTO signupDTO, HttpServletRequest request) throws CustomException;

    //sau nay chuyen sang void
    Message confirmEmail(String token, String email) throws CustomException;

    Map<String, String> login(LoginDTO loginDTO) throws Exception;

    Map<String, String> refreshtoken(HttpServletRequest request) throws Exception;

    Message forgotPassword(String email) throws CustomException;

    Message resetPassword(ResetPasswordDTO resetPasswordDTO) throws CustomException;
}
