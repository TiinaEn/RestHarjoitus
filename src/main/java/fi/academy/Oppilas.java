package fi.academy;



import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;


@Entity
public class Oppilas {
    @Id @GeneratedValue
    private Integer id;
    private String nimi;
    private Integer ika;

    @ManyToOne//(cascade = CascadeType.ALL)
    @JoinColumn (name = "koulu")
    private Koulu koulu;


    public Oppilas() {
    }


    public Oppilas(String nimi, Integer ika) {
        this.nimi = nimi;
        this.ika = ika;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public Integer getIka() {
        return ika;
    }

    public void setIka(Integer ika) {
        this.ika = ika;
    }

    public Koulu getKoulu() {
        return koulu;
    }

    public void setKoulu(Koulu koulu) {
        this.koulu = koulu;
    }

    @Override
    public String toString() {
        return "Oppilas{" +
                "id=" + id +
                ", nimi='" + nimi + '\'' +
                ", ika=" + ika +
                ", koulu=" + koulu +
                '}';
    }
}
