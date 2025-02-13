package cn.z.zai.dto.response;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuickNodeResponseTokenAccountsByOwnerItemValueAccountDataParsedInfoTokenAmount {

      private String amount;

      private Integer decimals;

      private BigDecimal uiAmount;

      private String uiAmountString;
}
