package software.project.project.component.mail;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:mail.properties")
public class MailConfig {

    @Value("${mail.host}")
    private String host;

    @Value("${mail.port}")
    private int port;

    @Value("${mail.enable_auth}")
    private boolean authEnabled;

    @Value("${mail.enabled_starttls}")
    private boolean starttlsEnabled;

    @Value("${mail.protocol}")
    private String protocol;

    @Value("${mail.username}")
    private String username;

    @Value("${mail.password}")
    private String password;

    public String getHost(){
        return host;
    }
    public int getPort(){
        return port;
    }
    public boolean getAuthEnabled(){
        return authEnabled;
    }
    public boolean getStarttlsEnabled(){
        return starttlsEnabled;
    }
    public String getProtocol(){
        return protocol;
    }
    public String getUsername(){
        return username;
    }
    public String getPassword(){
        return password;
    }
}
