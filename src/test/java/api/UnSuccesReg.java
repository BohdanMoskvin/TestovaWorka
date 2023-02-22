package api;

public class UnSuccesReg {
    private String error;

    public UnSuccesReg(String error) {
        this.error = error;
    }

    public UnSuccesReg() {
    }

    public String getError() {
        return error;
    }
}
