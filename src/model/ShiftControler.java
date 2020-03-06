package model;
import exceptions.*;

import java.util.ArrayList;

public class ShiftControler {
	//ATRIBUTES
	private ArrayList<User> users;
	private ArrayList<User> userShift;
	private Shift shift;
	
	public ArrayList<User> getUserShift() {
		return userShift;
	}
	//CONTRUCTOR
	public ShiftControler() {
		users = new ArrayList<User>();
		userShift = new ArrayList<User>();
		shift = new Shift('A',0,"A00",false,true);
	}
	
	//METHODS
	/**
	 * This method allows you to search for a user's information to return it in a string of characters, 
	 * the method searches for the user in the list of users and in case it is not found, throw an exception
	 * @param documentNumber this parameter is a String that represents the number of the user's document to look for
	 * @param documentType  this parameter us a String that represents the type of document that have the user to look for,
	 * this parameter only can have the following options: citizenship card, civil registration,passport,foreign identity card
	 * @return this method return a String that represent the information of the User to look for, 
	 * the information has been save in a method toString() that exist in the User class.
	 * @throws UserNoExistException This exception will be thrown when the user to search does not succeed
	 */
	public String searchUser(String documentNumber,String documentType) throws UserNoExistException {
		String result="";
		for(int i = 0;i<users.size();i++) {
			if(users.get(i).getDocumentType().equals(documentType) && users.get(i).getDocumentNumber().equals(documentNumber)) {
				result = users.get(i).toString();
				break;
			}
		}
		if(result == "") {
			throw new UserNoExistException();
		}
		return result;
	}
	/**
	 * this method allow assign a instance of Shift to a user through their identity document number
	 * (the turn is formed by a letter of the alphabet and a number from 0 to 99, 
	 * which go from lowest to highest always and once it goes from 99 it returns to 00 and the letter increases)
	 * taking into account if the user does not have any turn over the current shift.
	 *    
	 * @param documentNumber this parameter is a String that represents the number of the user's document to assign shift
	 * @param documentType  this parameter us a String that represents the type of document that have the user to  assign shift,
	 * this parameter only can have the following options: citizenship card, civil registration,passport,foreign identity card
	 * 
	 * @throws UserAlreadyHasShiftException this exception will be thrown when the user to assign a shift already have a shift assigned over the current shift
	 */
	public void assignShift(String documentNumber,String documentType) throws UserAlreadyHasShiftException {
		
		for(int j =0;j<userShift.size();j++) {
			int letterUser = (int)userShift.get(j).getShift().getLetter();
			int letterShift = (int)shift.getLetter();
			if(userShift.get(j).getDocumentNumber().equals(documentNumber) && letterShift<= letterUser && userShift.get(j).getDocumentType().equals(documentType)) {
				if(letterShift== letterUser) {
					if(shift.getNumber()<userShift.get(j).getShift().getNumber()) {		
						throw new UserAlreadyHasShiftException();
					}
				}else{
					throw new UserAlreadyHasShiftException();
				}
							
			}
		}
		for(int i = 0;i<users.size();i++) {
			if(users.get(i).getDocumentNumber().equals(documentNumber)&&users.get(i).getDocumentType().equals(documentType)) {
				User user = users.get(i);
				user.setShift(generateNextShift());
				userShift.add(user);
				
				break;
			}
		}	
	}
	//This method is to generate the next Shift in the list
	/**
	 * This method allows to generate the next turn corresponding to the list of assigned shifts (UserShift), 
	 * the method will take the last assigned turn and increase it by one, to return the result
	 * @return this method returns an instance of the Shift class, this represent to the next of the list
	 */
	public Shift generateNextShift() {
		Shift shift = new Shift('A',0,"A00",false,true);
		if(userShift.size()>0) {
			char letter =userShift.get(userShift.size()-1).getShift().getLetter();
			int number = userShift.get(userShift.size()-1).getShift().getNumber();
			String shift1= userShift.get(userShift.size()-1).getShift().getShift();
			shift = new Shift(letter,number,shift1,false,true);
			shift.setNumber(shift.getNumber()+1);
			if(shift.getNumber()>99) {
				if(shift.getLetter()=='Z') {
					shift.setLetter('A');
				}else shift.setLetter((char) (shift.getLetter()+1));		
				shift.setNumber(0);
			}
			if(shift.getNumber()<10) {
				shift.setShift((char)(shift.getLetter())+ "0"+Integer.toString(shift.getNumber()).toUpperCase());
				
			}else shift.setShift((char)(shift.getLetter())+Integer.toString(shift.getNumber()).toUpperCase()); 
			
			return shift;
		}else {
			
			return shift;
		}
		
	}
	/**
	 * This method allow to create a new User and to add in the list of Users, 
	 * only can be add if the id and type of the user does'nt exist otherwise it will throw an exception
	 * @param name this parameter is a String that represent the name of the User
	 * @param lastName this parameter is a String that represent the last name of the User
	 * @param documentType this parameter is a String that represent the document of the User,
	 * this parameter only can be: citizenship card, civil registration,passport,foreign identity card
	 * @param documentNumber this parameter is a String that represent the document Number of the User
	 * @param locate this parameter is a String that represent the locate of the User, 
	 * this parameter does'nt obligatory so if this parameter is empty will be "unknown"
	 * @param numberPhone this parameter is a String that represent the number phone of the User, 
	 * this parameter does'nt obligatory so if this parameter is empty will be "unknown"
	 * @throws IdUserExistException this exception will be throw if already exist a user whit the same id and type id in the users list
	 * @throws ValueIsEmptyException this exception will be throw if any of the following parameters is empty:
	 * name, lastName, documentType and documentNumber.
	 */
	public void registerUser(String name,String lastName,String documentType,String documentNumber,String locate,String numberPhone) throws IdUserExistException,ValueIsEmptyException {
		for(int i =0;i<users.size();i++) {
			if(users.get(i).getDocumentNumber().equals(documentNumber) && (users.get(i).getDocumentType().equals(documentType))){
				throw new IdUserExistException();
			}
		}
		if(name.isEmpty()||lastName.isEmpty()||documentType.isEmpty()||documentNumber.isEmpty()) {
			throw new ValueIsEmptyException();
		}
		if(locate.isEmpty()) {
			locate = User.UNKNOWN;
		}
		if(numberPhone.isEmpty()) {
			numberPhone = User.UNKNOWN;
		}
		users.add(new User(name,lastName,documentType,documentNumber,locate,numberPhone,new Shift('A',0,"---",false,true)));
	}
	/**
	 * This method allow to advance a shift of the current shift, only can advance if the next shift already has been assigned
	 * 
	 * @param attended this parameter is a boolean and represent if the user was attended or was absent.
	 * @throws NoMoreShiftException  this exception will be throw when don't exist more shift to continue
	 */
	public void advanceShift(boolean attended) throws NoMoreShiftException {	
		if(userShift.size()>0) {
			int letterUser = (int)userShift.get(userShift.size()-1).getShift().getLetter();
			int letterShift = (int)shift.getLetter();
			if(userShift.get(userShift.size()-1).getShift().isActive()==false) {
				throw new NoMoreShiftException();
			}
			if(letterShift>= letterUser && userShift.get(userShift.size()-1).getShift().isActive()==false) {
				if(letterShift== letterUser) {
					if(shift.getNumber()==userShift.get(userShift.size()-1).getShift().getNumber()) {
						throw new NoMoreShiftException();
					}
				}else {
					throw new NoMoreShiftException();
				}
				
			}
			for(int i =0;i<userShift.size();i++) {
				letterUser = (int)userShift.get(i).getShift().getLetter();
				if(shift.getNumber()==userShift.get(i).getShift().getNumber()&&letterUser == letterShift && userShift.get(i).getShift().isActive()==true) {
					userShift.get(i).getShift().setAttended(attended);
					userShift.get(i).getShift().setActive(false);
				}
			}
			
		}else {
			throw new NoMoreShiftException();
		}	
		shift.setNumber(shift.getNumber()+1);
		if(shift.getNumber()>99) {
			shift.setNumber(0);
			shift.setLetter((char) (shift.getLetter()+1));
		}
		if(shift.getNumber()<10) {
			shift.setShift((char)(shift.getLetter())+ "0"+Integer.toString(shift.getNumber()).toUpperCase()); 
		}
	}
	public String getShift() {
		return shift.getShift();
	}
	///Methods for JUnitTest
	public ArrayList<User> getUserList(){
		return users;
	}
	public void setShift(char letter,int number,String shift) {
		this.shift=(new Shift(letter,number,shift,false,true)); 
	}
	
	
	
}

