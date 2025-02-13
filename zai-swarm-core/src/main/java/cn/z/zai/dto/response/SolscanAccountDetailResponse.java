package cn.z.zai.dto.response;

import lombok.Data;

import java.math.BigInteger;


@Data
public class SolscanAccountDetailResponse {

  private String account;

  private Long lamports;

  private String type;

  private Boolean executable;

  private String owner_program;

  private BigInteger rent_epoch;

  private Boolean is_oncurve;
}