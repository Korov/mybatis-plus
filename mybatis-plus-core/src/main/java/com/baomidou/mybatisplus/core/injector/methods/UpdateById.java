/*
 * Copyright (c) 2011-2025, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baomidou.mybatisplus.core.injector.methods;

import com.baomidou.mybatisplus.core.DynamicTableName;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.injector.AbstractMethod;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlSource;

/**
 * 根据 ID 更新有值字段
 *
 * @author hubin
 * @since 2018-04-06
 */
public class UpdateById extends AbstractMethod {

    public UpdateById() {
        this(SqlMethod.UPDATE_BY_ID.getMethod());
    }

    /**
     * @param name 方法名
     * @since 3.5.0
     */
    public UpdateById(String name) {
        super(name);
    }

    @Override
    public MappedStatement injectMappedStatement(Class<?> mapperClass, Class<?> modelClass, TableInfo tableInfo) {
        SqlMethod sqlMethod = SqlMethod.UPDATE_BY_ID;
        String tableName;
        if (DynamicTableName.class.isAssignableFrom(modelClass)) {
            tableName = "<choose><when test=\"@org.apache.ibatis.reflection.SystemMetaObject@forObject(et).findProperty('dynamicTableName', false) != null and et.dynamicTableName != null and et.dynamicTableName != ''\">${et.dynamicTableName}</when><otherwise>" + tableInfo.getTableName() + "</otherwise></choose>";
        } else {
            tableName = tableInfo.getTableName();
        }
        final String additional = optlockVersion(tableInfo) + tableInfo.getLogicDeleteSql(true, true);
        String sql = String.format(sqlMethod.getSql(), tableName,
            sqlSet(tableInfo.isWithLogicDelete(), false, tableInfo, false, ENTITY, ENTITY_DOT),
            tableInfo.getKeyColumn(), ENTITY_DOT + tableInfo.getKeyProperty(), additional);
        SqlSource sqlSource = super.createSqlSource(configuration, sql, modelClass);
        return addUpdateMappedStatement(mapperClass, modelClass, methodName, sqlSource);
    }
}
