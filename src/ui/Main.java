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
		System.out.println("Current shift: "+shiftControler.getShift()+"\n");
		System.out.println("1. add user\n"+"2. Assign shift\n"+"3. Advance shift\n"+"0. Exit\n");
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
					shiftControler.assignShift(documentNumber, documentType,shiftType);
					System.out.println("User's shift assigned\n");
				}else if(optionAux != 0) { 
					System.out.println("this option is not correct so it will become to Cancel automatically");
					System.out.println("Cancelled\n");
				}else System.out.println("Cancelled\n");
				break;
			case 3:
				//throws NoMoreShiftException
				time1 =System.currentTimeMillis();
				shiftControler.advanceShift();
				System.out.println("attended correctly");
				
				
			}
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
			e.printStackTrace();
		}
		
		return continue1;
	}
	
	public void case1() {
		
	}
}


