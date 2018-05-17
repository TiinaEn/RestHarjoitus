package fi.academy;


import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Koulu {
    @Id
    @GeneratedValue
    private Integer id;
    String nimi;
    String kaupunki;
    @JsonIgnore
    @OneToMany(mappedBy = "koulu")    //  tämä "koulu" viittaa Oppilasluokan private Koulu kouluun (jälkimm.kouluun)
    private List<Oppilas> oppilaat;

    public Koulu() {
    }

    public Koulu(String nimi, String kaupunki) {
        this.nimi = nimi;
        this.kaupunki = kaupunki;
    }

    public Koulu(String nimi, String kaupunki, List<Oppilas> oppilaat) {
        this.nimi = nimi;
        this.kaupunki = kaupunki;
        this.oppilaat = oppilaat;
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

    public String getKaupunki() {
        return kaupunki;
    }

    public void setKaupunki(String kaupunki) {
        this.kaupunki = kaupunki;
    }

    public List<Oppilas> getOppilaat() {
        return oppilaat;
    }

    public void setOppilaat(List<Oppilas> oppilaat) {
        this.oppilaat = oppilaat;
    }

    @Override
    public String toString() {
        return "Koulu{" +
                "id=" + id +
                ", nimi='" + nimi + '\'' +
                ", kaupunki='" + kaupunki + '\'' +
                '}';
    }
}
