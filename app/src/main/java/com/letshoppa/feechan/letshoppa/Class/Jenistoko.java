package com.letshoppa.feechan.letshoppa.Class;

/**
 * Created by Feechan on 9/13/2016.
 */
public class Jenistoko
{
    private int jenistokoid;
    private String namajenis;

    public Jenistoko()
    {
        this(0,"");
    }
    public Jenistoko(int jenistokoid, String namajenis) {
        this.jenistokoid = jenistokoid;
        this.namajenis = namajenis;
    }

    public String getNamajenis() {
        return namajenis;
    }

    public void setNamajenis(String namajenis) {
        this.namajenis = namajenis;
    }

    public int getJenistokoid() {
        return jenistokoid;
    }

    public void setJenistokoid(int jenistokoid) {
        this.jenistokoid = jenistokoid;
    }
}
