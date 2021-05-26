package io.swagger.model;

public class JwtToken_Singleton {
    private static JwtToken_Singleton instance = new JwtToken_Singleton();

    private JwtToken_Singleton(){}

    public static JwtToken_Singleton getInstance(){
        if(instance == null)
            instance = new JwtToken_Singleton();

        return instance;
    }

    private String jwtToken;

    public String getJwtToken() {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
}
