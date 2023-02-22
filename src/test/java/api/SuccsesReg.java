package api;

public class SuccsesReg {
    private Integer id;
    private String token;

    public SuccsesReg(Integer id, String token) {
        this.id = id;
        this.token = token;
    }

    public SuccsesReg(){

    }

    public Integer getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}
