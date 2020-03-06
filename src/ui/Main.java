package ui;
import model.*;
import exceptions.*;
import java.util.Scanner;

public class Main {
	private ShiftControler shiftControler = new ShiftControler(); 
	public static void main(String[]args) throws IdUserExistException, ValueIsEmptyException {
		boolean continue1 = true;
		System.out.println("=============================\nWELCOME\n=============================\n");
		Main main = new Main();
		do {
			main.showMenu();
			continue1 = main.start();
		}while(continue1 == true);
		
		
	}
	public void showMenu() {
		System.out.println("Current shift: "+shiftControler.getShift()+"\n");
		System.out.println("1. add user\n"+"2. Assign shift\n"+"3. Advance shift\n"+"0. Exit\n");
	}
	public boolean start(){
		@SuppressWarnings("resource")
		Scanner s = new Scanner(System.in);
		boolean continue1= true,attended = false;
		int optionAux=0;
		int option = Integer.parseInt(s.nextLine().trim());
		String name="",lastName="",documentNumber="",documentType="",locate="",numberPhone="";
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
				if(optionAux ==1) {
					shiftControler.assignShift(documentNumber, documentType);
					System.out.println("User's shift assigned\n");
				}else if(optionAux != 0) { 
					System.out.println("this option is not correct so it will become to Cancel automatically");
					System.out.println("Cancelled\n");
				}else System.out.println("Cancelled\n");
				break;
			case 3:
				//throws NoMoreShiftException
				System.out.println("please type \n1. if the user was attended \n0. if the user was not in the attention room");
				optionAux = Integer.parseInt(s.nextLine().trim());
				if(optionAux == 1 && optionAux!=0) {
					attended = true;
				}else if(optionAux == 0){
					attended = true;
				}else {
					System.out.println("this option is not correct so it will become a not automatically");
					attended = false;
				}
				shiftControler.advanceShift(attended);
				System.out.println("attended correctly");
				
				
			}
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
		}
		
		return continue1;
	}
}


