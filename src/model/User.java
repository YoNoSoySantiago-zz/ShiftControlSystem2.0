package model;

public class User {
	//ATRIBUTES
	private String name;
	private String lastName;
	private String documentType;
	private String documentNumber;
	@SuppressWarnings("unused")
	private String locate;
	private String numberPhone;
	private Shift shift;
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
	public String getDocumentNumber() {
		return documentNumber;
	}

	public Shift getShift() {
		return shift;
	}

	public void setShift(Shift shift) {
		this.shift = shift;
	}
	
}
