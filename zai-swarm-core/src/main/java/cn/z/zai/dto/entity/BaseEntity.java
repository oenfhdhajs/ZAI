package cn.z.zai.dto.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = -6592752660378753957L;

    private Integer id;

    private LocalDateTime createdTime;

    private LocalDateTime updatedTime;

    private Boolean isDeleted;

}
