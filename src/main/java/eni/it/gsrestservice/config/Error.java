package eni.it.gsrestservice.config;

public enum Error {
    E_500_INTERNAL_SERVER("Internal Server Error, code 500");

    private String errorMsg;

    Error(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }
}
