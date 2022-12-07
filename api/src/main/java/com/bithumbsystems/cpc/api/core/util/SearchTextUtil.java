package com.bithumbsystems.cpc.api.core.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SearchTextUtil {

  public static String specialCharacterValidation(String keyword) {

    StringBuilder sb = new StringBuilder();

    if (!keyword.equals("")) {
      int specialCharacterCount = 0;
      String regex = "[^a-zA-Z0-9가-힣]";

      for (int i = 0; i < keyword.length(); i++) {
        Character character = keyword.charAt(i);
        Matcher matcher = Pattern.compile(regex).matcher(character.toString());

        if (matcher.find()) {
          sb.insert(i + specialCharacterCount, "\\" + character);
          specialCharacterCount++;
        } else {
          sb.insert(i + specialCharacterCount, character);
        }
      }
      return sb.toString();
    }
    return keyword;
  }


}
