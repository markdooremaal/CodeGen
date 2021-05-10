package io.swagger.model;

import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import org.threeten.bp.OffsetDateTime;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

/**
 * Transfer
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2021-05-10T11:33:45.087Z[GMT]")


public class Transfer   {
  @JsonProperty("account")
  private String account = null;

  /**
   * Transfer type
   */
  public enum TypeEnum {
    DEPOSIT("deposit"),
    
    WITHDRAWAL("withdrawal");

    private String value;

    TypeEnum(String value) {
      this.value = value;
    }

    @Override
    @JsonValue
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(String text) {
      for (TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }
  @JsonProperty("type")
  private TypeEnum type = null;

  @JsonProperty("amount")
  private Double amount = null;

  @JsonProperty("userPerforming")
  private Integer userPerforming = null;

  @JsonProperty("timestamp")
  private OffsetDateTime timestamp = null;

  public Transfer account(String account) {
    this.account = account;
    return this;
  }

  /**
   * The unique identifier for the account
   * @return account
   **/
  @Schema(example = "NL01INHO0000000001", required = true, description = "The unique identifier for the account")
      @NotNull

  @Pattern(regexp="/^[a-zA-Z]{2}[0-9]{2}[a-zA-Z0-9]{4}[0-9]{7}([a-zA-Z0-9]?){0,16}$/")   public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public Transfer type(TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Transfer type
   * @return type
   **/
  @Schema(example = "deposit", required = true, description = "Transfer type")
      @NotNull

    public TypeEnum getType() {
    return type;
  }

  public void setType(TypeEnum type) {
    this.type = type;
  }

  public Transfer amount(Double amount) {
    this.amount = amount;
    return this;
  }

  /**
   * The amount of money
   * @return amount
   **/
  @Schema(example = "10.42", required = true, description = "The amount of money")
      @NotNull

    public Double getAmount() {
    return amount;
  }

  public void setAmount(Double amount) {
    this.amount = amount;
  }

  public Transfer userPerforming(Integer userPerforming) {
    this.userPerforming = userPerforming;
    return this;
  }

  /**
   * The user performing the transfer
   * @return userPerforming
   **/
  @Schema(example = "1", required = true, description = "The user performing the transfer")
      @NotNull

    public Integer getUserPerforming() {
    return userPerforming;
  }

  public void setUserPerforming(Integer userPerforming) {
    this.userPerforming = userPerforming;
  }

  public Transfer timestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
    return this;
  }

  /**
   * Date and time of the transfer
   * @return timestamp
   **/
  @Schema(required = true, description = "Date and time of the transfer")
      @NotNull

    @Valid
    public OffsetDateTime getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(OffsetDateTime timestamp) {
    this.timestamp = timestamp;
  }


  @Override
  public boolean equals(java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Transfer transfer = (Transfer) o;
    return Objects.equals(this.account, transfer.account) &&
        Objects.equals(this.type, transfer.type) &&
        Objects.equals(this.amount, transfer.amount) &&
        Objects.equals(this.userPerforming, transfer.userPerforming) &&
        Objects.equals(this.timestamp, transfer.timestamp);
  }

  @Override
  public int hashCode() {
    return Objects.hash(account, type, amount, userPerforming, timestamp);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class Transfer {\n");
    
    sb.append("    account: ").append(toIndentedString(account)).append("\n");
    sb.append("    type: ").append(toIndentedString(type)).append("\n");
    sb.append("    amount: ").append(toIndentedString(amount)).append("\n");
    sb.append("    userPerforming: ").append(toIndentedString(userPerforming)).append("\n");
    sb.append("    timestamp: ").append(toIndentedString(timestamp)).append("\n");
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
