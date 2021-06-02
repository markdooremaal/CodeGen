package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.model.BankAccount;
import io.swagger.model.enums.Role;
import io.swagger.model.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import org.springframework.validation.annotation.Validated;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * User
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-25T09:30:53.687Z[GMT]")

@Entity
public class User   {
  @Id
  @GeneratedValue
  @JsonProperty("id")
  private Integer id = null;

  @JsonProperty("firstName")
  private String firstName = null;

  @JsonProperty("lastName")
  private String lastName = null;

  @JsonProperty("email")
  private String email = null;

  @JsonProperty("password")
  private String password = null;

  @JsonProperty("role")
  private Role role = null;

  @JsonProperty("status")
  private Status status = null;

  @JsonProperty("dayLimit")
  private Double dayLimit = null;

  private Double currentDayLimit = 0.0;

  @JsonProperty("transactionLimit")
  private Double transactionLimit = null;

  @JsonProperty("bankAccounts")
  @OneToMany(targetEntity=BankAccount.class, fetch= FetchType.EAGER)
  private List<BankAccount> bankAccounts = null;

  public User id(Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Unique user id
   * @return id
   **/
  @Schema(example = "1", description = "Unique user id")
  
    public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public User firstName(String firstName) {
    this.firstName = firstName;
    return this;
  }

  /**
   * Users firstname
   * @return firstName
   **/
  @Schema(example = "John", required = true, description = "Users firstname")
      @NotNull

    public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public User lastName(String lastName) {
    this.lastName = lastName;
    return this;
  }

  /**
   * Users lastname
   * @return lastName
   **/
  @Schema(example = "Doe", required = true, description = "Users lastname")
      @NotNull

    public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public User email(String email) {
    this.email = email;
    return this;
  }

  /**
   * Users email
   * @return email
   **/
  @Schema(example = "johndoe@example.dev", required = true, description = "Users email")
      @NotNull

    public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public User password(String password) {
    this.password = password;
    return this;
  }

  /**
   * Users password
   * @return password
   **/
  @Schema(example = "goedWachtwoord94!", required = true, description = "Users password")
      @NotNull

    public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public User role(Role role) {
    this.role = role;
    return this;
  }

  /**
   * Users role
   * @return role
   **/
  @Schema(example = "customer", description = "Users role")
  
    public Role getRole() {
    return role;
  }

  public void setRole(Role role) {
    this.role = role;
  }

  public User status(Status status) {
    this.status = status;
    return this;
  }

  /**
   * Users status
   * @return status
   **/
  @Schema(example = "active", description = "Users status")

    public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public User dayLimit(Double dayLimit) {
    this.dayLimit = dayLimit;
    return this;
  }

  /**
   * The max spending for the user per day
   * @return dayLimit
   **/
  @Schema(example = "499.9", description = "The max spending for the user per day")

    public Double getDayLimit() {
    return dayLimit;
  }

  public void setDayLimit(Double dayLimit) {
    this.dayLimit = dayLimit;
  }

  public User transactionLimit(Double transactionLimit) {
    this.transactionLimit = transactionLimit;
    return this;
  }

  public Double getCurrentDayLimit() {
    return currentDayLimit;
  }

  public void setCurrentDayLimit(Double currentDayLimit) {
    this.currentDayLimit = currentDayLimit;
  }

  /**
   * The max spending for the user
   * @return transactionLimit
   **/
  @Schema(example = "499.9", description = "The max spending for the user")

    public Double getTransactionLimit() {
    return transactionLimit;
  }

  public void setTransactionLimit(Double transactionLimit) {
    this.transactionLimit = transactionLimit;
  }

  public User bankAccounts(List<BankAccount> bankAccounts) {
    this.bankAccounts = bankAccounts;
    return this;
  }

  public User addBankAccountsItem(BankAccount bankAccountsItem) {
    if (this.bankAccounts == null) {
      this.bankAccounts = new ArrayList<BankAccount>();
    }
    this.bankAccounts.add(bankAccountsItem);
    return this;
  }

  /**
   * The users bank accounts
   * @return bankAccounts
   **/
  @Schema(description = "The users bank accounts")
      @Valid
    public List<BankAccount> getBankAccounts() {
    return bankAccounts;
  }

  public void setBankAccounts(List<BankAccount> bankAccounts) {
    this.bankAccounts = bankAccounts;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return Objects.equals(this.id, user.id) &&
        Objects.equals(this.firstName, user.firstName) &&
        Objects.equals(this.lastName, user.lastName) &&
        Objects.equals(this.email, user.email) &&
        Objects.equals(this.password, user.password) &&
        Objects.equals(this.role, user.role) &&
        Objects.equals(this.status, user.status) &&
        Objects.equals(this.dayLimit, user.dayLimit) &&
        Objects.equals(this.transactionLimit, user.transactionLimit) &&
        Objects.equals(this.bankAccounts, user.bankAccounts);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, firstName, lastName, email, password, role, status, dayLimit, transactionLimit, bankAccounts);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class User {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    firstName: ").append(toIndentedString(firstName)).append("\n");
    sb.append("    lastName: ").append(toIndentedString(lastName)).append("\n");
    sb.append("    email: ").append(toIndentedString(email)).append("\n");
    sb.append("    password: ").append(toIndentedString(password)).append("\n");
    sb.append("    role: ").append(toIndentedString(role)).append("\n");
    sb.append("    status: ").append(toIndentedString(status)).append("\n");
    sb.append("    dayLimit: ").append(toIndentedString(dayLimit)).append("\n");
    sb.append("    transactionLimit: ").append(toIndentedString(transactionLimit)).append("\n");
    sb.append("    bankAccounts: ").append(toIndentedString(bankAccounts)).append("\n");
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
