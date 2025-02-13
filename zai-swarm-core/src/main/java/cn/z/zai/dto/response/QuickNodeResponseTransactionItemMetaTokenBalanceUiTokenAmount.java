package cn.z.zai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuickNodeResponseTransactionItemMetaTokenBalanceUiTokenAmount {

      private String amount;

      private Integer decimals;

      private BigDecimal uiAmount;

      private String uiAmountString;
}
