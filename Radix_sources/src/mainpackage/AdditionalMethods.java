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
	 * Основной метод преобразования частного от а/b в требуемую систему счисления
	 * @param length Количество чисел, которое необходимо ввести с консоли. Если 0 - будет прочитал файл numbs.txt
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
					System.out.println("Система счисления меньше 2 или больше 36");
					continue;
				}

				result = (new BigDecimal(numbers[i][0])).divide(new BigDecimal(numbers[i][1]), 50, 1);
			} catch (NumberFormatException ex) {
				System.out.println("Неправильный формат числа");
				continue;
			}

			if (result.compareTo(MAX_VALUE) == 1) {
				System.out.println("Число слишком большое");
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
	 * Считывает данные с консоли. Будет предложено ввести делимое, делитель и частное size-раз
	 * @param size Количество чисел
	 * @return Возвращает массив, содержащий в каждом элементе 3 строки: делимое, делитель и система счисления
	 */
	public static String[][] getNumbersFromConsole(int size) {
		
		String [][] numbers = new String[size][3];
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);

		for(int i = 0; i < size; i++) {
			System.out.print("\nДелимое: ");
			numbers[i][0] = in.nextLine();
			System.out.print("Делитель: ");
			numbers[i][1] = in.nextLine();
			System.out.print("Система счисления: ");
			numbers[i][2] = in.nextLine();
		}

		System.out.println();
		return numbers;
		
	}
	
	/**
	 * Считывает данные из файла numbs.txt
	 * @return Возвращает массив, содержащий в каждом элементе 3 строки: делимое, делитель и система счисления
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
			System.out.println("Ошибка чтения файла");
			System.exit(0);
			return null;
		}
		
	}
	
	/**
	 * Переводит число в необходимую систему счисления
	 * @param number Число, которое нужно перевести
	 * @param radix Система счисления, в которую нужно перевести. Должно быть в диапазоне от 2 до 36
	 * @return Возвращает число, преобразованное в требуемую систему счисления
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
	 * Переводит дробную часть числа в необходимую систему счисления.
	 * @param num Число, которое нужно перевести. Должно быть меньше 1 (пример: 0.125)
	 * @param radix Система счисления, в которую нужно перевести. Должно быть в диапазоне от 2 до 36
	 * @return Возвращает дробную часть числа, преобразованную в требуемую систему счисления
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
	 * @param number Дробная часть числа без "0."
	 * @return Возвращает дробную часть числа в формате "ххх(период)". Если дробь конечная или периодическое число 
	 * длинее 6-ти знаков, то возвращает первые 10 символов.
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