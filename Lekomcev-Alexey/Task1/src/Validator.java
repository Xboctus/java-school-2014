import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

    private Pattern pattern;
    private Matcher matcher;
    private String regExp;

    public Validator(String p_regExp){
        regExp = p_regExp;
        pattern = Pattern.compile(regExp);
    }

    public boolean validate(String p_input){
        matcher = pattern.matcher(p_input);
        return matcher.matches();
    }
}
