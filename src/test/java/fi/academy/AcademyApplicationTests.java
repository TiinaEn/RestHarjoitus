package fi.academy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.templateparser.markup.HTMLTemplateParser;

import javax.persistence.Id;
import java.lang.reflect.ParameterizedType;
import java.net.URI;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AcademyApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;
      @Autowired
     OppilasRepository or;
      @Autowired
      Koulurepository kr;


    @Test
    public void palautaYksiVaihtoehto() {
        ResponseEntity<Oppilas> testi1 = restTemplate.getForEntity("/yksiOppilasVain/3", Oppilas.class);
        assertEquals(200, testi1.getStatusCodeValue());
        Oppilas oppilas = testi1.getBody();
        assertThat(oppilas, is(notNullValue()));
        assertThat(oppilas.getNimi(), is(equalTo("Maija Mehiläinen")));
    }

    @Test
    public void luoUusiOppilas1() throws Exception {
        Oppilas o = new Oppilas();
        o.setNimi("Mikko Mallikas");
        o.setIka(15);
        ResponseEntity<Oppilas> testi2 = restTemplate.postForEntity(URI.create("/lisaaOppilas"), o, Oppilas.class);
        assertEquals(201, testi2.getStatusCodeValue());
    }
//       assertThat(testi2.getStatusCode(), is(equalTo(HttpStatus.CREATED)));
/*        String location = testi2.getHeaders().get("Location").get(0);
        String testLocation = URI.create(location).getPath();
        testi2 = restTemplate.getForEntity(testLocation, Oppilas.class);
        assertThat(testi2.getStatusCode(), is(equalTo(HttpStatus.OK)));
        Oppilas haettu = testi2.getBody();
        assertThat(haettu.getNimi(), is(equalTo("Mikko Mallikas")));

    }*/

        @Test
        public void tulostuukoValitutOikein() {
            ResponseEntity<?> testi4 = restTemplate.getForEntity("/valitut/?q=yy",null, or.findByNimiContains("yy"));
            assertEquals(200,testi4.getStatusCodeValue() );


        }


        @Test
        public void listaEipalautaNull() {
            ResponseEntity<List<Oppilas>> oppilasTesti =
                    restTemplate.exchange("/tulostadb", HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<Oppilas>>(){});
            assertEquals(200, oppilasTesti.getStatusCodeValue() );
            List<Oppilas> oppilaat = oppilasTesti.getBody();
            assertThat(oppilaat,is(notNullValue()) );

        }

     /*   @Test
        public void tulostuukoListaOikein() {
            ResponseEntity<List<Oppilas>> oppilasTesti =
                    restTemplate.exchange("/tulostadb", HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<Oppilas>>(){});
            assertEquals(200, oppilasTesti.getStatusCodeValue() );
            List<Oppilas> oppilaat = oppilasTesti.getBody();
            assertThat(oppilaat,(equalTo(or.findAll().toString())) );
        }*/

    @Test
    public void poistuukoOppilas() {
        int id = 28;
        ResponseEntity<Oppilas> testi = restTemplate.exchange("/poista/{id}", HttpMethod.DELETE, null, Oppilas.class, id);
        assertThat(testi.getStatusCode(), equalTo(HttpStatus.NO_CONTENT) );

    }



    @Test
    public void saadaankoOppilasMuokattua() {

    }
}
