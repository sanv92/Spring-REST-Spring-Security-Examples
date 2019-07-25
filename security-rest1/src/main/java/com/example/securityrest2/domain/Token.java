package com.example.securityrest2.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "Token")
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "token_id_generator")
    @SequenceGenerator(
            name = "token_id_generator",
            sequenceName = "token_id_seq",
            allocationSize = 1
    )
    private Long id;

    @Column(name = "value")
    private String value;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "user_id", updatable = false, nullable = false)
    private User user;

    public Token() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Token)) return false;
        Token token = (Token) o;
        return Objects.equals(id, token.id) &&
                Objects.equals(value, token.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "Token{" +
                "id=" + id +
                ", value='" + value + '\'' +
                '}';
    }
}
