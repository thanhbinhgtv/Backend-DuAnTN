package duantn.backend.helper;

import duantn.backend.authentication.CustomException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateHelper {
    static final SimpleDateFormat DATE_FORMATER = new SimpleDateFormat("dd/MM/yyyy");

    public static Date toDate(String date, String... pattern) {
        try {
            if (pattern.length > 0) {
                DATE_FORMATER.applyPattern(pattern[0]);
            }
            if (date == null) {
                return DateHelper.now();
            }
            return DATE_FORMATER.parse(date);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public static Date now() {
        return new Date();
    }

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
