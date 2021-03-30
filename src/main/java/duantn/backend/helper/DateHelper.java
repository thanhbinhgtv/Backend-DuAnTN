package duantn.backend.helper;

import duantn.backend.authentication.CustomException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    public static Date changeTime(Date date, String time) throws CustomException{
        try{
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("dd/MM/yyyy");
            String stringDate=simpleDateFormat.format(date);
            simpleDateFormat.applyPattern("dd/MM/yyyy HH:mm:ss");
            return simpleDateFormat.parse(stringDate+" "+time);
        }catch (Exception e){
            throw new CustomException("Lỗi chuyển đổi thời gian");
        }
    }
}
