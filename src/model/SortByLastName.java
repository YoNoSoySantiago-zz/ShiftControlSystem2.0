package model;

import java.io.Serializable;
import java.util.Comparator;

@SuppressWarnings({ "rawtypes", "serial" })
public class SortByLastName implements Comparator,Serializable{

	@Override
	public int compare(Object arg0, Object arg1) {
		User one = (User)arg0;
		User two = (User)arg1;
		int result =0;
		if(one.getLastName().compareTo(two.getLastName())>0) {
			result =1;
		}else if(one.getLastName().compareTo(two.getLastName())<0){
			result=-1;
		}
		
		return result;
	}
	
}
