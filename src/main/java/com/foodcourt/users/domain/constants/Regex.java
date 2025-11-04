package com.foodcourt.users.domain.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Regex {
	
	public static final String JUST_NUMBERS = "^\\d+$";
	public static final String JUST_NUMBERS_AND_SYMBOL_PLUS = "^(\\+\\d{1,12}|\\d{1,13})$";
	
}
