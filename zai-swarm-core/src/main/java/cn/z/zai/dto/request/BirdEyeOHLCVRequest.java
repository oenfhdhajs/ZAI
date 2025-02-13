package cn.z.zai.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BirdEyeOHLCVRequest {

  private String address;

  /**
   * {1m,2m,5m,15m,.....}
   */
  private String type;

  private Long time_from;

  private Long time_to;
}
