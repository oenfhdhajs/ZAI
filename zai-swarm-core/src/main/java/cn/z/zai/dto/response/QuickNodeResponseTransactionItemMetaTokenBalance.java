package cn.z.zai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuickNodeResponseTransactionItemMetaTokenBalance {

      private Integer accountIndex;

      private String mint;

      private String owner;

      private String programId;

      private QuickNodeResponseTransactionItemMetaTokenBalanceUiTokenAmount uiTokenAmount;
}
