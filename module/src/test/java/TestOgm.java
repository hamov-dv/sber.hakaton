import com.sbt.rnd.meetup2017.transport.api.client.ClientApi;
import com.sbt.rnd.meetup2017.data.ogm.Account;
import com.sbt.rnd.meetup2017.data.ogm.Client;
import com.sbt.rnd.meetup2017.data.ogm.Document;
import com.sbt.rnd.meetup2017.data.ogm.dictionary.Currency;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@ContextConfiguration(locations = "classpath:spring-beans.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class TestOgm {
    @Autowired
    private EntityManager em;

    @Autowired
    private ClientApi clientApi;

    @Test
    public void testAccountPersistAndRead() throws Exception {
        Client client = new Client("Иванов", "1234567890");
        Account account = new Account(client, "123123123", "Счет 123123123");
        assertThat(account.getId(), is(nullValue(Long.class)));
        Collection<Document> documents=new ArrayList<>();
        //documents.add()
        account.setDocs(documents);
        account.setCurrency(new Currency("RUB", 810,"Российский рубль"));
        em.getTransaction().begin();
        assertThat(account.getId(), is(nullValue(Long.class)));

        em.persist(account);
        assertThat(account.getId(), is(notNullValue(Long.class)));
        assertThat(account.getClient(), is(notNullValue(Client.class)));
        assertThat(account.getCurrency(), is(notNullValue(Currency.class)));

        Long id = account.getId();
        em.getTransaction().commit();
        assertThat(account.getId(), is(id));

        Account accountRead = em.find(Account.class, id);
        assertThat(accountRead, is(notNullValue(Account.class)));
        assertThat(accountRead.getId(), is(id));
    }

    /*@Test
    public void testAddressPersistAndRead() throws Exception {
        Address address = new Address("ул. Ленина", 344000);
        assertThat(address.getId(), is(nullValue(Long.class)));

        em.getTransaction().begin();
        assertThat(address.getId(), is(nullValue(Long.class)));

        em.persist(address);
        assertThat(address.getId(), is(notNullValue(Long.class)));

        Long id = address.getId();
        em.getTransaction().commit();
        assertThat(address.getId(), is(id));

        Address addressRead = em.findById(Address.class, id);
        assertThat(addressRead, is(notNullValue(Address.class)));
        assertThat(addressRead.getId(), is(id));
    }*/

    @Test
    public void testClientPersistAndRead() throws Exception {
        //Client client = new Client("Пупкин", "09999991110");
        Client client = clientApi.create("Пупкин", "09999991110",null);
        //assertThat(client.getId(), is(nullValue(Long.class)));

        //em.getTransaction().begin();
        //assertThat(client.getId(), is(nullValue(Long.class)));

        //em.persist(client);
        assertThat(client.getId(), is(notNullValue(Long.class)));

        Long id = client.getId();
        //em.getTransaction().commit();
        assertThat(client.getId(), is(id));

        Client clientRead = em.find(Client.class, id);
        assertThat(clientRead, is(notNullValue(Client.class)));
        assertThat(clientRead.getId(), is(id));
    }

    @Test
    public void testDocumentPersistAndRead() throws Exception {
        Client clientDt = new Client("Иванов", "1234567890");
        Client clientCt = new Client("Петров", "6789054312");
        Account accountDt = new Account(clientDt, "123123123", "Счет 123123123");
        Account accountCt = new Account(clientCt, "321321", "Счет 321321");
        Document document = new Document(accountDt, accountCt, BigDecimal.valueOf(10000), new Date(), "aim");
        assertThat(document.getId(), is(nullValue(Long.class)));

        em.getTransaction().begin();
        assertThat(document.getId(), is(nullValue(Long.class)));

        em.persist(document);
        assertThat(document.getId(), is(notNullValue(Long.class)));
        assertThat(document.getCreditAccount(), is(notNullValue(Account.class)));
        assertThat(document.getDebetAccount(), is(notNullValue(Account.class)));

        Long id = document.getId();
        em.getTransaction().commit();
        assertThat(document.getId(), is(id));

        Document documentRead = em.find(Document.class, id);
        assertThat(documentRead, is(notNullValue(Document.class)));
        assertThat(documentRead.getId(), is(id));
    }
}
