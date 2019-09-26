package eni.it.gsrestservice.model;

import javax.persistence.*;

@Entity(name = "q_users")
@Table(name = "Q_USERS", schema = "WADA")
public class QUsers {
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;


    @Id
    @Column(name = "USERID")
    private String userId;

    @Column(name = "NAME")
    private String name;

    protected QUsers() {

    }

    public QUsers(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    @Override
    public String toString() {
        return "QUsers{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
