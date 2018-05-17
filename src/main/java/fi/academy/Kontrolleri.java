package fi.academy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import sun.rmi.runtime.Log;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;


@RestController
public class Kontrolleri {

    @Autowired
    OppilasRepository or;
    @Autowired
    Koulurepository kr;


    private static String template = "Hello, %s";
    private AtomicLong laskin = new AtomicLong();

    @RequestMapping("/greeting")
    public Greeting tervehdys(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(laskin.incrementAndGet(), String.format(template, name));
    }

    @RequestMapping(value = "/student", method = RequestMethod.GET)
    public Oppilas tulostaOppilas() {
        Oppilas oppilas = new Oppilas("Maija Mehiläinen", 15);
        return oppilas;
    }

    @RequestMapping(value = "/tulostadb", method = RequestMethod.GET)
    public Iterable<Oppilas> tulostaKaikki() {
        Iterable<Oppilas> oppilaat = or.findAll();
        return oppilaat;
    }

    @RequestMapping(value = "/students", method = RequestMethod.GET)
    public List<Oppilas> tulostaOppilaat() {
        List<Oppilas> oppilaat = new ArrayList<>();
        Oppilas oppilas = new Oppilas("Maija", 15);
        Oppilas oppilas1 = new Oppilas("Mikko Mallikas", 20);
        Oppilas oppilas2 = new Oppilas("Mauri Myyrä", 3);
        oppilaat.add(oppilas);
        oppilaat.add(oppilas1);
        oppilaat.add(oppilas2);
        return oppilaat;
    }
    //luontiJoukolla - cascade täytyy olla persist

    @RequestMapping(value = "/valitut", method = RequestMethod.GET)
    public Iterable<Oppilas> tulostaKaikkiValitut(@RequestParam(name = "q", required = false) String hakusana) {  //osoiteriville kirjoitetaan valitut?q=m tai joku muu hakusana
        if (hakusana == null)
            return or.findAll();
        return or.findByNimiContains(hakusana);
    }

    /*    @GetMapping("/yksiOppilasId/{id}")                        Tämä toimii jos repositoryn findById ei ole kommenteissa
        public Iterable oppilas(@PathVariable("id") int id) {
            return or.findById(id);*/
//    }
    @GetMapping("/yksiOppilas/{id}")
    public Oppilas palautaYksi(@PathVariable("id") int id) {
        return or.findById(id).get();
    }

    @GetMapping("/yksiOppilasVain/{id}")
    public Oppilas palautaYksiVaihtoehto(@PathVariable("id") int id) {
        Optional<Oppilas> optOppilas = or.findById(id);
        if (optOppilas.isPresent())     //tässä virhekäsittely
            return optOppilas.get();
        return null;
    }

    @PostMapping("/lisaaOppilas")
    public ResponseEntity<?> luoUusiOppilas(@RequestBody Oppilas oppilas) {     //RB koska muuten palautuisi oletusarvoisesti formidatana
        Oppilas talletettu = or.save(oppilas);      //oppilas viittaa yllä luotuun oppilas olioon, joka parametrina
        String osoite = "http://localhost:8080/lisaaOppilas/" + talletettu.getId();
        //  return ResponseEntity.created("http://localhost:8080/lisaaOppilas/"+talletettu.getId()).build();
        return ResponseEntity.created(URI.create(osoite)).build();

    }

    @PostMapping("/lisaaOppilas1")      //Tässä sama kuin ylempänä, mutta tarkistaa löytyykö syötettyä koulua
    public ResponseEntity<?> luoUusiOppilas1(@RequestBody Oppilas oppilas) {     //RB koska muuten palautuisi oletusarvoisesti formidatana
        if (oppilas.getKoulu() != null) {
            {
                Optional<Koulu> koulu = kr.findById(oppilas.getKoulu().getId());
                if (koulu.isPresent()) {
                    oppilas.setKoulu(koulu.get());
                } else {
                    return ResponseEntity.ok("Koulua ei löydy");
                }
            }
        }
        Oppilas talletettu = or.save(oppilas);      //oppilas viittaa yllä luotuun oppilas olioon, joka parametrina
        String osoite = "http://localhost:8080/lisaaOppilas/" + talletettu.getId();
        //  return ResponseEntity.created("http://localhost:8080/lisaaOppilas/"+talletettu.getId()).build();
        return ResponseEntity.created(URI.create(osoite)).build();


    }

    @GetMapping("/naytaKoulu/{id}")
    public Koulu palautaKoulu(@PathVariable("id") int id) {
        Optional<Koulu> optKoulu = kr.findById(id);
        if (optKoulu.isPresent())
            return optKoulu.get();
        return null;
    }

    @PostMapping("/lisaaKoulu")
    public ResponseEntity<?> luoUusiKoulu(@RequestBody Koulu koulu) {
        Koulu talletettu = kr.save(koulu);
        String osoite1 = "http://localhost:8080/lisaaKoulu/" + talletettu.getId();
        return ResponseEntity.created(URI.create(osoite1)).build();
    }

    @PutMapping("/muokkaa/{id}")
    public String muokkaaOppilas(@PathVariable Integer id, @RequestBody Oppilas oppilas) {
        oppilas.setId(id);
        Oppilas talletus = or.save(oppilas);
        return ("muokattu");
    }

    @DeleteMapping("/poista/{id}")
    public ResponseEntity<?> poistaOppilas(@PathVariable Integer id, @RequestBody Oppilas oppilas) {  //palauttaa responseEntityn, mutta ei kerrota vielä minkä tyyppinen
/*        Optional<Oppilas> tarkistus = or.findById(id);
        if (!(tarkistus.isPresent())) {
            return ResponseEntity.notFound().build();
        }*/
        oppilas.setId(id);
        or.delete(oppilas);
        // return ResponseEntity.ok("poistettu");
        return ResponseEntity.ok("poistettu");
    }

    @PutMapping("/muokkaa1/{id}")
    public ResponseEntity<?> muokkaaOppilas1(@PathVariable int id, @RequestBody Oppilas oppilas) {
        Optional<Oppilas> tarkistus = or.findById(id);
        if (!(tarkistus.isPresent())) {
            return ResponseEntity.ok("ID:tä ei löydy");
        }
        oppilas.setId(id);
        Oppilas talletus = or.save(oppilas);
        return ResponseEntity.ok("muokattu");
    }

    @Transactional
    @DeleteMapping("/poistaKoulu/{id}")
    public ResponseEntity<?> poistaKoulu(@PathVariable int id) {
        Optional<Koulu> optkoulu = kr.findById(id);
        if (!(optkoulu.isPresent())) {
            return ResponseEntity.notFound().build();
        }
        or.nollaaKaikkiJoillaOnTamaKOulu(optkoulu.get());
        kr.deleteById(id);
        return ResponseEntity.noContent().build();
    }


}
