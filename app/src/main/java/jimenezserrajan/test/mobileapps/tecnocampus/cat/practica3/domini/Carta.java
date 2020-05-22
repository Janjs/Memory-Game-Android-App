package jimenezserrajan.test.mobileapps.tecnocampus.cat.practica3.domini;

public class Carta {
    int id;
    int revelat;
    int image;
    int imageBack;
    boolean found;

    public Carta(int id, int revelat, int image, int imageBack, boolean found) {
        this.id = id;
        this.revelat = revelat;
        this.image = image;
        this.imageBack = imageBack;
        this.found = found;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRevelat() {
        return revelat;
    }

    public void setRevelat(int revelat) {
        this.revelat = revelat;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getImageBack() {
        return imageBack;
    }

    public void setImageBack(int imageBack) {
        this.imageBack = imageBack;
    }

    public void setFound(boolean found){
        this.found = found;
    }

    public boolean getFound() {
        return found;
    }

    public void revelar() {
        revelat++;
    }

    @Override
    public String toString() {
        return id + "," +
                revelat + "," +
                image + "," +
                imageBack + "," +
                found;
    }

    public static Carta createCartaFromString(String s) {
        String [] cartaInfo = s.split(",",-1);
        return new Carta(Integer.parseInt(cartaInfo[0]), Integer.parseInt(cartaInfo[1]), Integer.parseInt(cartaInfo[2]),
                Integer.parseInt(cartaInfo[3]), Boolean.parseBoolean(cartaInfo[4]));
    }

    public void flipImagesCarta() {
        int temp = this.getImage();
        this.setImage(this.getImageBack());
        this.setImageBack(temp);
    }
}
