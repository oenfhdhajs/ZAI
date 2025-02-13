package cn.z.zai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QuickNodeResponseTokenAccountsByOwnerItemValueAccountDataParsedInfo {

      private Boolean isNative;

      private String mint;

      private String owner;

      private String state;

      private QuickNodeResponseTokenAccountsByOwnerItemValueAccountDataParsedInfoTokenAmount tokenAmount;
}
