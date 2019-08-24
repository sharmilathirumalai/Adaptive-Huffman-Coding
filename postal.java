import java.util.Scanner;

public class postal {

	private static final int GROUPS = 7;
	
	public static void main(String[] args) {
		  int country;
		  int weight;
		  int boundary[] = {30, 50, 100, 200, 300, 400, 500};
		  int index = 0;
		  int low;
		  int high;
		  int combined_value;
		  double rate;
		  Scanner userInput = new Scanner (System.in);

		  // Get the input from the user.

		  System.out.println ("enter destination country: 1=Canada, 2=US");
		  country = userInput.nextInt() - 1;


		  System.out.println ("enter weight in grams");
		  weight = userInput.nextInt();
		  
		  userInput.close();

		  // Find the entry in the boundary array whose range matches the
		  // entered weight using a binary search.

		  low = 0;
		  high = GROUPS-1;
		  index = (low + high) / 2;
		  while (low <= high) {
		    // assert that boundary[low] < weight <= boundary[high] 

		    if (weight < boundary[index]) {
		      high = index;
		    } else {
		      low = index + 1;
		    }
		    index = (low + high) / 2;
		  }

		  // at this point, I should have that boundary[index-1] < weight <= boundary[index] 
		  // Make up a value that encodes both the country and the weight group. 

		  combined_value = country*10 + index;

		  // Identify the stamp cost. 

		  switch (combined_value) {
		    case 00:
		      rate = 0.85;
		      break;
		    case 01:
		      rate = 1.20;
		    case 02:
		      rate = 1.80;
		      break;
		    case 03:
		      rate = 2.95;
		      break;
		    case 04:
		      rate = 4.10;
		      break;
		    case 05:
		      rate = 4.70;
		      break;
		    case 06:
		      rate = 4.05;
		      break;
		    case 10:
		      rate = 1.20;
		      break;
		    case 11:
		      rate = 1.80;
		      break;
		    case 12:
		      rate = 2.95;
		      break;
		    case 13:
		      rate = 5.15;
		      break;
		    case 14:
		    case 15:
		    case 16:
		      rate = 10.30;
		      break;
		    default:
		      rate = -1;
		      break;

		  }
		  System.out.println ("Postal rate of $" + rate);
		}
}
