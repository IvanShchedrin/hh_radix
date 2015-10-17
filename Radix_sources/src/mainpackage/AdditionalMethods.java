package mainpackage;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.util.Scanner;

public class AdditionalMethods {

	private static final short ACCURACY = 50;
	private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final BigDecimal BIG_ZERO = new BigDecimal("0");
	private static final BigDecimal BIG_ONE = new BigDecimal("1");
	private static final BigDecimal MAX_VALUE = new BigDecimal("9223372036854775807");

	
	/**
	 * �������� ����� �������������� �������� �� �/b � ��������� ������� ���������
	 * @param length ���������� �����, ������� ���������� ������ � �������. ���� 0 - ����� �������� ���� numbs.txt
	 */
	public static void calculateDivision(int length) {

		String[][] numbers;
		
		if(length == 0) {
			numbers = getNumbersFromFile();
			length = numbers.length;
		} else {
			numbers = getNumbersFromConsole(length);
		}
		
		String intPart, fracPart, radix;
		BigDecimal result;

		for (int i = 0; i < length; i++) {
	         
			radix = numbers[i][2];

			try {
				if(Integer.parseInt(radix) > 36 || Integer.parseInt(radix) < 2) {
					System.out.println("������� ��������� ������ 2 ��� ������ 36");
					continue;
				}

				result = (new BigDecimal(numbers[i][0])).divide(new BigDecimal(numbers[i][1]), 50, 1);
			} catch (NumberFormatException ex) {
				System.out.println("������������ ������ �����");
				continue;
			}

			if (result.compareTo(MAX_VALUE) == 1) {
				System.out.println("����� ������� �������");
				continue;
			} else {
				intPart = convertIntPart10toN(result.longValue(), Integer.valueOf(radix).intValue());
				fracPart = convertFracPart10toN(result.remainder(BigDecimal.ONE), new BigDecimal(radix));
				
				if(fracPart.length() < 50) {
					if(fracPart.length() == 0) {
						System.out.println(intPart);
					} else {
						System.out.println(intPart + "." + fracPart);
					}
				} else {
					System.out.println(intPart + "." + findPeriod(fracPart));
				}
			}
			
		}

	}
	
	/**
	 * ��������� ������ � �������. ����� ���������� ������ �������, �������� � ������� size-���
	 * @param size ���������� �����
	 * @return ���������� ������, ���������� � ������ �������� 3 ������: �������, �������� � ������� ���������
	 */
	public static String[][] getNumbersFromConsole(int size) {
		
		String [][] numbers = new String[size][3];
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);

		for(int i = 0; i < size; i++) {
			System.out.print("\n�������: ");
			numbers[i][0] = in.nextLine();
			System.out.print("��������: ");
			numbers[i][1] = in.nextLine();
			System.out.print("������� ���������: ");
			numbers[i][2] = in.nextLine();
		}

		System.out.println();
		return numbers;
		
	}
	
	/**
	 * ��������� ������ �� ����� numbs.txt
	 * @return ���������� ������, ���������� � ������ �������� 3 ������: �������, �������� � ������� ���������
	 */
	public static String[][] getNumbersFromFile() {
	      
		try {
			File ex = new File("numbs.txt");
			FileInputStream fis = new FileInputStream(ex);
			
			byte[] data = new byte[(int)ex.length()];
			
			fis.read(data);
			fis.close();
			
			String[] numbArray = (new String(data, "UTF-8")).replace("\n", "").replace("\r", " ").split(" ");
			String[][] numbers = new String[numbArray.length / 3][3];

			for (int i = 0; i < numbArray.length; i += 3) {
				numbers[i / 3][0] = numbArray[i];
				numbers[i / 3][1] = numbArray[i + 1];
				numbers[i / 3][2] = numbArray[i + 2];
			}

			return numbers;
			
		} catch (Exception ex) {
			System.out.println("������ ������ �����");
			System.exit(0);
			return null;
		}
		
	}
	
	/**
	 * ��������� ����� � ����������� ������� ���������
	 * @param number �����, ������� ����� ���������
	 * @param radix ������� ���������, � ������� ����� ���������. ������ ���� � ��������� �� 2 �� 36
	 * @return ���������� �����, ��������������� � ��������� ������� ���������
	 */
	public static String convertIntPart10toN(long number, int radix) {
		
		if(number < 1) {
			return "0";
		}
		
		String result = "";
		
		while (number > 0) {
			result += ALPHABET.charAt( (int) (number % radix) );
			number /= radix;
		}
		
		return (new StringBuilder(result)).reverse().toString();
	
	}
	
	/**
	 * ��������� ������� ����� ����� � ����������� ������� ���������.
	 * @param num �����, ������� ����� ���������. ������ ���� ������ 1 (������: 0.125)
	 * @param radix ������� ���������, � ������� ����� ���������. ������ ���� � ��������� �� 2 �� 36
	 * @return ���������� ������� ����� �����, ��������������� � ��������� ������� ���������
	 */
	public static String convertFracPart10toN(BigDecimal num, BigDecimal radix) {
	      
		String result = "";
		int accuracy = ACCURACY;

		while(num.compareTo(BIG_ZERO) == 1 && accuracy > 0) {

			num = num.multiply(radix);
			accuracy --;
			
			if(num.compareTo(BIG_ONE) >= 0) {
				result = result + ALPHABET.charAt(num.intValue());
				num = num.remainder(BigDecimal.ONE);
			} else {
				result = result + "0";
			}
		}

		return result;
		
	}
	
	/**
	 * @param number ������� ����� ����� ��� "0."
	 * @return ���������� ������� ����� ����� � ������� "���(������)". ���� ����� �������� ��� ������������� ����� 
	 * ������ 6-�� ������, �� ���������� ������ 10 ��������.
	 */
	public static String findPeriod(String number) {
	      
		String str = "";
		int counter, q;

		for (int i = 0; i < ACCURACY / 3; i++) {
			
			for (q = 1; q < 7; ++q) {
				
				str = number.substring(i, i + q);
				counter = 1;
	            
				while ( number.substring(i + q * counter, i + q * counter + q).equals(str) && counter < (ACCURACY / 3) / q ) {
					counter ++;
				}

				if (counter >= (ACCURACY / 3) / q) {
					return number.substring(0, i) + "(" + str + ")";
				}
	            
			}
			
		}

		return number.substring(0, 10);
	      
	}
	
}