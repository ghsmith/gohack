package ghsmith.gohack;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.NewCookie;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.RequestEntityProcessing;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;

/**
 *
 * @author ghsmith
 */
public class GetTranscriptByGene {
    
    public static void main(String[] args) {
        
        NewCookie jSessionId = null;
        String jsonTranscript = null;
        
        // Get session cookie for an interactive GO session.
        {
            ClientConfig cc = new ClientConfig().connectorProvider(new ApacheConnectorProvider());    
            cc.property(ClientProperties.REQUEST_ENTITY_PROCESSING, RequestEntityProcessing.BUFFERED);
            HttpAuthenticationFeature feature = HttpAuthenticationFeature.basic(args[0], args[1]);
            Client client = ClientBuilder.newClient(cc);
            client.register(feature);
            jSessionId = client.target("https://patheuhmollabserv2.eushc.org:12443/clinical-app/workbench/permissions")
                .request()
                .get()
                .getCookies()
                .get("JSESSIONID");
        }
        
        // Request a transcript using the session cookie from above.
        {
            ClientConfig cc = new ClientConfig().connectorProvider(new ApacheConnectorProvider());    
            cc.property(ClientProperties.REQUEST_ENTITY_PROCESSING, RequestEntityProcessing.BUFFERED);
            Client client = ClientBuilder.newClient(cc);
            jsonTranscript = client.target("https://patheuhmollabserv2.eushc.org:12443/clinical-app/transcripts/information/" + args[2])
                .request()
                .cookie(jSessionId)
                .get(String.class);
        }
        
        System.out.println(jsonTranscript);
        
    }
    
}
