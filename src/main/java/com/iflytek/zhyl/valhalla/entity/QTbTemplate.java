package com.iflytek.zhyl.valhalla.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QTbTemplate is a Querydsl query type for TbTemplate
 */
public class QTbTemplate extends com.querydsl.sql.RelationalPathBase<TbTemplate> {

    private static final long serialVersionUID = 610071822;

    public static final QTbTemplate qTbTemplate = new QTbTemplate("tb_template");

    public final SimplePath<byte[]> args = createSimple("args", byte[].class);

    public final StringPath createOperator = createString("createOperator");

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    public final NumberPath<Long> datasourceId = createNumber("datasourceId", Long.class);

    public final NumberPath<Integer> enabled = createNumber("enabled", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath modifyOperator = createString("modifyOperator");

    public final DateTimePath<java.util.Date> modifyTime = createDateTime("modifyTime", java.util.Date.class);

    public final StringPath name = createString("name");

    public final SimplePath<byte[]> syntax = createSimple("syntax", byte[].class);

    public final com.querydsl.sql.PrimaryKey<TbTemplate> tbTemplatePkey = createPrimaryKey(id);

    public QTbTemplate(String variable) {
        super(TbTemplate.class, forVariable(variable), "public", "tb_template");
        addMetadata();
    }

    public QTbTemplate(String variable, String schema, String table) {
        super(TbTemplate.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QTbTemplate(String variable, String schema) {
        super(TbTemplate.class, forVariable(variable), schema, "tb_template");
        addMetadata();
    }

    public QTbTemplate(Path<? extends TbTemplate> path) {
        super(path.getType(), path.getMetadata(), "public", "tb_template");
        addMetadata();
    }

    public QTbTemplate(PathMetadata metadata) {
        super(TbTemplate.class, metadata, "public", "tb_template");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(args, ColumnMetadata.named("args").withIndex(4).ofType(Types.BINARY).withSize(2147483647));
        addMetadata(createOperator, ColumnMetadata.named("create_operator").withIndex(7).ofType(Types.VARCHAR).withSize(128));
        addMetadata(createTime, ColumnMetadata.named("create_time").withIndex(8).ofType(Types.TIMESTAMP).withSize(35).withDigits(6));
        addMetadata(datasourceId, ColumnMetadata.named("datasource_id").withIndex(5).ofType(Types.BIGINT).withSize(19));
        addMetadata(enabled, ColumnMetadata.named("enabled").withIndex(6).ofType(Types.INTEGER).withSize(10));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(modifyOperator, ColumnMetadata.named("modify_operator").withIndex(9).ofType(Types.VARCHAR).withSize(128));
        addMetadata(modifyTime, ColumnMetadata.named("modify_time").withIndex(10).ofType(Types.TIMESTAMP).withSize(35).withDigits(6));
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(128));
        addMetadata(syntax, ColumnMetadata.named("syntax").withIndex(3).ofType(Types.BINARY).withSize(2147483647));
    }

}

