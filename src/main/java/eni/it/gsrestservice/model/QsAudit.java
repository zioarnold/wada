package eni.it.gsrestservice.model;

public class QsAudit {
    private int id;
    private String log;
    private String date;

    public QsAudit() {

    }

    public QsAudit(int id, String log, String date) {
        this.id = id;
        this.log = log;
        this.date = date;
    }

    public QsAudit(QsAudit audit) {
        this.id = audit.id;
        this.log = audit.log;
        this.date = audit.log;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "QsAudit{" +
                "log='" + log + '\'' +
                ", date='" + date + '\'' +
                '}';
    }
}
