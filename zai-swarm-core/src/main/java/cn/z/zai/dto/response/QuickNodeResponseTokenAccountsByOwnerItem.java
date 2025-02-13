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
public class QuickNodeResponseTokenAccountsByOwnerItem {

      private QuickNodeResponseTokenAccountsByOwnerItemContext context;

      private List<QuickNodeResponseTokenAccountsByOwnerItemValue> value;

}
