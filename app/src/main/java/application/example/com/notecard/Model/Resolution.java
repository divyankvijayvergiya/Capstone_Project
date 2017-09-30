package application.example.com.notecard.Model;

/**
 * Created by Dell on 30-09-2017.
 */

public class Resolution {
    private int height;
    private String name;
    private int width;

    public Resolution(String paramString, int paramInt1, int paramInt2)
    {
        this.name = paramString;
        this.width = paramInt2;
        this.height = paramInt1;
    }

    public int getHeight()
    {
        return this.height;
    }

    public String getName()
    {
        return this.name;
    }

    public int getWidth()
    {
        return this.width;
    }
}
