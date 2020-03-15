package ui;
import model.*;

import exceptions.*;

import java.io.*;
import java.util.Scanner;

public class Main {
	private ShiftControler shiftControler = new ShiftControler();
	public final static String NAME_DATA="data/data.ynss";
	public static void main(String[]args) throws IdUserExistException, ValueIsEmptyException {
		boolean continue1 = true;
		System.out.println("=============================\nWELCOME\n=============================\n");
		Main main = new Main();
		File file = new File(NAME_DATA);
		if(file.canRead() &&file.exists()) {
			try {
				ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NAME_DATA));
				main.shiftControler = (ShiftControler) ois.readObject();
				ois.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		do {
			main.showMenu();
			continue1 = main.start();
		}while(continue1 == true);
		
		try {
			ObjectOutputStream ops = new ObjectOutputStream(new FileOutputStream(NAME_DATA));
			ops.writeObject(main.shiftControler);
			ops.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}
	public void showMenu() {
		shiftControler.showSystemTime();
		System.out.println("Current shift: "+shiftControler.getShift()+"\n");
		System.out.println("1. add user\n"+"2. add Type of shift\n"+"3. Assign shift\n"+"4. Advance shift\n"+"5. Show date and time\n"+"6. Change date and time\n"+"7. To generate...\n"+"0. Exit\n");
	}
	public boolean start(){
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		boolean continue1= true;
		double time1 =0;
		double time2 =0;
		int optionAux=0;
		int option = Integer.parseInt(s.nextLine().trim());
		String name="",lastName="",documentNumber="",documentType="",locate="",numberPhone="",shiftType="";
		try {
			
			switch(option) {
			case 0:
				System.out.println("========================\n"+"See you later\n"+"========================");
				continue1 = false;
				break;
			case 1:
				//throws IdUserExistException,ValueIsEmptyException
				
				System.out.println("Type the name of the user");
				name=s.nextLine().trim();
				System.out.println("Type the lastName of the user");
				lastName=s.nextLine().trim();
				System.out.println("Type the documentType of the user: "
						+ "\n1. citizenship card \n2. civel registration \n3. passport \n4. foreign identity card");
				optionAux=0;
				do {
					optionAux=Integer.parseInt(s.nextLine().trim());
					switch(optionAux) {
					case 1:
						documentType = User.CC;
						break;
					case 2:
						documentType = User.CR;
						break;
					case 3:
						documentType = User.FIC;
						break;
					case 4:
						documentType = User.PS;
						break;
					default:
						System.out.println("Please type a correctly");
					}
				}while(optionAux<1 ||optionAux>4 );
				System.out.println("Type the documentNumber of the user");
				documentNumber=s.nextLine().trim();
				System.out.println("Want to share your location \n1.Yes \n0.No");
				optionAux = Integer.parseInt(s.nextLine().trim());
				
				if(optionAux ==1) {
					System.out.println("Type the locate of the user");
					locate=s.nextLine().trim();
				}else if(optionAux!=0){
					System.out.println("this option is not correct so it will become a not automatically");
				}
				System.out.println("Want to share your cell/telephone number \n1.Yes \n0.No");
				optionAux = Integer.parseInt(s.nextLine().trim());
				if(optionAux ==1) {
					System.out.println("Type the cell/telephone number of the user");
					numberPhone=s.nextLine().trim();
					
				}else if(optionAux!=0){
					System.out.println("this option is not correct so it will become to not automatically");
					
				}
				time1 =System.currentTimeMillis();
				shiftControler.registerUser(name, lastName, documentType, documentNumber, locate, numberPhone);
				System.out.println("User add correctly\n");
				break;
			case 2:
				System.out.println("Type the name of the type of shift");
				String nameS = s.nextLine();
				System.out.println("Type the duration in minutes of the shift");
				double duration=0;
				do {
					System.out.println("remenber that the duration must be greater than 0");
					duration = Double.parseDouble(s.nextLine());
				}while(duration==0);
				
				time1 =System.currentTimeMillis();
				shiftControler.addTypeShift(nameS,duration);
				break;
			case 3:
				//throws UserAlreadyHasShiftException
				System.out.println("Type the documentType of the user: "
						+ "\n1. citizenship card \n2. civel registration \n3. passport \n4. foreign identity card");
				optionAux=0;
				do {
					optionAux=Integer.parseInt(s.nextLine().trim());
					
					switch(optionAux) {
					case 1:
						documentType = User.CC;
						break;
					case 2:
						documentType = User.CR;
						break;
					case 3:
						documentType = User.FIC;
						break;
					case 4:
						documentType = User.PS;
						break;
					default:
						System.out.println("Please type a correctly");
					}
				}while(optionAux<1 ||optionAux>4);
				
				System.out.println("Type your document number");
				documentNumber = s.nextLine().trim();
				System.out.println(shiftControler.searchUser(documentNumber,documentType));
				System.out.println("Type: \n1. to assign shift\n0. to Cancel");
				optionAux = Integer.parseInt(s.nextLine().trim());
				time1 =System.currentTimeMillis();
				if(optionAux ==1) {
					System.out.println("Please type the name of the type of shift");
					shiftType = s.nextLine();
					shiftControler.assignShift(documentNumber, documentType,shiftType);
					System.out.println("User's shift assigned\n");
				}else if(optionAux != 0) { 
					System.out.println("this option is not correct so it will become to Cancel automatically");
					System.out.println("Cancelled\n");
				}else System.out.println("Cancelled\n");
				break;
			case 4:
				//throws NoMoreShiftException
				time1 =System.currentTimeMillis();
				shiftControler.advanceShift();
				System.out.println("attended correctly"+"\n");
				break;
			case 6:
				//int year,int month,int day,int hour,int minute,int second
				System.out.println("type the 1 if you want change manualy else type 0 to put the System time");
				optionAux = Integer.parseInt(s.nextLine());
				if(optionAux ==1) {
					System.out.println("type the following values each on one line and the same order");
					int year = Integer.parseInt(s.nextLine());
					int month = Integer.parseInt(s.nextLine());
					int day = Integer.parseInt(s.nextLine());
					int hour = Integer.parseInt(s.nextLine());
					int minute = Integer.parseInt(s.nextLine());
					int second = Integer.parseInt(s.nextLine());
					time1 =System.currentTimeMillis();
					shiftControler.changeTime(year, month, day, hour, minute, second);
				}else if(optionAux != 0) { 
					System.out.println("this option is not correct so it will become to 0 automatically");
					time1 =System.currentTimeMillis();
					shiftControler.changeTime();
				}else {
					time1 =System.currentTimeMillis();
					shiftControler.changeTime();
				}
				break;
			case 7:
				System.out.println("1. Generate a report with all the shifts that a user has generated\n"+
									"2. Generate a report with all the users who have been assigned the shift\n"+
									"3. Randomly generate registered users in the System\n"+
									"4. Generar aleatoriamente turnos para los usuarios existentes");
				option =Integer.parseInt(s.nextLine());
				switch(option) {
				case 1:
					System.out.println("Type the documentType of the user: "
							+ "\n1. citizenship card \n2. civel registration \n3. passport \n4. foreign identity card");
					optionAux=0;
					do {
						optionAux=Integer.parseInt(s.nextLine().trim());
						
						switch(optionAux) {
						case 1:
							documentType = User.CC;
							break;
						case 2:
							documentType = User.CR;
							break;
						case 3:
							documentType = User.FIC;
							break;
						case 4:
							documentType = User.PS;
							break;
						default:
							System.out.println("Please type a correctly");
						}
					}while(optionAux<1 ||optionAux>4);
					System.out.println("Type the docuemnt number of the user");
					documentNumber = s.nextLine();
					System.out.println("-1. if you want that the report show in console\n"+"1. if you want that the report save in a file\n"+"0. if you want both to run");
					int option2 = Integer.parseInt(s.nextLine());
					if(option2<-1||option2>1) {
						option2 = 0;
						System.out.println("it will be set to 0 automatically, because the option is not correct");
					}
					System.out.println("1. if you want the repart will be in order\n 0. else");
					int order = Integer.parseInt(s.nextLine());
					if(order !=0 && order !=1) {
						System.out.println("it will be set to 0 automatically, because the option is not correct");
					}
					shiftControler.generateReportUserShift(documentType,documentNumber,option2,order);
					break;
				case 2:
					System.out.println("please type the code");
					String code = s.nextLine();
					option2 = Integer.parseInt(s.nextLine());
					if(option2<-1||option2>1) {
						option2 = 0;
						System.out.println("it will be set to 0 automatically, because the option is not correct");
					}
					System.out.println("1. if you want the repart will be in order\n 0. else");
					order = Integer.parseInt(s.nextLine());
					if(order ==1) {
						System.out.println("0. Sort by time\n"+"1. Sort by name ascendent\n"+"2. Sort by name descendant\n"+"3. Sort by lastName ascendent\n"+"4. Sort by lastName descendant\n"+
											"5. Sort by Document Number\n"+"6. Sort by ShiftType\n");
						order = Integer.parseInt(s.nextLine());
					}else if(order !=0) {
						System.out.println("it will be set to 0 automatically, because the option is not correct");
					}
					shiftControler.generateReportShiftUsers(code,option2,order);
					break;
				case 3:
					System.out.println("Type how many users you want generate");
					int cant = Integer.parseInt(s.nextLine());
					shiftControler.generateRamdonUsers(cant);
					break;
				case 4:
					System.out.println("Type how many shift you want generate");
					int cantS = Integer.parseInt(s.nextLine());
					shiftControler.generateRamdonUsers(cantS);
					break;
				}
				
			}
			//"1. add user\n"+"2. add Type of shift"+"3. Assign shift\n"+"4.Advance shift\n"+"5.Show date and time"+"6.Change date and time"+"7.Generar..."+"0. Exit\n"
			time2 = System.currentTimeMillis();
			System.out.println("The system took: "+(time2-time1)+"ms");
		}catch(IdUserExistException e) {
			System.out.println(e.getMessage());
		}catch(ValueIsEmptyException e) {
			System.out.println(e.getMessage());
		}catch(UserAlreadyHasShiftException e) {
			System.out.println(e.getMessage());
		}catch(NoMoreShiftException e) {
			System.out.println(e.getMessage());
		}catch(UserNoExistException e) {
			System.out.println(e.getMessage());
		} catch (ShiftTypeNotExist e) {
			System.out.println(e.getMessage());
		} catch (UserHasBeenBannedException e) {
			System.out.println(e.getMessage());
		} catch (NameShiftTypeAlreadyExist e) {
			System.out.println(e.getMessage());
		} catch (TimeDateNoValid e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println("Please check the data folder");
		}
		
		return continue1;
	}
}


