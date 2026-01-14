package loader;

public class Donnees {

    private Imagette[] imagettes;


    public Donnees(Imagette[] imagettes) {
        this.imagettes = imagettes;
    }

    public void setImagettes(Imagette[] imagettes) {
        this.imagettes = imagettes;
    }

    public Imagette[] getImagettes() {
        return imagettes;
    }
}
