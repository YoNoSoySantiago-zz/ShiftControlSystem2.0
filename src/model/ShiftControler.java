package model;
import exceptions.*;

import java.io.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

@SuppressWarnings("serial")
public class ShiftControler implements Serializable{
	//ATRIBUTES
	private ArrayList<User> users;
	private ArrayList<User> userShift;
	private ArrayList<Type> type;
	private ArrayList<User> userAttended;
	private Shift shift;
	private CurrentTime time;
	
	public ArrayList<User> getUserShift() {
		return userShift;
	}
	//CONTRUCTOR
	public ShiftControler() {
		users = new ArrayList<User>();
		userShift = new ArrayList<User>();
		userAttended = new ArrayList<User>();
		type = new ArrayList<Type>();
		time = new CurrentTime(null);
		shift = new Shift(time.getNowTime(),null,'A',0,"A00",false,true);
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
		//Sequential Search
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
	 * @throws ShiftTypeNotExist  this exception will be thrown when the parameter type, don't exist
	 * @throws UserHasBeenBanned This exception will be thrown when try to assign a shift to an user that is banned
	 */
	public void assignShift(String documentNumber,String documentType,String type) throws UserAlreadyHasShiftException, ShiftTypeNotExist, UserHasBeenBannedException {
		
		for(int j =0;j<userShift.size();j++) {
			int letterUser = (int)userShift.get(j).getShift().getLetter();
			int letterShift = (int)shift.getLetter();
			long days = ChronoUnit.DAYS.between(userShift.get(j).getShift().getCurrent().getShiftTime(), time.getNowTime());
			if(time.getNowTime().compareTo(userShift.get(j).getShift().getCurrent().getShiftTime())<0) {
				days*=-1;
			}
			
			if((userShift.get(j).getDocumentNumber().equals(documentNumber) && letterShift<= letterUser && userShift.get(j).getDocumentType().equals(documentType))&&days<=0	) {
				if(letterShift== letterUser) {
					if(shift.getNumber()<userShift.get(j).getShift().getNumber()) {		
						throw new UserAlreadyHasShiftException();
					}
				}else{
	
					throw new UserAlreadyHasShiftException();
				}
							
			}
			if(userShift.get(j).getBan()>1) {
				if(time.getNowTime().compareTo(userShift.get(j).getShift().getCurrent().getShiftTime())>0) {
					if(days>=2) {
						userShift.get(j).setBan(0);
					}else throw new UserHasBeenBannedException();
				}
			}
		}
		Type shiftType=null;
		for (int i = 0; i < this.type.size(); i++) {
			if(this.type.get(i).getName().equals(type)) {
				shiftType=this.type.get(i);
				break;
			}
		}
		if(shiftType!=null) {
			for(int i = 0;i<users.size();i++) {
				if(users.get(i).getDocumentNumber().equals(documentNumber)&&users.get(i).getDocumentType().equals(documentType)) {
					User user = users.get(i);
					user.setShift(generateNextShift(shiftType));
					userShift.add(user);
					
					break;
				}
			}	
		}else throw new ShiftTypeNotExist();
		
	}
	//This method is to generate the next Shift in the list
	/**
	 * This method allows to generate the next turn corresponding to the list of assigned shifts (UserShift), 
	 * the method will take the last assigned turn and increase it by one, to return the result, also add the type of shift to the user
	 * @param type, this parameter is a refer of the Type class that will be the type of shift to add, this parameter is not null
	 * @return this method returns an instance of the Shift class, this represent to the next of the list
	 */
	public Shift generateNextShift(Type type) {
		Shift shift = new Shift(time.getNowTime(),type,'A',0,"A00",false,true);
		long days=0;
		if(userShift.size()>0) {
			
			days = ChronoUnit.DAYS.between(userShift.get(userShift.size()-1).getShift().getCurrent().getShiftTime(), time.getNowTime());
			if(time.getNowTime().compareTo(userShift.get(userShift.size()-1).getShift().getCurrent().getShiftTime())<0) {
				days*=-1;
			}
		}
		
		
		if(userShift.size()>0 && days<=0) {
			User userLast = userShift.get(userShift.size()-1);
			char letter =userLast.getShift().getLetter();
			int number = userLast.getShift().getNumber();
			String shift1= userLast.getShift().getShift();
			LocalDateTime timer = time.getNowTime();
			if(userLast.getShift().getCurrent().getShiftTime().compareTo(time.getNowTime())>=0) {
				timer = userLast.getShift().getCurrent().getShiftTime().plusSeconds(15);
				long seconds = (long) (type.getDuration()*60);
				timer = timer.plusSeconds(seconds);
			}
			shift = new Shift(timer,type,letter,number,shift1,false,true);
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
		users.add(new User(name,lastName,documentType,documentNumber,locate,numberPhone,new Shift(null,null,'A',0,"---",false,true)));
	}
	/**
	 * This method allow to advance a shift of the current shift, only can advance if the next shift already has been assigned
	 * 
	 * @param attended this parameter is a boolean and represent if the user was attended or was absent.
	 * @throws NoMoreShiftException  this exception will be throw when don't exist more shift to continue
	 */
	/**
	 * this method actualize the current shift and put random value to all users with shift that have a turn before of the time now if was attended or not
	 * @throws NoMoreShiftException this exception will be thrown when no exist more users with shift in the list
	 */
	public void advanceShift() throws NoMoreShiftException {	
		if(userShift.size()>0) {
			
			if(userShift.get(userShift.size()-1).getShift().isActive()==false) {
				throw new NoMoreShiftException();
			}
			for(int i =0;i<userShift.size();i++) {
				if(userShift.get(0).getShift().getCurrent().getShiftTime().compareTo(time.getNowTime())<=0) {
					Random r = new Random();
					boolean attended=r.nextBoolean();
					userShift.get(0).getShift().setActive(false);
					userShift.get(0).getShift().setAttended(attended);
					userShift.get(0).setBan(userShift.get(0).getBan()+1);
					shift.setNumber(userShift.get(0).getShift().getNumber());
					shift.setLetter(userShift.get(0).getShift().getLetter());
					shift.setShift(userShift.get(0).getShift().getShift());;
					userAttended.add(userShift.get(0));
					userShift.remove(0);
				}else {
					break;
				}
			}
			
		}else {
			throw new NoMoreShiftException();
		}	
	}
	/**
	 * this method advance all shift in the list no matter what time and put random value to all users with shift if was attended or not
	 */
	public void advanceAll() {
		while(userShift.size()>0){
			Random r = new Random();
			boolean attended=r.nextBoolean();
			userShift.get(0).getShift().setActive(false);
			userShift.get(0).getShift().setAttended(attended);
			userShift.get(0).setBan(userShift.get(0).getBan()+1);
			shift.setNumber(userShift.get(0).getShift().getNumber());
			shift.setLetter(userShift.get(0).getShift().getLetter());
			shift.setShift(userShift.get(0).getShift().getShift());
			userAttended.add(userShift.get(0));
			userShift.remove(0);
		}
	}
	/**
	 * This method shoe in console the time that corresponding to the time of system with the time that the user can actualize
	 */
	public void showSystemTime(){
		long second = (time.getNowTime().getSecond()+time.getAdelanted()[5]);
		long minute = time.getNowTime().getMinute()+time.getAdelanted()[4];
		long hour = time.getNowTime().getHour()+time.getAdelanted()[3];
		long day = time.getNowTime().getDayOfMonth()+time.getAdelanted()[2];
		long month =time.getNowTime().getMonthValue()+time.getAdelanted()[1];
		long year = time.getNowTime().getYear()+time.getAdelanted()[0];
		year += month/12;
		month = (month%12)+(day/30);
		day = (day%30)+(hour/24);
		hour = (hour%24)+(minute/60);
		minute = (minute%60)+(second/60);
		second = second%60;
		System.out.println(year+"/"+month+"/"+day+"/"+hour+"/"+minute+"/"+second);
	}
	/**
	 * This method is to change the time of the system to a new time but higher than now, this method save the difference of these times
	 * @param year this parameter is a value that represent the new year that the user want to change
	 * @param month this parameter is a value that represent the new month that the user want to change
	 * @param day this parameter is a value that represent the new day that the user want to change
	 * @param hour this parameter is a value that represent the new hour that the user want to change
	 * @param minute this parameter is a value that represent the new minute that the user want to change
	 * @param second this parameter is a value that represent the new second that the user want to change
	 * @throws TimeDateNoValid this exception will be thrown when the user type values lower that the time now
	 */
	public void changeTime(int year,int month,int day,int hour,int minute,int second) throws TimeDateNoValid {
		LocalDate date = LocalDate.of(year,month,day);
		LocalTime time = LocalTime.of(hour,minute,second);
		LocalDateTime local = LocalDateTime.of(date,time);
		LocalDateTime now =this.time.getNowTime();
		if(this.time.getNowTime().compareTo(local)<=0) {
			year -= this.time.getNowTime().getYear();
			month = month>now.getMonthValue()?month-now.getMonthValue():now.getMonthValue()-month;
			day = day>now.getDayOfMonth()?day-now.getDayOfMonth():now.getDayOfMonth()-day;
			hour = hour>now.getHour()?hour-now.getHour():now.getHour()-hour;
			minute = minute>now.getMinute()?minute-now.getMinute():now.getMinute()-minute;
			second = second>now.getSecond()?second-now.getSecond():now.getSecond()-second;
			int[] result = new int[]{year,month,day,hour,minute,second};
			this.time.setAdelanted(result);
			advanceAll();
		}else {
			throw new TimeDateNoValid();
		}
	
	}
	/**
	 * this method return the value of the time to the time of the compete system
	 */
	public void changeTime() {
		int[] result = new int[6];
		time.setAdelanted(result);
		advanceAll();
	}
	
	///PRE_ Duration is bigger than 0
	//Req1
	/**
	 * this method add a type of shift to the list of types of shift
	 * @param name this parameter represent the name of the type of shift
	 * @param duration this parameter represent the duration that have the shift
	 * @throws NameShiftTypeAlreadyExist this exception will be thrown when the name of the type already exist
	 */
	public void addTypeShift(String name,double duration) throws NameShiftTypeAlreadyExist {
		Type type = new Type(name,duration);
		for (int i = 0; i < this.type.size(); i++) {
			if(this.type.get(i).getName().equals(name)) {
				throw new NameShiftTypeAlreadyExist();
			}
		}
		this.type.add(type);
	}
	/**
	 * this method allow create or show in console a report of the all shift that an user has had in the system.
	 * @param documentType this parameter represent the type of document that the user have
	 * @param documentNumber this parameter represent the number of document that the user have
	 * @param option this parameter is a integer that represent if the user want generate a file, show in console or both
	 * @param order this parameter is a integer that represent how the user want order the report
	 * @throws IOException this exception is thrown when the file or folder are damage 
	 * @throws UserNoExistException this exception will be thrown when the user is not exist
	 */
	public void generateReportUserShift(String documentType,String documentNumber,int option,int order) throws IOException, UserNoExistException {
		searchUser(documentNumber,documentType);
		boolean aux = false;
		File file = new File("data/"+documentNumber+".txt");
		PrintWriter pw = new PrintWriter(file);
		BufferedReader br = new BufferedReader(new FileReader(file));
		ArrayList<User>array = new ArrayList<User>();
		
		String result="";
		for (int i = 0; i < userAttended.size(); i++) {
			if(userAttended.get(i).getDocumentNumber().equals(documentNumber)&&userAttended.get(i).getDocumentType().equals(documentType)) {
				array.add(userAttended.get(i));
			}
		}
		for (int i = 0; i < userShift.size(); i++) {
			if(userShift.get(i).getDocumentNumber().equals(documentNumber)&&userShift.get(i).getDocumentType().equals(documentType)) {
				array.add(userShift.get(i));
				
			}
		}
		User[]array1 = new User[array.size()];
		for (int i = 0; i < array1.length; i++) {
			array1[i]=array.get(i);
		}
		//SORT BY CODE
		if(order==1) {
			Arrays.sort(array1);
		}
		for (int i = 0; i < array1.length; i++) {
			aux = true;
			result+=(array1[i].getShift().getShift()+" \n");
			if(array1[i].getShift().isActive()==true) {
				result+=("Already has been attended: "+" YES");
			}else {
				result+=("Already has been attended: "+" NO");
			}
			if(array1[i].getShift().isAttended()==true) {
				result+="\n"+("is into the room?: "+" YES");
			}else {
				result+="\n"+("is into the room?: "+" NO");
			}
		}
		
		
		if(aux ==false) {
			System.out.println("This user has not had a shift yet");
		}else {
			if(option ==-1 || option ==0) {
				System.out.println(result);
			}
			if(option == 1||option ==0) {
				pw.write(result);
			}
		}
		br.close();
		pw.close();
	}
	/**
	 * this method allow create or show in console a report of the all users that has had a code.
	 * @param code this parameter is the code that will be search
	 * @param option this parameter is a integer that represent if the user want generate a file, show in console or both
	 * @param order this parameter is a integer that represent how the user want order the report
	 * @throws IOException IOException this exception is thrown when the file or folder are damage 
	 */
	@SuppressWarnings("unchecked")
	public void generateReportShiftUsers(String code,int option,int order) throws IOException {
		File file = new File("data/"+code+".txt");
		boolean aux = false;
		PrintWriter pw = new PrintWriter(file);
		BufferedReader br = new BufferedReader(new FileReader(file));
		ArrayList<User> array = new ArrayList<User>();
		String result="";
		for (int i = 0; i < userAttended.size(); i++) {
			if(userAttended.get(i).getShift().getShift().equals(code)) {
				array.add(userAttended.get(i));
				
				
			}
		}
		for (int i = 0; i < userShift.size(); i++) {
			if(userShift.get(i).getShift().getShift().equals(code)) {
				array.add(userShift.get(i));
			}
		}
		User[] array1 = new User[array.size()];
		for (int i = 0; i < array1.length; i++) {
			array1[i]=array.get(i);
		}
		//ORDENAR POR NAME,LASTNAME,DOCUMENT NUMBER,SHIFT TYPE
		switch(order) {
		case 1:
			//Sort by name ascendent
			array1 =insertionSortByName(array1);
			break;
		case 2:
			//Sort by name descendant
			Comparator<User> comparatorReverName = new ReverseOrderSortName() {	};
			Arrays.sort(array1,comparatorReverName);
			break;
		case 3:
			//Sort by lastName ascendent
			Arrays.sort(array1, new SortByLastName());
			break;
		case 4:
			//Sort by lastName descendant
			Arrays.sort(array1,Collections.reverseOrder(new SortByLastName()));
			break;
		case 5:
			//Sort by Document Number
			array1 = selectionSortByDocumentNumber(array1);
			break;
		case 6:
			//Sort by ShiftType
			array1 = bubbleSortByShiftType(array1);
			break;
		}
		for (int i = 0; i < array.size(); i++) {
			aux =true;
			result+=(array1[i].getName()+" "+array.get(i).getLastName()+"/");
			result+=""+("Document: "+array1[i].getDocumentType()+" "+array1[i].getDocumentNumber())+"";
			result+="/Type of shift: "+array1[i].getShift().getType().getName()+"\n";
			}
		if(aux ==false) {
			System.out.println("No one peaple has get this shift");
		}else {
			if(option ==-1 || option ==0) {
				System.out.println(result);
			}
			if(option == 1||option ==0) {
				pw.write(result);
			}
		}
		pw.close();
		br.close();
	}
	/*
	 * Sort by burbuja-compareTo String generateReportUserShift por code asc
	 * Insertion generateReportUserShift document
	 * seleccion 
	 */
	public User[] bubbleSortByShiftType(User[] array){
		for (int i = array.length; i > 0; i--) {
			for (int j = 0; j < i-1; j++) {		
				if(array[j].getShift().getType().getName().compareTo(array[j+1].getShift().getType().getName())>0) {
					User temp = array[j];
					array[i]=array[j+1];
					array[j+1]=temp;
				}else if(array[i].getShift().getType().getName().compareTo(array[j+1].getShift().getType().getName())==0) {
					if(array[i].compareTo(array[j+1])>0) {
						User temp = array[j];
						array[i]=array[j+1];
						array[j+1]=temp;
					}
				}
			}
		}
		return array;
	}
	public User[] insertionSortByName(User[]array){
		for (int i = 1; i < array.length; i++) {
			for (int j = i; j > 0 && array[j-1].compareTo(array[j])>0; j--) {
				User temp = array[j];
				array[i]=array[j-1];
				array[j-1]=temp;
			}
		}
		return array;
	}
	public User[] selectionSortByDocumentNumber(User[] array){
		for (int i = 0; i < array.length; i++) {
			int menor = Integer.parseInt(array[i].getDocumentNumber());
			int cual =i;
			for (int j = i+1; j < array.length; j++) {
				int document1 = Integer.parseInt(array[j].getDocumentNumber());
				if(document1<menor	) {
					menor = document1;
					cual =j;
				}
			}
			User temp = array[i];
			array[i]=array[cual];
			array[cual]=temp;
		}
		return array;
	}
	
	//String documentNumber,String documentType,String type
	public	void generateRamdonShift(int[] cant) {
		if(type.size()>0) {
			if(users.size()>0) {
				for (int i = 0; i < cant.length; i++) {
					Random rnd = new Random();
					Random rndT = new Random();
					LocalDateTime plus = time.getNowTime().plusDays(i);
					
					for (int j = 0; j < cant[i]&&j<users.size(); j++) {
						int k = rnd.nextInt((users.size()-1));
						int t = rndT.nextInt(type.size()-1);
						plus.plusSeconds(15);
						plus.plusSeconds((long) (type.get(i).getDuration()*60));
						Shift shift =new Shift(plus,null,'A',0,"A00",false,true);
						if(j==0) {
							shift.setType(type.get(t));
						}else {
							shift = generateNextShift(type.get(t));
						}
						User user = users.get(k);
						user.setShift(shift);
						userShift.add(user);							
					}
				}
			}else System.out.println("Please create first the users");
			
		}else System.out.println("Please create first the types of shifts");
		
	}
	@SuppressWarnings("resource")
	public void generateRamdonUsers(int n) throws IOException {
		File file = new File("data/DocumentNumber.txt");
		String name="",lastName = "",street="",phone="",document="",type = "";
		int d=0;
		File fileN = new File("data/UserName.txt");
		File fileL = new File("data/UserLastName.txt");
		File fileA = new File("data/StreetAdrees.txt");
		File fileNP = new File("data/NumberPhone.txt");
		BufferedReader br = new BufferedReader(new FileReader(fileN));
		String[] names = br.readLine().split(";");
		br = new BufferedReader(new FileReader(fileL));
		String[] lastNames = br.readLine().split(";");
		br = new BufferedReader(new FileReader(fileA));
		String[] streets = br.readLine().split(";");
		br = new BufferedReader(new FileReader(fileNP));
		String[] phones = br.readLine().split(";");
		br = new BufferedReader(new FileReader(file));
		String[] documents = br.readLine().split(";");
		Random r = new Random();
		Random rN = new Random();
		Random rL = new Random();
		Random rS = new Random();
		Random rP = new Random();
		Random rD = new Random();
		Random t = new Random();
		
		if(documents.length>0) {
			for (int i = 0; i < n && i<4000; i++) {
				int t1 = t.nextInt(3);
				switch(t1+1) {
				case 1:
					type = User.CC;
					break;
				case 2:
					type = User.CR;
					break;
				case 3:
					type = User.FIC;
					break;
				case 4:
					type = User.PS;
					break;
				}
				d =rD.nextInt(documents.length-1);
				document = documents[d].trim();
				name = names[rN.nextInt(names.length-1)].trim();
				lastName = lastNames[rL.nextInt(lastNames.length-1)].trim();
				if(r.nextBoolean()==true) {
					street = streets[rS.nextInt(streets.length-1)].trim();
				}else street = User.UNKNOWN;
				if(r.nextBoolean()==true) {
					phone = phones[rP.nextInt(phones.length-1)].trim();
				}else phone = User.UNKNOWN;
				//registerUser(String name,String lastName,String documentType,String documentNumber,String locate,String numberPhone)
				
					try {
						registerUser(name, lastName, type, document, street, phone);
					} catch (IdUserExistException e) {
						documents[d]="";
					} catch (ValueIsEmptyException e) {
						//NOTHING
					}
				
				
			}
			file.delete();
			PrintWriter pw = new PrintWriter(new File("DocumentNumber.txt"));
			boolean first = false;
			for (int i = 0; i < documents.length; i++) {
				if(documents[i]!="") {
					if(first==false) {
						pw.write(documents[i]);
						first=true;
					}else {
						pw.write(";"+documents[i]);
					}
				}
				
			}
			pw.close();
		}else System.out.println("can't generate more users");
		
		br.close();
		
	}
	public String getShift() {
		return shift.getShift();
	}
	//Anonymous Class
	@SuppressWarnings("rawtypes")
	public class ReverseOrderSortName implements Comparator{

		@Override
		public int compare(Object o1, Object o2) {
			User user1 = (User)o1;
			User user2 = (User) o2;
			int result = user1.compareTo(user2);
			return result*-1;
		}
		
	}
}

