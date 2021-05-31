package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.model.enums.AccountType;
import io.swagger.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.validation.annotation.Validated;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.*;

/**
 * BankAccount
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-25T09:30:53.687Z[GMT]")

@Entity
public class BankAccount   {
  @Id
  @JsonProperty("iban")
  private String iban = null;

  @JsonProperty("userId")
  private Integer userId = null;

  @JsonProperty("status")
  private Status status = null;

  @JsonProperty("accountType")
  private AccountType accountType = null;

  @JsonProperty("balance")
  private Double balance = null;

  @JsonProperty("absoluteLimit")
  private Double absoluteLimit = null;

  public BankAccount iban(String iban) {
    this.iban = iban;
    return this;
  }

  /**
   * The unique identifier for the account
   * @return iban
   **/
  @Schema(example = "NL01INHO0000000001", description = "The unique identifier for the account")
  
  @Pattern(regexp="^[a-z]{2}[0-9]{2}[a-z0-9]{4}[0-9]{7}([a-z0-9]?){0,16}$")
  public String getIban() {
    return iban;
  }

  public void setIban(String iban) {
    this.iban = iban;
  }

  public BankAccount userId(Integer userId) {
    this.userId = userId;
    return this;
  }

  /**
   * Owner of the bank account
   * @return userId
   **/
  @Schema(example = "1", required = true, description = "Owner of the bank account")
      @NotNull

    public Integer getUserId() {
    return userId;
  }

  public void setUserId(Integer userId) {
    this.userId = userId;
  }

  public BankAccount status(Status status) {
    this.status = status;
    return this;
  }

  /**
   * Status of the account
   * @return status
   **/
  @Schema(example = "Open", required = true, description = "Status of the account")
      @NotNull

    public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public BankAccount accountType(AccountType accountType) {
    this.accountType = accountType;
    return this;
  }

  /**
   * Account type
   * @return AccountType
   **/
  @Schema(example = "regular", required = true, description = "Account type")
      @NotNull

    public AccountType getAccountType() {
    return accountType;
  }

  public void setAccountType(AccountType accountType) {
    this.accountType = accountType;
  }

  public BankAccount balance(Double balance) {
    this.balance = balance;
    return this;
  }

  /**
   * The ammount of money on the account
   * @return balance
   **/
  @Schema(example = "500.5", description = "The ammount of money on the account")
  
    public Double getBalance() {
    return balance;
  }

  public void setBalance(Double balance) {
    this.balance = balance;
  }

  public BankAccount absoluteLimit(Double absoluteLimit) {
    this.absoluteLimit = absoluteLimit;
    return this;
  }

  /**
   * The absolute minimum of the account
   * @return absoluteLimit
   **/
  @Schema(example = "-1000", required = true, description = "The absolute minimum of the account")
      @NotNull

    public Double getAbsoluteLimit() {
    return absoluteLimit;
  }

  public void setAbsoluteLimit(Double absoluteLimit) {
    this.absoluteLimit = absoluteLimit;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    BankAccount bankAccount = (BankAccount) o;
    return Objects.equals(this.iban, bankAccount.iban) &&
        Objects.equals(this.userId, bankAccount.userId) &&
        Objects.equals(this.status, bankAccount.status) &&
        Objects.equals(this.accountType, bankAccount.accountType) &&
        Objects.equals(this.balance, bankAccount.balance) &&
        Objects.equals(this.absoluteLimit, bankAccount.absoluteLimit);
  }

  @Override
  public int hashCode() {
    return Objects.hash(iban, userId, status, accountType, balance, absoluteLimit);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class BankAccount {\n");
    
    sb.append("    iban: ").append(toIndentedString(iban)).append("\n");
    sb.append("    userId: ").append(toIndentedString(userId)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    type: ").append(toIndentedString(accountType)).append("\n");
    sb.append("    balance: ").append(toIndentedString(balance)).append("\n");
    sb.append("    absoluteLimit: ").append(toIndentedString(absoluteLimit)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
