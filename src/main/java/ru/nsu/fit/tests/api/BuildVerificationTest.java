package ru.nsu.fit.tests.api;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.testng.annotations.BeforeClass;
import ru.nsu.fit.data.Customer;
import ru.nsu.fit.data.Plan;
import ru.nsu.fit.services.log.Logger;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author Timur Zolotuhin (tzolotuhin@gmail.com)
 */
public class BuildVerificationTest {

    protected String makeJsonRequest(String path, String username, String password, String json){
        ClientConfig clientConfig = new ClientConfig();
        HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(username, password);
        clientConfig.register( feature );
        clientConfig.register(JacksonFeature.class);
        Client client = ClientBuilder.newClient( clientConfig );

        WebTarget webTarget = client.target("http://localhost:8080/endpoint/rest").path(path);
        Invocation.Builder invocationBuilder = webTarget.request(MediaType.APPLICATION_JSON);
        Response response = null;
        Logger.info("Пытаемся сделать POST запрос по адресу: " + path);
        Logger.info("Авторизация - Username: "+username+", Password: "+password);
        if( json != null ) {
            if( json.length() != 0) {
                Logger.info("Тело POST запроса: " + json);
            }
        }
        response = invocationBuilder.post(Entity.entity(json, MediaType.APPLICATION_JSON));
        String responseText = null;
        if (response != null) {
            responseText = response.readEntity(String.class);
            Logger.info("Ответ от сервера: " + responseText);
        } else {
            Logger.info("Ответ от сервера пустой\n");
        }
        return responseText; //there's strange error "java.lang.IllegalStateException: Entity input stream has already been closed."
        //when I return Response and try to read it in other methods
    }

    protected Customer customerBeforeCreateMethod;

    protected Plan planBeforeCreateMethod;

    @BeforeClass(description = "")
    public void beforeTests() {
        planBeforeCreateMethod = new Plan()
                .setName("Greatest Things")
                .setFee(200)
                .setDetails("Greatest Things details");
        customerBeforeCreateMethod = new Customer()
                .setFirstName("Johnds")
                .setLastName("Weak")
                .setLogin("helloworld123@login.com")
                .setPass("password123")
                .setBalance(0);
    }



}
