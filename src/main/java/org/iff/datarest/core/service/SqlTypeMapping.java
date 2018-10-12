/*******************************************************************************
 * Copyright (c) Oct 8, 2016 @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>.
 * All rights reserved.
 *
 * Contributors:
 *     <a href="mailto:iffiff1@gmail.com">Tyler Chen</a> - initial API and implementation
 ******************************************************************************/
package org.iff.datarest.core.service;

import java.math.BigDecimal;
import java.sql.Types;

/**
 * @author <a href="mailto:iffiff1@gmail.com">Tyler Chen</a>
 * @since Oct 8, 2016
 */
public class SqlTypeMapping {

    public static String getMybatisJavaType(int jdbcType) {
        switch (jdbcType) {
            case Types.CHAR:
                return "String";

            case Types.VARCHAR:
                return "String";

            case Types.LONGVARCHAR:
                return "String";
            case Types.NCHAR:
                return "String";

            case Types.NVARCHAR:
                return "String";

            case Types.LONGNVARCHAR:
                return "String";

            case Types.SQLXML:
                return "String";

            case Types.NUMERIC:
                return BigDecimal.class.getName();

            case Types.DECIMAL:
                return BigDecimal.class.getName();

            case Types.BIT:
                return "boolean";

            case Types.BOOLEAN:
                return "boolean";

            case Types.TINYINT:
                return "byte";

            case Types.SMALLINT:
                return "short";

            case Types.INTEGER:
                return "int";

            case Types.BIGINT:
                return "long";

            case Types.REAL:
                return "float";

            case Types.FLOAT:
                return "double";

            case Types.DOUBLE:
                return "double";

            case Types.BINARY:
                return "byte[]";

            case Types.VARBINARY:
                return "byte[]";

            case Types.LONGVARBINARY:
                return "byte[]";

            case Types.DATE:
                return java.sql.Date.class.getName();

            case Types.TIME:
                return java.sql.Time.class.getName();

            case Types.TIMESTAMP:
                return java.sql.Timestamp.class.getName();

            case Types.CLOB:
                return java.sql.Clob.class.getName();

            case Types.NCLOB:
                return java.sql.Clob.class.getName();

            case Types.BLOB:
                return java.sql.Blob.class.getName();

            case Types.ARRAY:
                return java.sql.Array.class.getName();

            case Types.STRUCT:
                return java.sql.Struct.class.getName();

            case Types.REF:
                return java.sql.Ref.class.getName();

            case Types.DATALINK:
                return java.net.URL.class.getName();

            default:
                return "String";
        }
    }
}
