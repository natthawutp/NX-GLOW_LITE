package jp.co.nittsu.gwh.common.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Base entity for all GWH tables.
 * Contains universal audit fields and soft-delete flag
 * matching existing Oracle schema conventions.
 */
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Soft delete flag: 0=Active, 1=Deleted */
    @Column(name = "DEL_FLG")
    private Integer delFlg = 0;

    /** Created date (YYYYMMDD) */
    @Column(name = "CRT_YMD")
    private LocalDate crtYmd;

    /** Created time */
    @Column(name = "CRT_TIM")
    private LocalTime crtTim;

    /** Created by user */
    @Column(name = "CRT_USER")
    private String crtUser;

    /** Created by program */
    @Column(name = "CRT_PGM")
    private String crtPgm;

    /** Created timezone */
    @Column(name = "CRT_TM_ZONE")
    private String crtTmZone;

    /** Updated date (YYYYMMDD) */
    @Column(name = "UPD_YMD")
    private LocalDate updYmd;

    /** Updated time */
    @Column(name = "UPD_TIM")
    private LocalTime updTim;

    /** Updated by user */
    @Column(name = "UPD_USER")
    private String updUser;

    /** Updated by program */
    @Column(name = "UPD_PGM")
    private String updPgm;

    /** Updated timezone */
    @Column(name = "UPD_TM_ZONE")
    private String updTmZone;

    /** Optimistic lock timestamp (used for concurrency control) */
    @Version
    @Column(name = "UPD_YMDHMS")
    private LocalDateTime updYmdhms;

    // --- Lifecycle callbacks ---

    @PrePersist
    public void prePersist() {
        this.delFlg = 0;
        this.crtYmd = LocalDate.now();
        this.crtTim = LocalTime.now();
        this.updYmd = LocalDate.now();
        this.updTim = LocalTime.now();
        this.updYmdhms = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updYmd = LocalDate.now();
        this.updTim = LocalTime.now();
        this.updYmdhms = LocalDateTime.now();
    }

    // --- Getters & Setters ---

    public Integer getDelFlg() { return delFlg; }
    public void setDelFlg(Integer delFlg) { this.delFlg = delFlg; }
    public LocalDate getCrtYmd() { return crtYmd; }
    public void setCrtYmd(LocalDate crtYmd) { this.crtYmd = crtYmd; }
    public LocalTime getCrtTim() { return crtTim; }
    public void setCrtTim(LocalTime crtTim) { this.crtTim = crtTim; }
    public String getCrtUser() { return crtUser; }
    public void setCrtUser(String crtUser) { this.crtUser = crtUser; }
    public String getCrtPgm() { return crtPgm; }
    public void setCrtPgm(String crtPgm) { this.crtPgm = crtPgm; }
    public String getCrtTmZone() { return crtTmZone; }
    public void setCrtTmZone(String crtTmZone) { this.crtTmZone = crtTmZone; }
    public LocalDate getUpdYmd() { return updYmd; }
    public void setUpdYmd(LocalDate updYmd) { this.updYmd = updYmd; }
    public LocalTime getUpdTim() { return updTim; }
    public void setUpdTim(LocalTime updTim) { this.updTim = updTim; }
    public String getUpdUser() { return updUser; }
    public void setUpdUser(String updUser) { this.updUser = updUser; }
    public String getUpdPgm() { return updPgm; }
    public void setUpdPgm(String updPgm) { this.updPgm = updPgm; }
    public String getUpdTmZone() { return updTmZone; }
    public void setUpdTmZone(String updTmZone) { this.updTmZone = updTmZone; }
    public LocalDateTime getUpdYmdhms() { return updYmdhms; }
    public void setUpdYmdhms(LocalDateTime updYmdhms) { this.updYmdhms = updYmdhms; }
}
