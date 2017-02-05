package co.atoth.intellij.plugin.chucknorris.util;

import com.btr.proxy.search.ProxySearch;
import com.intellij.openapi.diagnostic.Logger;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class NetUtil {

    private static final Logger log = Logger.getInstance(NetUtil.class);

    public static Proxy findProxy(String url){

        ProxySearch proxySearch = new ProxySearch();
        proxySearch.addStrategy(ProxySearch.Strategy.OS_DEFAULT);
        proxySearch.addStrategy(ProxySearch.Strategy.JAVA);
        proxySearch.addStrategy(ProxySearch.Strategy.BROWSER);

        ProxySelector proxySelector = proxySearch.getProxySelector();
        if(proxySelector == null){
            return Proxy.NO_PROXY;
        }

        ProxySelector.setDefault(proxySelector);
        URI home = URI.create(url);

        log.debug("ProxySelector: " + proxySelector);
        log.debug("URI: " + home);

        List<Proxy> proxyList = proxySelector.select(home);
        if (proxyList != null && !proxyList.isEmpty()) {
            return proxyList.iterator().next();
        }

        return Proxy.NO_PROXY;
    }

    public static String responseToString(InputStream inputStream, String charsetName){
        try {
            return new BufferedReader(new InputStreamReader(inputStream, charsetName)).lines().parallel().collect(Collectors.joining("\n"));
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

}