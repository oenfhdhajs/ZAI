package cn.z.zai.dto.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;


@Data
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    private static final long serialVersionUID = 8896507542369270147L;
    private BigInteger tgUserId;

    private LocalDate day;

    private Boolean isBot;

    private String firstName;

    private String lastName;

    private String userName;

    private String languageCode;

    private Boolean isActive;


    /**
     * @see cn.z.zai.common.enums.UserSourceType
     *
     *      default: 0 tg
     */
    private Integer source;

    private String address;

    private Long lamports;

    private String salt;

    //
    private String verifyCode;

    private BigInteger superiorsTgUserId;

    private BigDecimal yieldRate;

    private Integer invitedFriends;

    private BigDecimal commission;

    private Long pointsCoin;

    // ----- inc -------

    private Integer incInvitedFriends;

    private BigDecimal incCommission;

    private Boolean isExport;
}
