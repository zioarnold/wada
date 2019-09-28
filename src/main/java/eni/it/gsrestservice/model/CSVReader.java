package eni.it.gsrestservice.model;

public class CSVReader {
    private String content;
    private String uAccess;
    private String uNtName;
    private String uUtente;
    private String uGruppo;
    private String uNote;

    public CSVReader() {

    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getuAccess() {
        return uAccess;
    }

    public void setuAccess(String uAccess) {
        this.uAccess = uAccess;
    }

    public String getuNtName() {
        return uNtName;
    }

    public void setuNtName(String uNtName) {
        this.uNtName = uNtName;
    }

    public String getuUtente() {
        return uUtente;
    }

    public void setuUtente(String uUtente) {
        this.uUtente = uUtente;
    }

    public String getuGruppo() {
        return uGruppo;
    }

    public void setuGruppo(String uGruppo) {
        this.uGruppo = uGruppo;
    }

    public String getuNote() {
        return uNote;
    }

    public void setuNote(String uNote) {
        this.uNote = uNote;
    }
}
