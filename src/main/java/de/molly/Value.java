package de.molly;

public class Value {
    public Double kraft;
    public Double zeit;
    public Double winkel;
    public Double beschleunigung;

    @Override
    public String toString() {
        return "Value{" +
                "kraft=" + kraft +
                ", zeit=" + zeit +
                ", winkel=" + winkel +
                ", beschleunigung=" + beschleunigung +
                '}';
    }
}
