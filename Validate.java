// Requires In class
public class Validate {
	// prompts the user for input using the provided prompt message and continuously loops until the user provides a valid input that matches one of the values in the valid array
	public static String input(String prompt, String[] valid) {
		boolean checks = false;
		String input = "";
		do {
			input = In.getString(prompt);
			for (int i = 0; i < valid.length; i++) {
				if (input.toUpperCase().equalsIgnoreCase(valid[i].toUpperCase())) {
					checks = true;
					break;
				}
			}
			if (!checks) {
				System.out.println("Invalid input. Please try again.");
			}
		} while (!checks); while (!checks);
		return input.toUpperCase();
	}
	// prompts the user for input and then validates that the input is either "y" or "n". If the input is valid, the method returns true. Otherwise, it returns false.
	public static boolean input(String prompt) {
		String[] valid = {"y","n"};
		String output = input(prompt, valid);
		if (output.equalsIgnoreCase("Y")) {
			return true;
		} else {
			return false;
		}
	}
	// prompts the user for input and then validates that the input is an int in the valid range. Than the function return the int
	public static int input(String prompt, int min, int max) {
		int input;
		do {
			input = In.getInt(prompt);
			if (input < min || input > max) {
				System.out.println("Invalid input. Please try again.");
			}
		} while (input < min || input > max); 
		return input;
	}
	// Each valid string in the array valid is acomponied by a int in map
	// Valid = ["Y" , "Yes"]
	// Map   = [ 0  ,  0   ]
	// After prompting the user and confirming that the input is valid than return the Valid[Map[Indx of input]]
	// Meaning: Y or Yes both return Y
	public static String inputAndMap(String prompt, String[] valid, int[] map) {
		return valid[map[locate.indxInArray(input(prompt,valid),valid)]];
	}
}
class locate {
	public static int indxInArray(String query, String[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i].toUpperCase().equalsIgnoreCase(query.toUpperCase())) {
				return i;
			}
		}
		return -1;
	}
}
