package cn.z.zai.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BirdEyeTokenOverviewResponseItemExtensions {

  private String coingeckoId;

  private String telegram;

  private String twitter;

  private String description;


}
