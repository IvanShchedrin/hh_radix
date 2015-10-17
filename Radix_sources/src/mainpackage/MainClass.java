package mainpackage;
import java.util.Scanner;

public class MainClass {

	public static void main(String [] args) {

		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		String decision = "";
		
		System.out.println("'f' - прочитать данные из файла numbs.txt;"
				+ "\n'c' - ввести данные в консоль\n");
		System.out.print(">> ");
		decision = in.nextLine();
		System.out.println();
		
		switch (decision) {
		case "c":
		case "с":
			System.out.print("\nВведите количество чисел: ");

			int numCounter = in.nextInt();
			
			if (numCounter < 1 || numCounter > 1000000) {
				System.out.print("\nНеобходимо ввести число от 1 до 1000000");
				System.exit(0);
			}
			
			AdditionalMethods.calculateDivision(numCounter);
			break;
		
		case "f":
			AdditionalMethods.calculateDivision(0);
			break;
		default:
			System.out.println("\nНеизвестная команда. Перезапустите программу");
			System.exit(0);
		}

	}
	
}