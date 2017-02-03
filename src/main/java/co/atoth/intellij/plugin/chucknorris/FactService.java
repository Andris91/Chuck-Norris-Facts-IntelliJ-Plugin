package co.atoth.intellij.plugin.chucknorris;

import com.btr.proxy.search.ProxySearch;
import com.intellij.openapi.diagnostic.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.net.URLConnection;
import java.util.List;

public class FactService{

    private static final String API_ADDRESS = "http://api.icndb.com/jokes/random";
    private static final String FAILED_MESSAGE = "Failed to get fact from REST API";

    private final Logger log = Logger.getInstance(FactService.class);

    public Fact getRandomFact(){
        try {
            URI uri = URI.create(API_ADDRESS);
            URLConnection connection = uri.toURL().openConnection(findProxy());

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            StringBuilder responseStrBuilder = new StringBuilder();
            while((line =  bufferedReader.readLine()) != null){
                responseStrBuilder.append(line);
            }
            JSONObject jsonFact = new JSONObject(responseStrBuilder.toString());

            if(jsonFact.has("type")
                    && jsonFact.getString("type").equals("success")
                    && !jsonFact.has("value")) {
                throw new RuntimeException(FAILED_MESSAGE);
            }

            JSONObject value = jsonFact.getJSONObject("value");
            Fact fact = new Fact(value.getLong("id")+ "", value.getString("joke"));
            return fact;

        } catch (IOException | JSONException ex ) {
            log.debug(ex);
            throw new RuntimeException(FAILED_MESSAGE);
        }
    }

    public Proxy findProxy(){

        ProxySearch proxySearch = new ProxySearch();
        proxySearch.addStrategy(ProxySearch.Strategy.OS_DEFAULT);
        proxySearch.addStrategy(ProxySearch.Strategy.JAVA);
        proxySearch.addStrategy(ProxySearch.Strategy.BROWSER);

        ProxySelector proxySelector = proxySearch.getProxySelector();
        if(proxySelector == null){
            return Proxy.NO_PROXY;
        }

        ProxySelector.setDefault(proxySelector);
        URI home = URI.create(API_ADDRESS);

        log.debug("ProxySelector: " + proxySelector);
        log.debug("URI: " + home);

        List<Proxy> proxyList = proxySelector.select(home);
        if (proxyList != null && !proxyList.isEmpty()) {
            return proxyList.iterator().next();
        }

        return Proxy.NO_PROXY;
    }

}