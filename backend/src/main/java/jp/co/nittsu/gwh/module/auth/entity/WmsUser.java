package jp.co.nittsu.gwh.module.auth.entity;

import jp.co.nittsu.gwh.common.entity.BaseEntity;

import javax.persistence.*;

/**
 * WMS User entity mapped to the user management table.
 */
@Entity
@Table(name = "GWH_TM_USER")
public class WmsUser extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "GWH_SEQ_USER", allocationSize = 1)
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "EMAIL", unique = true, nullable = false, length = 200)
    private String email;

    @Column(name = "PASSWORD_HASH", nullable = false, length = 256)
    private String passwordHash;

    @Column(name = "DISPLAY_NAME", length = 100)
    private String displayName;

    @Column(name = "USER_ROLE", length = 50)
    private String userRole;

    @Column(name = "CPNY_COD", nullable = false, length = 20)
    private String companyCode;

    @Column(name = "LOCALE", length = 10)
    private String locale = "en";

    @Column(name = "IS_ACTIVE")
    private Boolean isActive = true;

    // --- Getters & Setters ---

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public String getUserRole() { return userRole; }
    public void setUserRole(String userRole) { this.userRole = userRole; }
    public String getCompanyCode() { return companyCode; }
    public void setCompanyCode(String companyCode) { this.companyCode = companyCode; }
    public String getLocale() { return locale; }
    public void setLocale(String locale) { this.locale = locale; }
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
}
