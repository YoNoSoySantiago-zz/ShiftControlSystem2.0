package model;

import java.util.Comparator;

public class SortByLastName implements Comparator{

	@Override
	public int compare(Object arg0, Object arg1) {
		User one = (User)arg0;
		User two = (User)arg1;
		return one.getLastName().compareTo(two.getLastName());
	}
	
}
