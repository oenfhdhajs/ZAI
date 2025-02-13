package cn.z.zai.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.Map;


@Data
@AllArgsConstructor
public class Changes implements Serializable {
    private static final long serialVersionUID = -4177320365902479152L;

    private String address;
    private long solChange;
    private Map<String, Long> tokenChanges;
}
