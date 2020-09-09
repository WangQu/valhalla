package com.iflytek.zhyl.valhalla.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QTbDatasource is a Querydsl query type for TbDatasource
 */
public class QTbDatasource extends com.querydsl.sql.RelationalPathBase<TbDatasource> {

    private static final long serialVersionUID = -1246903335;

    public static final QTbDatasource qTbDatasource = new QTbDatasource("tb_datasource");

    public final StringPath config = createString("config");

    public final StringPath createOperator = createString("createOperator");

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    public final NumberPath<Integer> enabled = createNumber("enabled", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath modifyOperator = createString("modifyOperator");

    public final DateTimePath<java.util.Date> modifyTime = createDateTime("modifyTime", java.util.Date.class);

    public final StringPath name = createString("name");

    public final com.querydsl.sql.PrimaryKey<TbDatasource> tbDatasourcexPkey = createPrimaryKey(id);

    public QTbDatasource(String variable) {
        super(TbDatasource.class, forVariable(variable), "public", "tb_datasource");
        addMetadata();
    }

    public QTbDatasource(String variable, String schema, String table) {
        super(TbDatasource.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QTbDatasource(String variable, String schema) {
        super(TbDatasource.class, forVariable(variable), schema, "tb_datasource");
        addMetadata();
    }

    public QTbDatasource(Path<? extends TbDatasource> path) {
        super(path.getType(), path.getMetadata(), "public", "tb_datasource");
        addMetadata();
    }

    public QTbDatasource(PathMetadata metadata) {
        super(TbDatasource.class, metadata, "public", "tb_datasource");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(config, ColumnMetadata.named("config").withIndex(3).ofType(Types.VARCHAR).withSize(1024));
        addMetadata(createOperator, ColumnMetadata.named("create_operator").withIndex(5).ofType(Types.VARCHAR).withSize(32));
        addMetadata(createTime, ColumnMetadata.named("create_time").withIndex(6).ofType(Types.TIMESTAMP).withSize(35).withDigits(6));
        addMetadata(enabled, ColumnMetadata.named("enabled").withIndex(4).ofType(Types.INTEGER).withSize(10));
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
        addMetadata(modifyOperator, ColumnMetadata.named("modify_operator").withIndex(7).ofType(Types.VARCHAR).withSize(32));
        addMetadata(modifyTime, ColumnMetadata.named("modify_time").withIndex(8).ofType(Types.TIMESTAMP).withSize(35).withDigits(6));
        addMetadata(name, ColumnMetadata.named("name").withIndex(2).ofType(Types.VARCHAR).withSize(128));
    }

}

