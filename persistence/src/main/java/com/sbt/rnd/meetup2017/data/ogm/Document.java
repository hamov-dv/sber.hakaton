package com.sbt.rnd.meetup2017.data.ogm;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Entity
public class Document implements Serializable{
    private Long id;
    private Account debetAccount;
    private Account creditAccount;
    private BigDecimal sumDoc;
    private Date dateDoc;
    private String aim;
    private int state=0;
    private Date dateWork;
    private Integer version;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Document(Account debetAccount, Account creditAccount, BigDecimal sumDoc, Date dateDoc, String aim) {
        this.debetAccount = debetAccount;
        this.creditAccount = creditAccount;
        this.sumDoc = sumDoc;
        this.dateDoc = dateDoc;
        this.aim = aim;
    }
    @ManyToOne(targetEntity=Account.class,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Account getDebetAccount() {
        return debetAccount;
    }

    public void setDebetAccount(Account debetAccount) {
        this.debetAccount = debetAccount;
    }

    @ManyToOne(targetEntity=Account.class,fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Account getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(Account creditAccount) {
        this.creditAccount = creditAccount;
    }

    public BigDecimal getSumDoc() {
        return sumDoc;
    }

    public void setSumDoc(BigDecimal sumDoc) {
        this.sumDoc = sumDoc;
    }

    public Date getDateDoc() {
        return dateDoc;
    }

    public void setDateDoc(Date dateDoc) {
        this.dateDoc = dateDoc;
    }

    public String getAim() {
        return aim;
    }

    public void setAim(String aim) {
        this.aim = aim;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Date getDateWork() {
        return dateWork;
    }

    public void setDateWork(Date dateWork) {
        this.dateWork = dateWork;
    }

    @Version
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
