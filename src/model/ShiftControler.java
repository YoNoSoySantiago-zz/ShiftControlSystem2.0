package model;
import exceptions.*;

import java.io.*;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
		type = new ArrayList<Type>();
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
	 * @throws ShiftTypeNotExist 
	 * @throws UserHasBeenBanned 
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
	 * the method will take the last assigned turn and increase it by one, to return the result
	 * @return this method returns an instance of the Shift class, this represent to the next of the list
	 */
	public Shift generateNextShift(Type type) {
		Shift shift = new Shift(time.getNowTime(),type,'A',0,"A00",false,true);
		long days = ChronoUnit.DAYS.between(userShift.get(userShift.size()-1).getShift().getCurrent().getShiftTime(), time.getNowTime());
		if(time.getNowTime().compareTo(userShift.get(userShift.size()-1).getShift().getCurrent().getShiftTime())<0) {
			days*=-1;
		}
		if(userShift.size()>0 && days<=0) {
			User userLast = userShift.get(userShift.size()-1);
			char letter =userLast.getShift().getLetter();
			int number = userLast.getShift().getNumber();
			String shift1= userLast.getShift().getShift();
			LocalDateTime timer = time.getNowTime();
			if(userLast.getShift().getCurrent().getShiftTime().compareTo(time.getNowTime())>0) {
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
	public void advanceShift() throws NoMoreShiftException {	
		if(userShift.size()>0) {
			
			if(userShift.get(userShift.size()-1).getShift().isActive()==false) {
				throw new NoMoreShiftException();
			}
			for(int i =0;i<userShift.size();i++) {
				if(userShift.get(0).getShift().getCurrent().getShiftTime().compareTo(time.getNowTime())<0) {
					Random r = new Random();
					boolean attended=r.nextBoolean();
					userShift.get(0).getShift().setActive(false);
					userShift.get(0).getShift().setAttended(attended);
					userShift.get(0).setBan(userShift.get(0).getBan()+1);
					shift.setNumber(userShift.get(0).getShift().getNumber());
					shift.setLetter(userShift.get(0).getShift().getLetter());
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
	public void showSystemTime(){
		long year = time.getNowTime().getYear()+time.getAdelanted()[0];
		long month =time.getNowTime().getMonthValue()+time.getAdelanted()[1];
		long day = time.getNowTime().getDayOfMonth()+time.getAdelanted()[2];
		long hour = time.getNowTime().getHour()+time.getAdelanted()[3];
		long minute = time.getNowTime().getMinute()+time.getAdelanted()[4];
		long second = time.getNowTime().getSecond()+time.getAdelanted()[5];
		System.out.println(year+"/"+month+"/"+day+"/"+hour+"/"+minute+"/"+second);
	}
	@SuppressWarnings("static-access")
	public void changeTime(int year,int month,int day,int hour,int minute,int second) throws TimeDateNoValid {
		LocalDate date = null;
		date.of(year,month,day);
		LocalTime time = null;
		time.of(hour,minute,second);
		LocalDateTime local = null;
		local.of(date,time);
		if(this.time.getNowTime().compareTo(local)<=0) {
			int[] result = new int[]{year,month,day,hour,minute,second};
			this.time.setAdelanted(result);
			try {
				advanceShift();
			} catch (NoMoreShiftException e) {
				//Nothing
			}
		}else {
			throw new TimeDateNoValid();
		}
	
	}
	public void changeTime() {
		int[] result = new int[6];
		time.setAdelanted(result);
		try {
			advanceShift();
		} catch (NoMoreShiftException e) {
			//Nothing
		}
	}
	
	///PRE_ Duration is bigger than 0
	//Req1
	public void addTypeShift(String name,double duration) throws NameShiftTypeAlreadyExist {
		Type type = new Type(name,duration);
		for (int i = 0; i < this.type.size(); i++) {
			if(this.type.get(i).getName().equals(name)) {
				throw new NameShiftTypeAlreadyExist();
			}
		}
		this.type.add(type);
	}
	public void generateReportUserShift(String documentType,String documentNumber,int option) throws IOException, UserNoExistException {
		searchUser(documentNumber,documentType);
		boolean aux = false;
		File file = new File("data/"+documentNumber+".txt");
		PrintWriter pw = new PrintWriter(file);
		BufferedReader br = new BufferedReader(new FileReader(file));
		String result="";
		for (int i = 0; i < userAttended.size(); i++) {
			if(userAttended.get(i).getDocumentNumber().equals(documentNumber)&&userAttended.get(i).getDocumentType().equals(documentType)) {
				aux = true;
				result+=(userAttended.get(i).getShift().getShift()+"\n");
				if(userAttended.get(i).getShift().isActive()==true) {
					result+=("Already has been attended: "+" YES");
				}else {
					result+=("Already has been attended: "+" NO");
				}
				if(userAttended.get(i).getShift().isAttended()==true) {
					result+="\n"+("was into the room?: "+" YES");
				}else {
					result+="\n"+("was into the room?: "+" NO");
				}
			}
		}
		for (int i = 0; i < userShift.size(); i++) {
			if(userShift.get(i).getDocumentNumber().equals(documentNumber)&&userShift.get(i).getDocumentType().equals(documentType)) {
				aux = true;
				result+=(userShift.get(i).getShift().getShift()+" ");
				if(userShift.get(i).getShift().isActive()==true) {
					result+=("Already has been attended: "+" YES");
				}else {
					result+=("Already has been attended: "+" NO");
				}
				if(userShift.get(i).getShift().isAttended()==true) {
					result+="\n"+("is into the room?: "+" YES");
				}else {
					result+="\n"+("is into the room?: "+" NO");
				}
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
	public void generateReportShiftUsers(String code,int option) throws IOException {
		File file = new File("data/ShiftUsers/"+code);
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
		//ORDENAR POR NAME,DOCUMENT NUMBER,SHIFT TYPE
		for (int i = 0; i < array.size(); i++) {
			aux =true;
			result+=(array.get(i).getName()+"/");
			result+=""+("Document: "+array.get(i).getDocumentType()+" "+array.get(i).getDocumentNumber())+"";
			result+="/Type of shift: "+array.get(i).getShift().getType().getName()+"\n";
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
	
	//String documentNumber,String documentType,String type
	public	void generateRamdonShift(int[] cant) {
		if(type.size()>0) {
			if(users.size()>0) {
				for (int i = 0; i < cant.length; i++) {
					Random rnd = new Random();
					Random rndT = new Random();
					LocalDateTime plus = time.getNowTime().plusDays(i);
					Shift shift =new Shift(plus,null,'A',0,"---",false,true);
					for (int j = 0; j < cant[i]&&j<users.size(); j++) {
						int k = rnd.nextInt()*(users.size()-1)+0;
						int t = rndT.nextInt()*type.size()-1;
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
			}
			
		}else System.out.println("Please create first the types of shifts");
		
	}
	@SuppressWarnings("resource")
	public void generateRamdonUsers(int n) throws IOException {
		File file = new File("DocumentNumber.txt");
		String name,lastName,street,phone,document,type = "";
		int d=0;
		BufferedReader br = new BufferedReader(new FileReader(new File("UserName.txt")));
		String[] names = br.readLine().split(";");
		br = new BufferedReader(new FileReader(new File("UserLastName.txt")));
		String[] lastNames = br.readLine().split(";");
		br = new BufferedReader(new FileReader(new File("StreetAdrees.txt")));
		String[] streets = br.readLine().split(";");
		br = new BufferedReader(new FileReader(new File("NumberPhone.txt")));
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
				int t1 = t.nextInt()*3;
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
				d =rD.nextInt()*documents.length-1;
				document = documents[d];
				name = names[rN.nextInt()*(names.length-1)];
				lastName = lastNames[rL.nextInt()*(lastNames.length-1)];
				if(r.nextBoolean()==true) {
					street = streets[rS.nextInt()*(streets.length-1)];
				}else street = User.UNKNOWN;
				if(r.nextBoolean()==true) {
					phone = phones[rP.nextInt()*phones.length-1];
				}else phone = User.UNKNOWN;
				//registerUser(String name,String lastName,String documentType,String documentNumber,String locate,String numberPhone)
				try {
					registerUser(name, lastName, type, document, street, phone);
				} catch (IdUserExistException | ValueIsEmptyException e) {
					documents[d]="";
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
		}
		
		br.close();
		
	}
	public String getShift() {
		return shift.getShift();
	}
	///Methods for JUnitTest
	public ArrayList<User> getUserList(){
		return users;
	}
	public void setShift(LocalDateTime timer,Type type,char letter,int number,String shift) {
		this.shift=(new Shift(timer,type,letter,number,shift,false,true)); 
	}
	
	
	
}

