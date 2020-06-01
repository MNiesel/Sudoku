package TeaVMPackage;

import org.teavm.flavour.templates.BindTemplate;
import org.teavm.flavour.templates.Templates;

import java.util.ArrayList;
import java.util.List;

@BindTemplate("templates/fibonacci.html")
public class Fibonacci {

    public static void main(String[] args) {
        Templates.bind(new Fibonacci(), "application-content");
    }

    private List<Integer> values = new ArrayList<>();

    public Fibonacci() {
        values.add(0);
        values.add(1);
    }

    public List<Integer> getValues() {
        return values;
    }



    public void next() {
        values.add(values.get(values.size() - 2) + values.get(values.size() - 1));
    }
}