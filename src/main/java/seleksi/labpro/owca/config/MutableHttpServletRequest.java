package seleksi.labpro.owca.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.*;

final class MutableHttpServletRequest extends HttpServletRequestWrapper {
    // holds custom header and value mapping
    private final Map<String, String> customHeaders;

    public MutableHttpServletRequest(HttpServletRequest request){
        super(request);
        this.customHeaders = new HashMap<String, String>();
    }

    public void putHeader(String name, String value){
        this.customHeaders.put(name, value);
    }

    private HttpServletRequest getServletRequest() {
        return (HttpServletRequest) getRequest();
    }

    @Override
    public String getHeader(String name) {
        return Optional.ofNullable(customHeaders.get(name))
                .orElseGet(() -> getServletRequest().getHeader(name));
    }


    @Override
    public Enumeration<String> getHeaders(String name) {
        Set<String> set = new HashSet<>();
        Optional.ofNullable(customHeaders.get(name)).ifPresent(h -> set.add(h));
        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaders(name);
        while (e.hasMoreElements()) {
            // add the names of the request headers into the list
            String n = e.nextElement();
            set.add(n);
        }
        Optional.ofNullable(customHeaders.get(name)).ifPresent(h -> set.add(h));
        return Collections.enumeration(set);
    }

    public Enumeration<String> getHeaderNames() {
        // create a set of the custom header names
        Set<String> set = new HashSet<String>(customHeaders.keySet());

        // now add the headers from the wrapped request object
        @SuppressWarnings("unchecked")
        Enumeration<String> e = ((HttpServletRequest) getRequest()).getHeaderNames();
        while (e.hasMoreElements()) {
            // add the names of the request headers into the list
            String n = e.nextElement();
            set.add(n);
        }

        // create an enumeration from the set and return
        return Collections.enumeration(set);
    }

}