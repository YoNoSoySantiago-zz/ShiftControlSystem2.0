package model;

public class User implements Comparable{
	//ATRIBUTES
	private String name;
	private String lastName;
	private String documentType;
	private String documentNumber;
	@SuppressWarnings("unused")
	private String locate;
	private String numberPhone;
	private Shift shift;
	private int ban=0;
	public static final String CC = "citizenship card";
	public static final String CR = "civil registration";
	public static final String PS = "passport";
	public static final String FIC = "foreign identity card";
	public static final String UNKNOWN = "unknown";
	
	//CONTRUCTOR
	public User(String name,String lastName,String documentType,String documentNumber,String locate,String numberPhone,Shift shift) {
		this.name = name;
		this.lastName= lastName;
		this.documentType=documentType;
		this.documentNumber = documentNumber;
		this.locate = locate;
		this.numberPhone = numberPhone;
		this.shift = shift;
	}
	
	//GETTERS AND SETTERS
	public String toString() {
		String result="full: "+name+" "+lastName+"\ndocument type: "+documentType+"\nnumber Phone: "+numberPhone;
		return result.trim();
	}

	public String getDocumentType() {
		return documentType;
	}

	public String getName() {
		return name;
	}
	public String getLastName() {
		return lastName;
	}
	public String getDocumentNumber() {
		return documentNumber;
	}

	public Shift getShift() {
		return shift;
	}

	public void setShift(Shift shift) {
		this.shift = shift;
	}

	/**
	 * @return the ban
	 */
	public int getBan() {
		return ban;
	}

	/**
	 * @param ban the ban to set
	 */
	public void setBan(int ban) {
		this.ban = ban;
	}
	

	@Override
	public int compareTo(Object o) {
		User user = (User)o;
		int result =0;
		if(shift.getLetter()>user.getShift().getLetter()) {
			result =1;
		}else if(shift.getLetter()<user.getShift().getLetter()) {
			result =-1;
		}else {
			if(shift.getNumber()>user.getShift().getNumber()) {
				result =1;
			}else if(shift.getNumber()<user.getShift().getNumber()) {
				result=-1;
			}
		}
		return result;
	}
	
}
