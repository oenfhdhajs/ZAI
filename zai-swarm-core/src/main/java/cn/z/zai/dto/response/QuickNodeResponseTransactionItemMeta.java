package cn.z.zai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuickNodeResponseTransactionItemMeta {

    private Long computeUnitsConsumed;

    private Object err;

    private Long fee;

    private List<Long> postBalances;

    private List<Long> preBalances;

    private List<QuickNodeResponseTransactionItemMetaTokenBalance> preTokenBalances;

    private List<QuickNodeResponseTransactionItemMetaTokenBalance> postTokenBalances;

}
