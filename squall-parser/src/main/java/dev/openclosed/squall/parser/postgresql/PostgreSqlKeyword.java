/*
 * Copyright 2022-2023 The Squall Authors
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

package dev.openclosed.squall.parser.postgresql;

import dev.openclosed.squall.parser.basic.IdentifierType;
import dev.openclosed.squall.parser.basic.StandardKeyword;
import dev.openclosed.squall.parser.basic.Keyword;
import dev.openclosed.squall.parser.basic.OperatorGroup;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

enum PostgreSqlKeyword implements Keyword {
    A,
    ABORT,
    ABS,
    ABSENT,
    ABSOLUTE,
    ACCESS,
    ACCORDING,
    ACOS,
    ACTION,
    ADA,
    ADD,
    ADMIN,
    AFTER,
    AGGREGATE,
    ALL(Option.RESERVED),
    ALLOCATE,
    ALSO,
    ALTER,
    ALWAYS,
    ANALYSE(Option.RESERVED),
    ANALYZE(Option.RESERVED),
    AND(Option.RESERVED) {
        @Override
        public boolean isBinaryOperator() {
            return true;
        }

        @Override
        public OperatorGroup binaryOperatorGroup() {
            return PostgreSqlOperatorGroup.LOGICAL_CONJUNCTION;
        }
    },
    ANY(Option.RESERVED),
    ARE,
    ARRAY(Option.RESERVED | Option.REQUIRES_AS),
    ARRAY_AGG,
    ARRAY_MAX_CARDINALITY,
    AS(Option.RESERVED | Option.REQUIRES_AS),
    ASC(Option.RESERVED),
    ASENSITIVE,
    ASIN,
    ASSERTION,
    ASSIGNMENT,
    ASYMMETRIC(Option.RESERVED),
    AT,
    ATAN,
    ATOMIC,
    ATTACH,
    ATTRIBUTE,
    ATTRIBUTES,
    AUTHORIZATION(Option.NOT_FOR_TABLE),
    AVG,
    BACKWARD,
    BASE64,
    BEFORE,
    BEGIN,
    BEGIN_FRAME,
    BEGIN_PARTITION,
    BERNOULLI,
    BETWEEN(Option.NOT_FOR_FUNCTION),
    BIGINT(Option.NOT_FOR_FUNCTION),
    BINARY(Option.NOT_FOR_TABLE),
    BIT(Option.NOT_FOR_FUNCTION),
    BIT_LENGTH,
    BLOB,
    BLOCKED,
    BOM,
    BOOLEAN(Option.NOT_FOR_FUNCTION),
    BOTH(Option.RESERVED),
    BREADTH,
    BY,
    C,
    CACHE,
    CALL,
    CALLED,
    CARDINALITY,
    CASCADE,
    CASCADED,
    CASE(Option.RESERVED),
    CAST(Option.RESERVED),
    CATALOG,
    CATALOG_NAME,
    CEIL,
    CEILING,
    CHAIN,
    CHAINING,
    CHAR(Option.NOT_FOR_FUNCTION | Option.REQUIRES_AS),
    CHARACTER(Option.NOT_FOR_FUNCTION | Option.REQUIRES_AS),
    CHARACTERISTICS,
    CHARACTERS,
    CHARACTER_LENGTH,
    CHARACTER_SET_CATALOG,
    CHARACTER_SET_NAME,
    CHARACTER_SET_SCHEMA,
    CHAR_LENGTH,
    CHECK(Option.RESERVED),
    CHECKPOINT,
    CLASS,
    CLASSIFIER,
    CLASS_ORIGIN,
    CLOB,
    CLOSE,
    CLUSTER,
    COALESCE(Option.NOT_FOR_FUNCTION),
    COBOL,
    COLLATE(Option.RESERVED),
    COLLATION(Option.NOT_FOR_TABLE),
    COLLATION_CATALOG,
    COLLATION_NAME,
    COLLATION_SCHEMA,
    COLLECT,
    COLUMN(Option.RESERVED),
    COLUMNS,
    COLUMN_NAME,
    COMMAND_FUNCTION,
    COMMAND_FUNCTION_CODE,
    COMMENT,
    COMMENTS,
    COMMIT,
    COMMITTED,
    COMPRESSION,
    CONCURRENTLY(Option.NOT_FOR_TABLE),
    CONDITION,
    CONDITIONAL,
    CONDITION_NUMBER,
    CONFIGURATION,
    CONFLICT,
    CONNECT,
    CONNECTION,
    CONNECTION_NAME,
    CONSTRAINT(Option.RESERVED),
    CONSTRAINTS,
    CONSTRAINT_CATALOG,
    CONSTRAINT_NAME,
    CONSTRAINT_SCHEMA,
    CONSTRUCTOR,
    CONTAINS,
    CONTENT,
    CONTINUE,
    CONTROL,
    CONVERSION,
    CONVERT,
    COPY,
    CORR,
    CORRESPONDING,
    COS,
    COSH,
    COST,
    COUNT,
    COVAR_POP,
    COVAR_SAMP,
    CREATE(Option.RESERVED | Option.REQUIRES_AS),
    CROSS(Option.NOT_FOR_TABLE),
    CSV,
    CUBE,
    CUME_DIST,
    CURRENT,
    CURRENT_CATALOG(Option.RESERVED),
    CURRENT_DATE(Option.RESERVED | Option.FUNCTION),
    CURRENT_DEFAULT_TRANSFORM_GROUP,
    CURRENT_PATH,
    CURRENT_ROLE(Option.RESERVED),
    CURRENT_ROW,
    CURRENT_SCHEMA(Option.NOT_FOR_TABLE),
    CURRENT_TIME(Option.RESERVED | Option.FUNCTION),
    CURRENT_TIMESTAMP(Option.RESERVED | Option.FUNCTION),
    CURRENT_TRANSFORM_GROUP_FOR_TYPE,
    CURRENT_USER(Option.RESERVED),
    CURSOR,
    CURSOR_NAME,
    CYCLE,
    DATA,
    DATABASE,
    DATALINK,
    DATE,
    DATETIME_INTERVAL_CODE,
    DATETIME_INTERVAL_PRECISION,
    DAY(Option.REQUIRES_AS),
    DB,
    DEALLOCATE,
    DEC,
    DECFLOAT,
    DECIMAL,
    DECLARE,
    DEFAULT(Option.RESERVED),
    DEFAULTS,
    DEFERRABLE(Option.RESERVED),
    DEFERRED,
    DEFINE,
    DEFINED,
    DEFINER,
    DEGREE,
    DELETE,
    DELIMITER,
    DELIMITERS,
    DENSE_RANK,
    DEPENDS,
    DEPTH,
    DEREF,
    DERIVED,
    DESC(Option.RESERVED),
    DESCRIBE,
    DESCRIPTOR,
    DETACH,
    DETERMINISTIC,
    DIAGNOSTICS,
    DICTIONARY,
    DISABLE,
    DISCARD,
    DISCONNECT,
    DISPATCH,
    DISTINCT(Option.RESERVED),
    DLNEWCOPY,
    DLPREVIOUSCOPY,
    DLURLCOMPLETE,
    DLURLCOMPLETEONLY,
    DLURLCOMPLETEWRITE,
    DLURLPATH,
    DLURLPATHONLY,
    DLURLPATHWRITE,
    DLURLSCHEME,
    DLURLSERVER,
    DLVALUE,
    DO(Option.RESERVED),
    DOCUMENT,
    DOMAIN,
    DOUBLE,
    DROP,
    DYNAMIC,
    DYNAMIC_FUNCTION,
    DYNAMIC_FUNCTION_CODE,
    EACH,
    ELEMENT,
    ELSE(Option.RESERVED),
    EMPTY,
    ENABLE,
    ENCODING,
    ENCRYPTED,
    END(Option.RESERVED),
    END_EXEC() {
        @Override
        public String canonicalName() {
            return "END-EXEC";
        }
    },
    END_FRAME,
    END_PARTITION,
    ENFORCED,
    ENUM,
    EQUALS,
    ERROR,
    ESCAPE,
    EVENT,
    EVERY,
    EXCEPT(Option.RESERVED | Option.REQUIRES_AS),
    EXCEPTION,
    EXCLUDE,
    EXCLUDING,
    EXCLUSIVE,
    EXEC,
    EXECUTE,
    EXISTS(Option.NOT_FOR_FUNCTION),
    EXP,
    EXPLAIN,
    EXPRESSION,
    EXTENSION,
    EXTERNAL,
    EXTRACT(Option.NOT_FOR_FUNCTION),
    FALSE(Option.RESERVED),
    FAMILY,
    FETCH(Option.RESERVED | Option.REQUIRES_AS),
    FILE,
    FILTER(Option.REQUIRES_AS),
    FINAL,
    FINALIZE,
    FINISH,
    FIRST,
    FIRST_VALUE,
    FLAG,
    FLOAT(Option.NOT_FOR_FUNCTION),
    FLOOR,
    FOLLOWING,
    FOR(Option.RESERVED | Option.REQUIRES_AS),
    FORCE,
    FOREIGN(Option.RESERVED),
    FORMAT,
    FORTRAN,
    FORWARD,
    FOUND,
    FRAME_ROW,
    FREE,
    FREEZE(Option.NOT_FOR_TABLE),
    FROM(Option.RESERVED | Option.REQUIRES_AS),
    FS,
    FULFILL,
    FULL(Option.NOT_FOR_TABLE),
    FUNCTION,
    FUNCTIONS,
    FUSION,
    G,
    GENERAL,
    GENERATED,
    GET,
    GLOBAL,
    GO,
    GOTO,
    GRANT(Option.RESERVED | Option.REQUIRES_AS),
    GRANTED,
    GREATEST,
    GROUP(Option.RESERVED | Option.REQUIRES_AS),
    GROUPING,
    GROUPS,
    HANDLER,
    HAVING(Option.RESERVED | Option.REQUIRES_AS),
    HEADER,
    HEX,
    HIERARCHY,
    HOLD,
    HOUR(Option.REQUIRES_AS),
    ID,
    IDENTITY,
    IF,
    IGNORE,
    ILIKE(Option.NOT_FOR_TABLE),
    IMMEDIATE,
    IMMEDIATELY,
    IMMUTABLE,
    IMPLEMENTATION,
    IMPLICIT,
    IMPORT,
    IN(Option.RESERVED) {
        @Override
        public OperatorGroup binaryOperatorGroup() {
            return PostgreSqlOperatorGroup.RANGE_MEMBERSHIP;
        }
    },
    INCLUDE,
    INCLUDING,
    INCREMENT,
    INDENT,
    INDEX,
    INDEXES,
    INDICATOR,
    INHERIT,
    INHERITS,
    INITIAL,
    INITIALLY(Option.RESERVED),
    INLINE,
    INNER(Option.NOT_FOR_TABLE),
    INOUT(Option.NOT_FOR_FUNCTION),
    INPUT,
    INSENSITIVE,
    INSERT,
    INSTANCE,
    INSTANTIABLE,
    INSTEAD,
    INT(Option.NOT_FOR_FUNCTION),
    INTEGER(Option.NOT_FOR_FUNCTION),
    INTEGRITY,
    INTERSECT(Option.RESERVED | Option.REQUIRES_AS),
    INTERSECTION,
    INTERVAL(Option.NOT_FOR_FUNCTION),
    INTO(Option.RESERVED | Option.REQUIRES_AS),
    INVOKER,
    IS(Option.NOT_FOR_TABLE) {
        @Override
        public OperatorGroup binaryOperatorGroup() {
            return PostgreSqlOperatorGroup.IS;
        }
    },
    ISNULL(Option.NOT_FOR_TABLE | Option.REQUIRES_AS) {
        @Override
        public OperatorGroup binaryOperatorGroup() {
            return PostgreSqlOperatorGroup.IS;
        }
    },
    ISOLATION,
    JOIN(Option.NOT_FOR_TABLE),
    JSON_ARRAY,
    JSON_ARRAYAGG,
    JSON_EXISTS,
    JSON_OBJECT,
    JSON_OBJECTAGG,
    JSON_QUERY,
    JSON_TABLE,
    JSON_TABLE_PRIMITIVE,
    JSON_VALUE,
    K,
    KEEP,
    KEY,
    KEYS,
    KEY_MEMBER,
    KEY_TYPE,
    LABEL,
    LAG,
    LANGUAGE,
    LARGE,
    LAST,
    LAST_VALUE,
    LATERAL(Option.RESERVED),
    LEAD,
    LEADING(Option.RESERVED),
    LEAKPROOF,
    LEAST(Option.NOT_FOR_FUNCTION),
    LEFT(Option.NOT_FOR_TABLE),
    LENGTH,
    LEVEL,
    LIBRARY,
    LIKE(Option.NOT_FOR_TABLE),
    LIKE_REGEX,
    LIMIT(Option.RESERVED | Option.REQUIRES_AS),
    LINK,
    LISTAGG,
    LISTEN,
    LN,
    LOAD,
    LOCAL,
    LOCALTIME(Option.RESERVED | Option.FUNCTION),
    LOCALTIMESTAMP(Option.RESERVED | Option.FUNCTION),
    LOCATION,
    LOCATOR,
    LOCK,
    LOCKED,
    LOG,
    LOG10,
    LOGGED,
    LOWER,
    M,
    MAP,
    MAPPING,
    MATCH,
    MATCHED,
    MATCHES,
    MATCH_NUMBER,
    MATCH_RECOGNIZE,
    MATERIALIZED,
    MAX,
    MAXVALUE,
    MEASURES,
    MEMBER,
    MERGE,
    MESSAGE_LENGTH,
    MESSAGE_OCTET_LENGTH,
    MESSAGE_TEXT,
    METHOD,
    MIN,
    MINUTE(Option.REQUIRES_AS),
    MINVALUE,
    MOD,
    MODE,
    MODIFIES,
    MODULE,
    MONTH(Option.REQUIRES_AS),
    MORE,
    MOVE,
    MULTISET,
    MUMPS,
    NAME,
    NAMES,
    NAMESPACE,
    NATIONAL(Option.NOT_FOR_FUNCTION),
    NATURAL(Option.NOT_FOR_TABLE),
    NCHAR(Option.NOT_FOR_FUNCTION),
    NCLOB,
    NESTED,
    NESTING,
    NEW,
    NEXT,
    NFC,
    NFD,
    NFKC,
    NFKD,
    NIL,
    NO,
    NONE(Option.NOT_FOR_FUNCTION),
    NORMALIZE(Option.NOT_FOR_FUNCTION),
    NORMALIZED,
    NOT(Option.RESERVED) {
        @Override
        public OperatorGroup unaryOperatorGroup() {
            return PostgreSqlOperatorGroup.LOGICAL_NEGATION;
        }

        @Override
        public OperatorGroup binaryOperatorGroup() {
            return PostgreSqlOperatorGroup.RANGE_MEMBERSHIP;
        }
    },
    NOTHING,
    NOTIFY,
    NOTNULL(Option.NOT_FOR_TABLE | Option.REQUIRES_AS) {
        @Override
        public OperatorGroup binaryOperatorGroup() {
            return PostgreSqlOperatorGroup.IS;
        }
    },
    NOWAIT,
    NTH_VALUE,
    NTILE,
    NULL(Option.RESERVED),
    NULLABLE,
    NULLIF(Option.NOT_FOR_FUNCTION),
    NULLS,
    NULL_ORDERING,
    NUMBER,
    NUMERIC(Option.NOT_FOR_FUNCTION),
    OBJECT,
    OCCURRENCE,
    OCCURRENCES_REGEX,
    OCTETS,
    OCTET_LENGTH,
    OF,
    OFF,
    OFFSET(Option.RESERVED | Option.REQUIRES_AS),
    OIDS,
    OLD,
    OMIT,
    ON(Option.RESERVED | Option.REQUIRES_AS),
    ONE,
    ONLY(Option.RESERVED),
    OPEN,
    OPERATOR,
    OPTION,
    OPTIONS,
    OR(Option.RESERVED) {
        @Override
        public boolean isBinaryOperator() {
            return true;
        }

        @Override
        public OperatorGroup binaryOperatorGroup() {
            return PostgreSqlOperatorGroup.LOGICAL_DISJUNCTION;
        }
    },
    ORDER(Option.RESERVED | Option.REQUIRES_AS),
    ORDERING,
    ORDINALITY,
    OTHERS,
    OUT(Option.NOT_FOR_FUNCTION),
    OUTER(Option.NOT_FOR_TABLE),
    OUTPUT,
    OVER(Option.REQUIRES_AS),
    OVERFLOW,
    OVERLAPS(Option.NOT_FOR_TABLE | Option.REQUIRES_AS),
    OVERLAY(Option.NOT_FOR_FUNCTION),
    OVERRIDING,
    OWNED,
    OWNER,
    P,
    PAD,
    PARALLEL,
    PARAMETER,
    PARAMETER_MODE,
    PARAMETER_NAME,
    PARAMETER_ORDINAL_POSITION,
    PARAMETER_SPECIFIC_CATALOG,
    PARAMETER_SPECIFIC_NAME,
    PARAMETER_SPECIFIC_SCHEMA,
    PARSER,
    PARTIAL,
    PARTITION,
    PASCAL,
    PASS,
    PASSING,
    PASSTHROUGH,
    PASSWORD,
    PAST,
    PATH,
    PATTERN,
    PER,
    PERCENT,
    PERCENTILE_CONT,
    PERCENTILE_DISC,
    PERCENT_RANK,
    PERIOD,
    PERMISSION,
    PERMUTE,
    PIPE,
    PLACING(Option.RESERVED),
    PLAN,
    PLANS,
    PLI,
    POLICY,
    PORTION,
    POSITION(Option.NOT_FOR_FUNCTION),
    POSITION_REGEX,
    POWER,
    PRECEDES,
    PRECEDING,
    PRECISION(Option.NOT_FOR_FUNCTION | Option.REQUIRES_AS),
    PREPARE,
    PREPARED,
    PRESERVE,
    PREV,
    PRIMARY(Option.RESERVED),
    PRIOR,
    PRIVATE,
    PRIVILEGES,
    PROCEDURAL,
    PROCEDURE,
    PROCEDURES,
    PROGRAM,
    PRUNE,
    PTF,
    PUBLIC,
    PUBLICATION,
    QUOTE,
    QUOTES,
    RANGE,
    RANK,
    READ,
    READS,
    REAL(Option.NOT_FOR_FUNCTION),
    REASSIGN,
    RECHECK,
    RECOVERY,
    RECURSIVE,
    REF,
    REFERENCES(Option.RESERVED),
    REFERENCING,
    REFRESH,
    REGR_AVGX,
    REGR_AVGY,
    REGR_COUNT,
    REGR_INTERCEPT,
    REGR_R2,
    REGR_SLOPE,
    REGR_SXX,
    REGR_SXY,
    REGR_SYY,
    REINDEX,
    RELATIVE,
    RELEASE,
    RENAME,
    REPEATABLE,
    REPLACE,
    REPLICA,
    REQUIRING,
    RESET,
    RESPECT,
    RESTART,
    RESTORE,
    RESTRICT,
    RESULT,
    RETURN,
    RETURNED_CARDINALITY,
    RETURNED_LENGTH,
    RETURNED_OCTET_LENGTH,
    RETURNED_SQLSTATE,
    RETURNING(Option.RESERVED | Option.REQUIRES_AS),
    RETURNS,
    REVOKE,
    RIGHT(Option.NOT_FOR_TABLE),
    ROLE,
    ROLLBACK,
    ROLLUP,
    ROUTINE,
    ROUTINES,
    ROUTINE_CATALOG,
    ROUTINE_NAME,
    ROUTINE_SCHEMA,
    ROW(Option.NOT_FOR_FUNCTION),
    ROWS,
    ROW_COUNT,
    ROW_NUMBER,
    RULE,
    RUNNING,
    SAVEPOINT,
    SCALAR,
    SCALE,
    SCHEMA,
    SCHEMAS,
    SCHEMA_NAME,
    SCOPE,
    SCOPE_CATALOG,
    SCOPE_NAME,
    SCOPE_SCHEMA,
    SCROLL,
    SEARCH,
    SECOND(Option.REQUIRES_AS),
    SECTION,
    SECURITY,
    SEEK,
    SELECT(Option.RESERVED),
    SELECTIVE,
    SELF,
    SEMANTICS,
    SENSITIVE,
    SEQUENCE,
    SEQUENCES,
    SERIALIZABLE,
    SERVER,
    SERVER_NAME,
    SESSION,
    SESSION_USER(Option.RESERVED),
    SET,
    SETOF(Option.NOT_FOR_FUNCTION),
    SETS,
    SHARE,
    SHOW,
    SIMILAR(Option.NOT_FOR_TABLE),
    SIMPLE,
    SIN,
    SINH,
    SIZE,
    SKIP,
    SMALLINT,
    SNAPSHOT,
    SOME(Option.RESERVED),
    SORT_DIRECTION,
    SOURCE,
    SPACE,
    SPECIFIC,
    SPECIFICTYPE,
    SPECIFIC_NAME,
    SQL,
    SQLCODE,
    SQLERROR,
    SQLEXCEPTION,
    SQLSTATE,
    SQLWARNING,
    SQRT,
    STABLE,
    STANDALONE,
    START,
    STATE,
    STATEMENT,
    STATIC,
    STATISTICS,
    STDDEV_POP,
    STDDEV_SAMP,
    STDIN,
    STDOUT,
    STORAGE,
    STORED,
    STRICT,
    STRING,
    STRIP,
    STRUCTURE,
    STYLE,
    SUBCLASS_ORIGIN,
    SUBMULTISET,
    SUBSCRIPTION,
    SUBSET,
    SUBSTRING(Option.NOT_FOR_FUNCTION),
    SUBSTRING_REGEX,
    SUCCEEDS,
    SUM,
    SUPPORT,
    SYMMETRIC(Option.RESERVED),
    SYSID,
    SYSTEM,
    SYSTEM_TIME,
    SYSTEM_USER,
    T,
    TABLE(Option.RESERVED),
    TABLES,
    TABLESAMPLE,
    TABLESPACE,
    TABLE_NAME,
    TAN,
    TANH,
    TEMP,
    TEMPLATE,
    TEMPORARY,
    TEXT,
    THEN(Option.RESERVED),
    THROUGH,
    TIES,
    TIME(Option.NOT_FOR_FUNCTION),
    TIMESTAMP(Option.NOT_FOR_FUNCTION),
    TIMEZONE_HOUR,
    TIMEZONE_MINUTE,
    TO(Option.RESERVED | Option.REQUIRES_AS),
    TOKEN,
    TOP_LEVEL_COUNT,
    TRAILING(Option.RESERVED),
    TRANSACTION,
    TRANSACTIONS_COMMITTED,
    TRANSACTIONS_ROLLED_BACK,
    TRANSACTION_ACTIVE,
    TRANSFORM,
    TRANSFORMS,
    TRANSLATE,
    TRANSLATE_REGEX,
    TRANSLATION,
    TREAT(Option.NOT_FOR_FUNCTION),
    TRIGGER,
    TRIGGER_CATALOG,
    TRIGGER_NAME,
    TRIGGER_SCHEMA,
    TRIM(Option.NOT_FOR_FUNCTION),
    TRIM_ARRAY,
    TRUE(Option.RESERVED),
    TRUNCATE,
    TRUSTED,
    TYPE,
    TYPES,
    UESCAPE,
    UNBOUNDED,
    UNCOMMITTED,
    UNCONDITIONAL,
    UNDER,
    UNENCRYPTED,
    UNION(Option.RESERVED | Option.REQUIRES_AS),
    UNIQUE(Option.RESERVED),
    UNKNOWN,
    UNLINK,
    UNLISTEN,
    UNLOGGED,
    UNMATCHED,
    UNNAMED,
    UNNEST,
    UNTIL,
    UNTYPED,
    UPDATE,
    UPPER,
    URI,
    USAGE,
    USER(Option.RESERVED),
    USER_DEFINED_TYPE_CATALOG,
    USER_DEFINED_TYPE_CODE,
    USER_DEFINED_TYPE_NAME,
    USER_DEFINED_TYPE_SCHEMA,
    USING(Option.RESERVED),
    UTF16,
    UTF32,
    UTF8,
    VACUUM,
    VALID,
    VALIDATE,
    VALIDATOR,
    VALUE,
    VALUES(Option.NOT_FOR_FUNCTION),
    VALUE_OF,
    VARBINARY,
    VARCHAR(Option.NOT_FOR_FUNCTION),
    VARIADIC(Option.RESERVED),
    VARYING(Option.REQUIRES_AS),
    VAR_POP,
    VAR_SAMP,
    VERBOSE(Option.NOT_FOR_TABLE),
    VERSION,
    VERSIONING,
    VIEW,
    VIEWS,
    VOLATILE,
    WHEN(Option.RESERVED),
    WHENEVER,
    WHERE(Option.RESERVED | Option.REQUIRES_AS),
    WHITESPACE,
    WIDTH_BUCKET,
    WINDOW(Option.RESERVED | Option.REQUIRES_AS),
    WITH(Option.RESERVED | Option.REQUIRES_AS),
    WITHIN(Option.REQUIRES_AS),
    WITHOUT(Option.REQUIRES_AS),
    WORK,
    WRAPPER,
    WRITE,
    XML,
    XMLAGG,
    XMLATTRIBUTES(Option.NOT_FOR_FUNCTION),
    XMLBINARY,
    XMLCAST,
    XMLCOMMENT,
    XMLCONCAT(Option.NOT_FOR_FUNCTION),
    XMLDECLARATION,
    XMLDOCUMENT,
    XMLELEMENT(Option.NOT_FOR_FUNCTION),
    XMLEXISTS(Option.NOT_FOR_FUNCTION),
    XMLFOREST(Option.NOT_FOR_FUNCTION),
    XMLITERATE,
    XMLNAMESPACES(Option.NOT_FOR_FUNCTION),
    XMLPARSE(Option.NOT_FOR_FUNCTION),
    XMLPI(Option.NOT_FOR_FUNCTION),
    XMLQUERY,
    XMLROOT(Option.NOT_FOR_FUNCTION),
    XMLSCHEMA,
    XMLSERIALIZE(Option.NOT_FOR_FUNCTION),
    XMLTABLE(Option.NOT_FOR_FUNCTION),
    XMLTEXT,
    XMLVALIDATE,
    YEAR(Option.REQUIRES_AS),
    YES,
    ZONE;

    static final class Option {
        static final int NOT_FOR_TABLE = 1;
        static final int NOT_FOR_FUNCTION = 2;
        static final int REQUIRES_AS = 4;
        static final int FUNCTION = 8;
        static final int RESERVED = NOT_FOR_TABLE | NOT_FOR_FUNCTION;
    }

    private final StandardKeyword standardKeyword;
    private final int options;

    PostgreSqlKeyword() {
        this.standardKeyword = findStandardKeyword();
        this.options = 0;
    }

    PostgreSqlKeyword(int options) {
        this.standardKeyword = findStandardKeyword();
        this.options = options;
    }

    // Keyword interface

    @Override
    public String canonicalName() {
        return name();
    }

    @Override
    public StandardKeyword standard() {
        return this.standardKeyword;
    }

    @Override
    public boolean isSameAs(Object obj) {
        return (this == obj || standard() == obj);
    }

    // Token interface

    @Override
    public boolean isIdentifier(IdentifierType type) {
        return switch (type) {
            case OBJECT_NAME -> (this.options & Option.NOT_FOR_TABLE) == 0;
            case FUNCTION_NAME, TYPE_NAME -> (this.options & Option.NOT_FOR_FUNCTION) == 0;
        };
    }
    @Override
    public boolean isFunction() {
        return (this.options & Option.FUNCTION) != 0;
    }

    static Map<String, Keyword> valuesAsMap() {
        return Stream.of(PostgreSqlKeyword.values())
            .collect(Collectors.toMap(Keyword::canonicalName, Function.identity()));
    }

    private StandardKeyword findStandardKeyword() {
        return StandardKeyword.lookUp(canonicalName());
    }
}