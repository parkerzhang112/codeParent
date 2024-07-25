package com.code.baseservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity基类
 *
 * @author ruoyi
 */
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("page_size")
    private Integer pageSize=10;

    @JsonProperty("page_num")
    private Integer pageNum= 1;

    /**
     * 创建者
     */
    private String createBy;


    /**
     * 请求参数
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> params;



    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }






    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }

        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }
}
