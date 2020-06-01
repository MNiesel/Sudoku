package TeaVMPackage;

import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.widgets.ApplicationTemplate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@BindTemplate("templates/client.html")
public class Client extends ApplicationTemplate {

    private int rows = 9;
    private int columns=9;
    private List<Integer> values = new ArrayList<>();
    private String userName = "";
    private String i = "";
    private String j = "";


    public static void main(String[] args) {
        Client client = new Client();
        client.bind("application-content");

    }

    public void createField() {
        values.add(0);
        values.add(1);
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public List<Integer> getValues() {
        return values;
    }


}
