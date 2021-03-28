package ca.interac.etransfer.model;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AccessToken  {

    private String accessToken;
    private String tokenType;
    private Integer expiresIn;
    private String scope;

    public AccessToken () {
    }

    @JsonProperty("accessToken")
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @JsonProperty("tokenType")
    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    @JsonProperty("expiresIn")
    public Integer getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = expiresIn;
    }

    @JsonProperty("scope")
    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AccessToken AccessToken = (AccessToken) o;

        return Objects.equals(accessToken, AccessToken.accessToken) &&
               Objects.equals(tokenType, AccessToken.tokenType) &&
               Objects.equals(expiresIn, AccessToken.expiresIn) &&
               Objects.equals(scope, AccessToken.scope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessToken, tokenType, expiresIn, scope);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class AccessToken {\n");
        sb.append("    accessToken: ").append(toIndentedString(accessToken)).append("\n");        sb.append("    tokenType: ").append(toIndentedString(tokenType)).append("\n");        sb.append("    expiresIn: ").append(toIndentedString(expiresIn)).append("\n");        sb.append("    scope: ").append(toIndentedString(scope)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
