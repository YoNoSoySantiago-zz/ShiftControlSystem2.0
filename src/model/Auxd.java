/*package model;
import java.io.*;

public class Auxd {
	public static void main(String[]args) throws IOException {
		
		File file = new File("data/DN.txt");
		File fileR = new File("data/DocumentNumber.txt");
		BufferedReader br = new BufferedReader(new FileReader(file));
		PrintWriter pw = new PrintWriter(fileR);
		String str = br.readLine();
		pw.print(str);
		while(str !=null) {
			str = br.readLine();
			if(str!=null) {
				pw.print(";"+str);
			}	
		}
		pw.close();
		br.close();
		
		
	}
}
*/