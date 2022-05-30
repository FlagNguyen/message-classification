package domain;

import java.util.List;

public class Structure {
    private String prefixNumber;
    private List<String> syntaxes;

    public Structure() {
    }

    public Structure(String prefixNumber, List<String> syntaxes) {
        this.prefixNumber = prefixNumber;
        this.syntaxes = syntaxes;
    }

    public String getPrefixNumber() {
        return prefixNumber;
    }

    public void setPrefixNumber(String prefixNumber) {
        this.prefixNumber = prefixNumber;
    }

    public List<String> getSyntaxes() {
        return syntaxes;
    }

    public void setSyntaxes(List<String> syntaxes) {
        this.syntaxes = syntaxes;
    }

    @Override
    public String toString() {
        return getPrefixNumber() + "=" + syntaxes+"\n";
    }
}
