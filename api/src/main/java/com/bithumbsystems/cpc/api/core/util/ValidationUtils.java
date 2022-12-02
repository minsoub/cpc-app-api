package com.bithumbsystems.cpc.api.core.util;

import com.bithumbsystems.cpc.api.core.exception.InvalidParameterException;
import com.bithumbsystems.cpc.api.core.model.enums.ErrorCode;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.util.StringUtils;

public class ValidationUtils {

  public static boolean assertEmailFormat(String value) throws InvalidParameterException {
    if (!EmailValidator.getInstance().isValid(value)) {
      return false;
    }
    return true;
  }

  public static boolean assertNameFormat(String value) {
    String regexp = "^[가-힣]*$";
    Pattern pattern = Pattern.compile(regexp);
    Matcher matcher = pattern.matcher(value);
    return matcher.matches();
  }

  public static boolean assertCellPhoneFormat(String value) throws InvalidParameterException {
    String regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";
    Pattern pattern = Pattern.compile(regexp);
    Matcher matcher = pattern.matcher(value);
    return matcher.matches();
  }

  public static void assertAllowFileExt(String fileName, String[] allowExt) {
    String ext = StringUtils.getFilenameExtension(fileName).toUpperCase();

    if(Arrays.asList(allowExt).contains(ext) == false){
      throw new InvalidParameterException(ErrorCode.NOT_ALLOWED_FILE_EXT);
    }
  }
}
