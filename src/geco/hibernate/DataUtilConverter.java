package geco.hibernate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataUtilConverter {
	public static Date convertDateFromString(String date){
		Date newDate = null;
		try{
			if(date != null && date.equals("") == false){
				newDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
			}
		}catch(ParseException e){
			e.printStackTrace();
		}
		return newDate;
	}
	public static String convertStringFromDate(Date date){
		String dateString = "";
		if (date != null){
			dateString = new SimpleDateFormat("dd/MM/yyyy").format(date);
		}
		return dateString;
	}
	public static String addSpaceRight(String text,int length){
		if (text.length() >= length){
			text = text.substring(0, length);
		}else{
			int diff = length - text.length();
			for (int i =0; i< diff;i++){
				text = text + " ";
			}
		}
		return text;
	}
	public static String addSpaceRight(String text,int length,String fill ){
		if (text.length() >= length){
			text = text.substring(0, length);
		}else{
			int diff = length - text.length();
			for (int i =0; i< diff;i++){
				text = text + fill;
			}
		}
		return text;
	}
	public static String addSpaceLeft(String text,int length){
		/*if (text.length() >= length){
			text = text.substring(0, length);
		}else{
			int diff = length - text.length();
			for (int i =0; i< diff;i++){
				text = " " +text;
			}
		}*/
		return addSpaceLeft(text,length," ");
	}
	public static String addSpaceLeft(String text,int length,String fill){
		if (text.length() >= length){
			text = text.substring(0, length);
		}else{
			int diff = length - text.length();
			for (int i =0; i< diff;i++){
				text = fill +text;
			}
		}
		return text;
	}
	public static String numberToTrack(String number){
		String intpart = "";
		String decpart ="";
		String[] numsplitted = number.split("\\.");
		if (numsplitted.length == 1){
			intpart = numsplitted[0];
			decpart = "000";
			intpart = addSpaceLeft(intpart,5,"0");
		}else if (numsplitted.length == 2){
			intpart = numsplitted[0];
			decpart = numsplitted[1];
			intpart = addSpaceLeft(intpart,5,"0");
			decpart = addSpaceRight(decpart,3,"0");
		}else{
			intpart = "00000";
			decpart = "000";
		}
		return intpart+decpart;
	}
	public static String dateToTrack(String date){
		Date newDate = null;
		try{
			if(date != null && date.equals("") == false){
				newDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
			}
		}catch(ParseException e){
			e.printStackTrace();
		}
		return new SimpleDateFormat("yyyyMMdd").format(newDate);
	}
}
