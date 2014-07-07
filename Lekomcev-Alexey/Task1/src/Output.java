import java.util.Date;

public class Output {
    User user;
    Date date;
    String description;
    Output(User p_user, Date p_date, String p_description){
        user = p_user;
        date = p_date;
        description = p_description;
    }

    Output(User p_user){
        user = p_user;
    }

    public boolean equals(Object otherObject){
        if (this == otherObject) return true;

        if (otherObject == null) return false;

        if (getClass() != otherObject.getClass())
            return false;

        Output other = (Output) otherObject;

        return user.equals(other.user);
    }
}
