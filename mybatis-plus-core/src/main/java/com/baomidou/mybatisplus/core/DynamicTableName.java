package com.baomidou.mybatisplus.core;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Setter;

import java.util.Optional;

@Setter
public abstract class DynamicTableName {
    @TableField(exist = false)
    private String tableName;

    public DynamicTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getDynamicTableName() {
        return Optional.ofNullable(tableName).orElse("");
    }
}
