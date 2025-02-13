package cn.z.zai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuickNodeResponseTokenAccountsByOwnerItemContext {

      private Long blockTime;

      private QuickNodeResponseTransactionItemMeta meta;

      private Long slot;

      private Integer version;
}
