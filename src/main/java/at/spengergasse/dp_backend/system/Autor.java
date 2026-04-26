package at.spengergasse.dp_backend.system;

import lombok.Getter;

@Getter
public class Autor
{
    private String vorname;
    private String name;
    private String bescrebung;

    public Autor(String vorname, String name, String bescrebung) {
        this.vorname = vorname;
        this.name = name;
        this.bescrebung = bescrebung;
    }
}
