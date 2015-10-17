package mainpackage;
import java.util.Scanner;

public class MainClass {

	public static void main(String [] args) {

		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		String decision = "";
		
		System.out.println("'f' - ��������� ������ �� ����� numbs.txt;"
				+ "\n'c' - ������ ������ � �������\n");
		System.out.print(">> ");
		decision = in.nextLine();
		System.out.println();
		
		switch (decision) {
		case "c":
		case "�":
			System.out.print("\n������� ���������� �����: ");

			int numCounter = in.nextInt();
			
			if (numCounter < 1 || numCounter > 1000000) {
				System.out.print("\n���������� ������ ����� �� 1 �� 1000000");
				System.exit(0);
			}
			
			AdditionalMethods.calculateDivision(numCounter);
			break;
		
		case "f":
			AdditionalMethods.calculateDivision(0);
			break;
		default:
			System.out.println("\n����������� �������. ������������� ���������");
			System.exit(0);
		}

	}
	
}