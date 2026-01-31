package com.window.system.model.req;

import cn.hutool.json.JSONUtil;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BasePageReq {
    private Integer pageNo = 1;
    private Integer pageSize = 10;

    public Integer getStartIndex() {
        if (pageNo < 1) pageNo = 1;
        return (pageNo - 1) * pageSize;
    }

    @Override
    public String toString() {
        return JSONUtil.toJsonStr(this);
    }


}
