package domain

/**
 * Structure consist 2 part:
 * - prefix number of Viettel
 * - syntax messages of this prefix number
 */
class Structure {
    private String prefixNumber;
    private List<String> syntaxes;

    Structure(String prefixNumber, List<String> syntaxes) {
        this.prefixNumber = prefixNumber;
        this.syntaxes = syntaxes;
    }

    String getPrefixNumber() {
        return prefixNumber;
    }

    List<String> getSyntaxes() {
        return syntaxes;
    }

    @Override
    String toString() {
        return getPrefixNumber() + "=" + syntaxes + "\n";
    }
}
