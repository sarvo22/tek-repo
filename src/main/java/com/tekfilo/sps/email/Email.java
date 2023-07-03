package com.tekfilo.sps.email;

public class Email {
    private String toEmailId;
    private String tokenId;
    private Integer signupId;
    private String url;

    public Email(String toEmailId,
                 String tokenId,
                 Integer signupId,
                 String url) {
        this.toEmailId = toEmailId;
        this.tokenId = tokenId;
        this.signupId = signupId;
        this.url = url;
    }

    public String getToEmailId() {
        return toEmailId;
    }

    public void setToEmailId(String toEmailId) {
        this.toEmailId = toEmailId;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public Integer getSignupId() {
        return signupId;
    }

    public void setSignupId(Integer signupId) {
        this.signupId = signupId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
