import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class teststring {
	public static void main(String... s) throws Exception {
		Pattern VALID_PATTERN = Pattern.compile("([A-Za-z\\s])+|[0-9]+");
		List<String> chunks = new ArrayList<String>();
		Matcher matcher = VALID_PATTERN.matcher("ab1458hht8 78");
		while (matcher.find()) {
			chunks.add( matcher.group() );
		}
		
		System.out.println(chunks);
	}
}
