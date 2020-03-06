package controlTest;
import model.*;
import exceptions.*;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class JUnitTestControlShift {
	ShiftControler sc = new ShiftControler();

	@Test
	public void addUserTest() throws IdUserExistException, ValueIsEmptyException {
		setUp1();
		assertTrue("Santiago"==sc.getUserList().get(sc.getUserList().size()-1).getName());
		assertTrue("1192806566"==sc.getUserList().get(sc.getUserList().size()-1).getDocumentNumber());
		assertThrows(IdUserExistException.class, () ->setUp1());
		setUp2();
		assertTrue("Cristian"==sc.getUserList().get(sc.getUserList().size()-1).getName());
		assertTrue("1685334868"==sc.getUserList().get(sc.getUserList().size()-1).getDocumentNumber());
		
	}
	@Test
	public void searchUserTest() throws UserNoExistException, IdUserExistException, ValueIsEmptyException {
		assertThrows(UserNoExistException.class, () ->sc.searchUser("1192806566", User.CC));
		assertThrows(UserNoExistException.class, () ->sc.searchUser("45968419698", User.PS));
		setUp1();
		String toSring=("full: "+"Santiago"+" "+"Hurtado Solis"+"\ndocument type: "+User.CC+"\nnumber Phone: "+User.UNKNOWN);
		assertTrue(toSring.equals(sc.searchUser("1192806566", User.CC)));
		
	}
	@Test
	public void assingShiftUserTest() throws IdUserExistException, ValueIsEmptyException, UserAlreadyHasShiftException {
		setUp1();
		setUp2();
		setUp3();
		setUp4();
		setUp5();
		setUp6();
		
		assertTrue(sc.getUserShift().get(0).getShift().getShift().equals("A00"));
		assertTrue(sc.getUserShift().get(1).getShift().getShift().equals("A01"));
		assertThrows(UserAlreadyHasShiftException.class, () -> setUp6());
	}
	//This method found to the case d,e and f in the guide of test in the lab 
	@Test
	public void advanceShiftTest() throws NoMoreShiftException, UserAlreadyHasShiftException, IdUserExistException, ValueIsEmptyException {
		assertThrows(NoMoreShiftException.class, () ->sc.advanceShift(true));
		setUp1();
		setUp2();
		setUp3();
		setUp4();
		setUp5();
		setUp6();
		assertTrue(sc.getUserShift().get(0).getShift().getShift().equals(sc.getShift()));
		sc.advanceShift(true);
		assertTrue(sc.getUserShift().get(0).getShift().isAttended()==(true));
		assertTrue(sc.getUserShift().get(1).getShift().getShift().equals(sc.getShift()));
		setUp7();
		assertTrue(sc.getShift().equals("D99"));
		sc.advanceShift(true);
		assertTrue(sc.getShift().equals("E00"));
		sc.advanceShift(true);
		assertTrue(sc.getUserShift().get(2).getShift().isAttended()==(true));
		assertTrue(sc.getUserShift().get(3).getShift().getShift().equals(sc.getShift()));
		sc.advanceShift(true);
		assertTrue(sc.getUserShift().get(3).getShift().isActive()==(false));
	}
	public void setUp1() throws IdUserExistException, ValueIsEmptyException {
		sc.registerUser("Santiago", "Hurtado Solis", User.CC, "1192806566","","");
	}
	
	void setUp2() throws IdUserExistException, ValueIsEmptyException {
		sc.registerUser("Cristian", "Ortiz Castro", User.PS, "1685334868", "SanAncho", "3216179944");
	}
	
	void setUp3() throws UserAlreadyHasShiftException, IdUserExistException, ValueIsEmptyException {
		sc.registerUser("Sebastian", "Moreno Solis", User.CR, "123456789","","");
	}
	void setUp4() throws UserAlreadyHasShiftException {
		sc.assignShift("1192806566", User.CC);
	}
	
	void setUp5() throws UserAlreadyHasShiftException {
		sc.assignShift("1685334868", User.PS);
	}
	
	void setUp6() throws UserAlreadyHasShiftException {
		sc.assignShift("123456789", User.CR);
	}
	void setUp7() throws IdUserExistException, ValueIsEmptyException, UserAlreadyHasShiftException {
		sc.registerUser("Takashi", "Ken", User.CC, "115236584", "", "3216179944");
		sc.setShift('D', 99, "D99");
		sc.getUserShift().get(2).getShift().setLetter('E');
		sc.getUserShift().get(2).getShift().setNumber(0);
		sc.getUserShift().get(2).getShift().setShift("E00");
		sc.assignShift("115236584", User.CC);
		
	}
	
	

}
