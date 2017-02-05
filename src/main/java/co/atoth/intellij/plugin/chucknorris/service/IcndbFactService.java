package co.atoth.intellij.plugin.chucknorris.service;

import co.atoth.intellij.plugin.chucknorris.settings.PluginSettings;
import co.atoth.intellij.plugin.chucknorris.util.NetUtil;
import com.intellij.openapi.diagnostic.Logger;
import org.apache.http.client.utils.URIBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class IcndbFactService implements FactService {

    private final Logger log = Logger.getInstance(IcndbFactService.class);

    private static final String API_HOST = "api.icndb.com";
    private static final String API_SCHEME = "http";
    private static final String API_BASE_PATH = "/jokes/random";
    private static final String API_ENCODING = "UTF-8";

    @Override
    public List<Fact> getRandomFacts(PluginSettings settings) {

        List<Fact> result = new ArrayList<>();

        //Build URL
        URIBuilder uriBuilder = new URIBuilder();

        uriBuilder.setHost(API_HOST);

        uriBuilder.setScheme(API_SCHEME);

        String path = API_BASE_PATH;
        if (settings.getFactLoadCount() != 1) {
            path += "/" + settings.getFactLoadCount();
        }
        uriBuilder.setPath(path);

        if (!PluginSettings.DEFAULT_FIRST_NAME.equals(settings.getFactFirstName())
                && !PluginSettings.DEFAULT_LAST_NAME.equals(settings.getFactLastName())) {

            uriBuilder.addParameter("firstName", settings.getFactFirstName());
            uriBuilder.addParameter("lastName", settings.getFactLastName());
        }

        URI uri;
        URLConnection connection;
        String response;
        try {
            uri = uriBuilder.build();
            connection = uri.toURL().openConnection(NetUtil.findProxy(uri.toString()));
            response = NetUtil.responseToString(connection.getInputStream(), API_ENCODING);
        } catch (URISyntaxException | IOException e) {
            log.error(e);
            return result;
        }

        //Parse response JSON
        JSONObject jsonObject = new JSONObject(response);
        if (jsonObject.has("type") && jsonObject.getString("type").equals("success") && jsonObject.has("value")) {

            if (settings.getFactLoadCount() > 1) {
                JSONArray factArray = jsonObject.getJSONArray("value");
                for (int i = 0; i < factArray.length(); i++) {
                    JSONObject fact = factArray.getJSONObject(i);
                    result.add(new Fact(fact.getLong("id") + "", fact.getString("joke")));
                }
            } else {
                JSONObject fact = jsonObject.getJSONObject("value");
                result.add(new Fact(fact.getLong("id") + "", fact.getString("joke")));
            }
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        return result;
    }

}