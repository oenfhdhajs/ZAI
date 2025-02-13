package cn.z.zai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.BigInteger;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuickNodeResponseTokenAccountsByOwnerItemValueAccount {

      private QuickNodeResponseTokenAccountsByOwnerItemValueAccountData data;

      private Boolean executable;

      private BigDecimal lamports;

      private String owner;

      private BigInteger rentEpoch;

      private Integer space;
}
