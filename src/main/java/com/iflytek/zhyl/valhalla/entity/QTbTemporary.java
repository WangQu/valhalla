package com.iflytek.zhyl.valhalla.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import com.querydsl.core.types.Path;

import com.querydsl.sql.ColumnMetadata;
import java.sql.Types;




/**
 * QTbTemporary is a Querydsl query type for TbTemporary
 */
public class QTbTemporary extends com.querydsl.sql.RelationalPathBase<TbTemporary> {

    private static final long serialVersionUID = 1735616573;

    public static final QTbTemporary qTbTemporary = new QTbTemporary("tb_temporary");

    public final DateTimePath<java.util.Date> createTime = createDateTime("createTime", java.util.Date.class);

    public final StringPath data = createString("data");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.querydsl.sql.PrimaryKey<TbTemporary> ordersPkey = createPrimaryKey(id);

    public QTbTemporary(String variable) {
        super(TbTemporary.class, forVariable(variable), "public", "tb_temporary");
        addMetadata();
    }

    public QTbTemporary(String variable, String schema, String table) {
        super(TbTemporary.class, forVariable(variable), schema, table);
        addMetadata();
    }

    public QTbTemporary(String variable, String schema) {
        super(TbTemporary.class, forVariable(variable), schema, "tb_temporary");
        addMetadata();
    }

    public QTbTemporary(Path<? extends TbTemporary> path) {
        super(path.getType(), path.getMetadata(), "public", "tb_temporary");
        addMetadata();
    }

    public QTbTemporary(PathMetadata metadata) {
        super(TbTemporary.class, metadata, "public", "tb_temporary");
        addMetadata();
    }

    public void addMetadata() {
        addMetadata(createTime, ColumnMetadata.named("create_time").withIndex(3).ofType(Types.TIMESTAMP).withSize(28));
        addMetadata(data, ColumnMetadata.named("data").withIndex(2).ofType(Types.VARCHAR).withSize(2147483647).notNull());
        addMetadata(id, ColumnMetadata.named("id").withIndex(1).ofType(Types.BIGINT).withSize(19).notNull());
    }

}

