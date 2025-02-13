package cn.z.zai.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BirdEyeTokenOverviewRequest {

  /**
   * Address of a token
   */
  private String address;
}
