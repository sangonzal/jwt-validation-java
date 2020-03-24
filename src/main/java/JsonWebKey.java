import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public class JsonWebKey {

    @JsonAlias("kty")
    private String kty;

    @JsonAlias("use")
    private String use;

    @JsonAlias("kid")
    private String kid;

    @JsonAlias("n")
    private String n;

    @JsonAlias("e")
    private String e;

    @JsonAlias("x5t")
    private String x5t;

    @JsonAlias("issuer")
    private String issuer;

    @JsonAlias("x5c")
    private List<String> x5c;

    public String getKty() {
        return kty;
    }

    public void setKty(String kty) {
        this.kty = kty;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getKid() {
        return kid;
    }

    public void setKid(String kid) {
        this.kid = kid;
    }

    public String getN() {
        return n;
    }

    public void setN(String n) {
        this.n = n;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public String getX5t() {
        return x5t;
    }

    public void setX5t(String x5t) {
        this.x5t = x5t;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public List<String> getX5c() {
        return x5c;
    }

    public void setX5c(List<String> x5c) {
        this.x5c = x5c;
    }
}
