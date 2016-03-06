package org.openhab.binding.thermiaonline.network;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CodingErrorAction;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.ArrayUtils;
import org.apache.http.client.CookieStore;
import org.glassfish.jersey.client.ClientConfig;
import org.openhab.binding.thermiaonline.handler.ThermiaOnlineHandler;
import org.openhab.binding.thermiaonline.models.Installation;
import org.openhab.binding.thermiaonline.models.InstallationInfo;
import org.openhab.binding.thermiaonline.models.InstallationResult;
import org.openhab.binding.thermiaonline.models.InstallationsResult;
import org.openhab.binding.thermiaonline.models.RegisterGroup;
import org.openhab.binding.thermiaonline.models.RegistersResult;
import org.openhab.binding.thermiaonline.models.Retailer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NetworkHandler {
    private Logger logger = LoggerFactory.getLogger(ThermiaOnlineHandler.class);
    private CookieStore cookieStore;
    Client client;
    WebTarget thermiaTarget, thermiaAuthTarget, localizationTarget, userTarget, installationsTarget;
    NewCookie userAuthCookie, userLoginCookie;
    String username, password;

    public NetworkHandler() {
        ClientConfig conf = new ClientConfig();
        conf.register(new GsonMessageBodyHandler()).register(new CookieFilter(this));
        client = ClientBuilder.newClient(conf);
        thermiaTarget = client.target("http://www.online.thermia.se");
        thermiaAuthTarget = thermiaTarget.path("/api/authentication");
        localizationTarget = thermiaTarget.path("/api/currentuser/localization");
        userTarget = thermiaTarget.path("/api/currentuser");
        installationsTarget = thermiaTarget.path("/api/installationsInfo").queryParam("filter", "")
                .queryParam("limitValue", "100");

        // System.setProperty("http.proxyHost", "127.0.0.1");
        // System.setProperty("http.proxyPort", "8888");

    }

    public List<InstallationInfo> getInstallations() {
        return call(installationsTarget, InstallationsResult.class).installationsInfo;
    }

    public Installation getInstallation(int installationId) {
        WebTarget target = thermiaTarget.path("/api/installations/" + installationId);
        InstallationResult result = call(target, InstallationResult.class);
        if (result.installations.size() > 0) {
            return result.installations.get(0);
        }
        return null;
    }

    /*
     * public calendarFunctions getCalendarFunctions(int installationId) {
     * WebTarget target = thermiaTarget.path("/api/installations/" + installationId + "/calendar/functions");
     * return call(target, calendarFunctions.class);
     * }
     */

    public Retailer getRetailer(int installationId) {
        WebTarget target = thermiaTarget.path("/api/installations/" + installationId + "/retailer");
        return call(target, Retailer.class);
    }

    public List<RegisterGroup> getRegisters(int installationId) {
        WebTarget target = thermiaTarget.path("/api/installations/" + installationId + "/registers");
        return call(target, RegistersResult.class).registerGroups;
    }

    private <T> T call(WebTarget target, Class<T> returnType) {
        authenticateIfNeeded();
        Builder request = target.request().accept(MediaType.APPLICATION_JSON_TYPE);
        Response response = request.get();
        if (isLoggedOut(response)) {
            logger.warn("User logged out (" + response.getStatus() + "). reauthenticating..");
            authenticate();
            response = request.get();
        }
        logger.debug("call status " + response.getStatus());
        T result = response.readEntity(returnType);
        return result;
    }

    private boolean isLoggedOut(Response response) {
        int status = response.getStatus();
        return status == 401;
    }

    private void authenticateIfNeeded() {
        if (userLoginCookie == null || userAuthCookie == null) {
            logger.info("reauthenticating..");
            userLoginCookie = null;
            userAuthCookie = null;
            authenticate();
        }
    }

    private boolean authenticate() {
        authRequest auth = new authRequest();
        auth.userName = username;
        Builder request = thermiaAuthTarget.request().accept(MediaType.APPLICATION_JSON_TYPE);
        Entity<authRequest> reqEnt = Entity.json(auth);
        Response response = request.post(reqEnt);
        saveCookies(response);
        if (userLoginCookie == null) {
            userLoginCookie = new NewCookie("user-login", username);
        }
        authResponse authResponse = response.readEntity(authResponse.class);
        auth.secret = encrypt(password, authResponse.salt1, authResponse.salt2);
        logger.debug("Hashed password: " + auth.secret);
        reqEnt = Entity.json(auth);
        response = request.post(reqEnt);
        saveCookies(response);
        // String raw = response.readEntity(String.class);
        // logger.info("Auth result: " + raw);
        authResponse = response.readEntity(authResponse.class);
        // logger.info("Auth result: " + authResponse.isAuthenticated);
        return authResponse.isAuthenticated;// authResponse.isAuthenticated;
        /*
         * MultivaluedMap<String, Object> headers = r.getHeaders();
         * logger.info("Login result");
         * for (Iterator<String> i = headers.keySet().iterator(); i.hasNext();) {
         * String h = i.next();
         * Object o = headers.get(h);
         * logger.info("H: " + h + " = " + (o == null ? "NULL" : o.toString()));
         * }
         * logger.info("Body");
         * logger.info(r.readEntity(String.class));
         */
    }

    public boolean connect(String username, String password) {
        this.username = username;
        this.password = password;
        if (userLoginCookie != null && userLoginCookie.getValue() != username) {
            userLoginCookie = null;
        }
        return authenticate();
    }

    private void saveCookies(Response response) {
        Map<String, NewCookie> c = response.getCookies();
        Object[] k = c.keySet().toArray();
        // logger.info("Saving cookies " + k.length);
        /*
         * for (int i = 0; i < k.length; i++) {
         * logger.info("CookieDump: " + k[i] + " = " + c.get(k[i]).getValue());
         * }
         */
        if (c.containsKey("user-auth")) {
            userAuthCookie = c.get("user-auth");
            logger.info("Setting user-auth: " + userAuthCookie.getValue());
        }
        if (c.containsKey("user-login")) {
            userLoginCookie = c.get("user-login");
            logger.info("Setting user-login: " + userLoginCookie.getValue());
            // logger.info("Saving user-login: " + userLoginCookie.toString());
        }
    }

    private String encrypt(String value, String salt1, String salt2) {
        return hash(hash(value, salt1), salt2);
    }

    private String hash(String value, String salt) {
        byte[] saltBytes = hexStringToByteArray(salt.trim());
        byte[] valueBytes = new byte[0];
        try {
            valueBytes = value.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        byte[] buffer = ArrayUtils.addAll(valueBytes, saltBytes);

        byte[] bytes = sha256Enc(buffer);
        String x = byteArrayToHexString(bytes);
        // logger.info("{'Salt': " + salt + ", 'value':" + value + ", 'X': " + x + "}");
        return x;
    }

    protected String utf8Decode(byte[] buffer) {
        Charset charset = Charset.forName("UTF-8");
        CharsetDecoder decoder = charset.newDecoder();
        decoder.onMalformedInput(CodingErrorAction.REPLACE);
        // decoder.replaceWith("]");
        CharBuffer chars;
        try {
            chars = decoder.decode(ByteBuffer.wrap(buffer));
            return chars.toString();
        } catch (CharacterCodingException e) {
            logger.error("Failed to decode to utf-8 string: " + e.getMessage());
        }
        return "";
    }

    private byte[] sha256Enc(byte[] bytes) {// String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(bytes);// (value).getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String byteArrayToHexString(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

    class authRequest {
        public String userName;
        public String secret;
    }

    class authResponse {
        public Boolean isAuthenticated;
        public String salt1;
        public String salt2;
    }

    class CookieFilter implements ClientRequestFilter {
        NetworkHandler networkHandler;

        public CookieFilter(NetworkHandler handler) {
            super();
            networkHandler = handler;
        }

        @Override
        public void filter(ClientRequestContext context) throws IOException {
            String cookie = "";
            NewCookie c = networkHandler.userAuthCookie;
            if (c != null) {
                // logger.debug("Cookie: " + c.getName() + "=" + c.getValue());
                cookie += c.getName() + "=" + c.getValue();
            }
            c = networkHandler.userLoginCookie;
            if (c != null) {
                // logger.debug("Cookie: " + c.getName() + "=" + c.getValue());
                if (cookie.length() > 0) {
                    cookie += "; ";
                }
                cookie += c.getName() + "=" + c.getValue();
            }
            if (cookie.length() > 0) {
                List<Object> o = new ArrayList<Object>();
                o.add(cookie);
                context.getHeaders().put("Cookie", o);
            }
        }
    }
}
