package com.restaurant.Restaurant_Backend.dto.response;

import java.time.LocalDateTime;

public class CustomerResponse {

    private Long id;
    private String tableNumber;
    private String name;
    private String phone;
    private Integer loyaltyPoints;
    private String preferredLanguage;
    private LocalDateTime checkedInAt;
    private LocalDateTime checkedOutAt;

    // ── Constructors ──────────────────────────────────────────────────────────

    public CustomerResponse() {}

    // ── Getters & Setters ─────────────────────────────────────────────────────

    public Long getId()                                     { return id; }
    public void setId(Long id)                              { this.id = id; }

    public String getTableNumber()                          { return tableNumber; }
    public void   setTableNumber(String tableNumber)        { this.tableNumber = tableNumber; }

    public String getName()                                 { return name; }
    public void   setName(String name)                      { this.name = name; }

    public String getPhone()                                { return phone; }
    public void   setPhone(String phone)                    { this.phone = phone; }

    public Integer getLoyaltyPoints()                       { return loyaltyPoints; }
    public void    setLoyaltyPoints(Integer loyaltyPoints)  { this.loyaltyPoints = loyaltyPoints; }

    public String getPreferredLanguage()                    { return preferredLanguage; }
    public void   setPreferredLanguage(String v)            { this.preferredLanguage = v; }

    public LocalDateTime getCheckedInAt()                   { return checkedInAt; }
    public void          setCheckedInAt(LocalDateTime v)    { this.checkedInAt = v; }

    public LocalDateTime getCheckedOutAt()                  { return checkedOutAt; }
    public void          setCheckedOutAt(LocalDateTime v)   { this.checkedOutAt = v; }

    // ── Builder ───────────────────────────────────────────────────────────────

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String tableNumber;
        private String name;
        private String phone;
        private Integer loyaltyPoints;
        private String preferredLanguage;
        private LocalDateTime checkedInAt;
        private LocalDateTime checkedOutAt;

        public Builder id(Long v)                        { this.id = v; return this; }
        public Builder tableNumber(String v)             { this.tableNumber = v; return this; }
        public Builder name(String v)                    { this.name = v; return this; }
        public Builder phone(String v)                   { this.phone = v; return this; }
        public Builder loyaltyPoints(Integer v)          { this.loyaltyPoints = v; return this; }
        public Builder preferredLanguage(String v)       { this.preferredLanguage = v; return this; }
        public Builder checkedInAt(LocalDateTime v)      { this.checkedInAt = v; return this; }
        public Builder checkedOutAt(LocalDateTime v)     { this.checkedOutAt = v; return this; }

        public CustomerResponse build() {
            CustomerResponse r = new CustomerResponse();
            r.id = this.id;
            r.tableNumber = this.tableNumber;
            r.name = this.name;
            r.phone = this.phone;
            r.loyaltyPoints = this.loyaltyPoints;
            r.preferredLanguage = this.preferredLanguage;
            r.checkedInAt = this.checkedInAt;
            r.checkedOutAt = this.checkedOutAt;
            return r;
        }
    }
}