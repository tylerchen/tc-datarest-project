;
var upperCaseByEvent = function(event){
    var val=$(event.target).val();
    if(val && val.length>0){
        $(event.target).val(val.toUpperCase());
    }
};
var capitalizeByEvent = function(event){
    var val=$(event.target).val();
    if(val && val.length>0){
        $(event.target).val(val.substring(0,1).toUpperCase()+val.substring(1));
    }
};
var uncapitalizeByEvent = function(event){
    var val=$(event.target).val();
    if(val && val.length>0){
        $(event.target).val(val.substring(0,1).toLowerCase()+val.substring(1));
    }
};
var fieldSchema = {
    fieldName:{type: 'string', pattern: '^[a-z][a-zA-Z0-9_]*$', description: '小写开头，符合Java字符命名：^[a-z][a-zA-Z0-9_]*$', title: '字段名称', required: true},
    fieldAlias:{type: 'string', title: '字段中文名'},
    fieldColumn:{type: 'string', pattern: '^[A-Z][A-Z0-9_]*$', description: '^[A-Z][A-Z0-9_]*$', title: '数据库字段名称', required: true},
    fieldColType:{type: 'string', enum:['VARCHAR','CHAR','DATETIME','TIMESTAMP','INT','BOOLEAN','SMALLINT','DOUBLE','FLOAT','DECIMAL','NUMERIC','BLOB','MEDIUMBLOB','LONGBLOB','TEXT','MEDIUMTEXT','LONGTEXT','CLOB'], title: '数据库字段类型', required: true, default: 'VARCHAR'},
    fieldJavaType:{type: 'string', enum: ['String','Integer','Long','Date','Double','BigDecimal','Boolean','Float','Short','Byte','byte[]'], title: 'Java字段类型', required: true, default: 'String'},
    fieldLength:{type: 'integer', title: '字段长度'},
    fieldScale:{type: 'integer', title: '字段精度'},
    fieldValue:{type: 'string', title: '字段默认值'},
    fieldDesc:{type: 'string', title: '字段描述'},
    fieldSeq:{type: 'integer', title: '字段顺序'},
    isNull:{type: 'string', enum: ['Y','N'], title: '是否为空', required: true, default: 'N'},
    isPk:{type: 'string', enum: ['Y','N'], title: '是否为主键', required: true, default: 'N'},
    isIndex:{type: 'string', enum: ['Y','N'], title: '是否添加索引', required: true, default: 'N'},
    isUnique:{type: 'string', enum: ['Y','N'], title: '是否添加唯一索引', required: true, default: 'N'},
    isAutoIncrease:{type: 'string', enum: ['Y','N'], title: '是否自增长', required: true, default: 'N'},
    isNonTableColumn:{type: 'string', enum: ['Y','N'], title: '是否为非数据库字段', required: true, default: 'N'},
    ableAdd:{type: 'string', enum: ['Y','N'], title: '是否可添加', required: true, default: 'Y'},
    ableEdit:{type: 'string', enum: ['Y','N'], title: '是否可修改', required: true, default: 'Y'},
    ableList:{type: 'string', enum: ['Y','N'], title: '是否为列表', required: true, default: 'Y'},
    ableInfo:{type: 'string', enum: ['Y','N'], title: '是否在信息页面显示', required: true, default: 'Y'},
    ableSearch:{type: 'string', enum: ['Y','N'], title: '是否可查询', required: true, default: 'Y'},
    ableSort:{type: 'string', enum: ['Y','N'], title: '是否可排序', required: true, default: 'Y'},
    htmlWidth:{type: 'string', title: '页面字段宽度'},
    htmlWordbreak:{type: 'string', enum: ['Y','N'], title: '是否可换行', required: true, default: 'Y'},
    dataUrl:{type: 'string', title: '数据来源URL'},
    dataJson:{type: 'string', title: 'JSON数据'},
    typeAll:{type: 'string', enum: ['text','textarea','password','radio','checkbox','switch','select','date','time','cascader','number','rate','file','modal','label','icon'], title: '默认类型', required: true, default: 'text'},
    typeAdd:{type: 'string', enum: ['text','textarea','password','radio','checkbox','switch','select','date','time','cascader','number','rate','file','modal','label','icon'], title: '添加页面上字段显示的类型', required: true, default: 'text'},
    typeEdit:{type: 'string', enum: ['text','textarea','password','radio','checkbox','switch','select','date','time','cascader','number','rate','file','modal','label','icon'], title: '修改页面上字段显示的类型', required: true, default: 'text'},
    typeGrid:{type: 'string', enum: ['text','textarea','password','radio','checkbox','switch','select','date','time','cascader','number','rate','file','modal','label','icon'], title: '列表页面上字段显示的类型', required: true, default: 'text'},
    typeInfo:{type: 'string', enum: ['text','textarea','password','radio','checkbox','switch','select','date','time','cascader','number','rate','file','modal','label','icon'], title: '信息页面上字段显示的类型', required: true, default: 'text'},
    typeSearch:{type: 'string', enum: ['text','textarea','password','radio','checkbox','switch','select','date','time','cascader','number','rate','file','modal','label','icon'], title: '搜索页面上字段显示的类型', required: true, default: 'text'},
    refTable:{type: 'string', pattern: '^[a-zA-Z]*[a-zA-Z0-9_]*$', description: '^[a-zA-Z]*[a-zA-Z0-9_]*$', title: '对应的外键表'},
    refField:{type: 'string', pattern: '^[a-zA-Z]*[a-zA-Z0-9_]*$', description: '^[a-zA-Z]*[a-zA-Z0-9_]*$', title: '对应的外键表的字段'},
    refLabelField:{type: 'string', pattern: '^[a-zA-Z]*[a-zA-Z0-9_]*$', description: '^[a-zA-Z]*[a-zA-Z0-9_]*$', title: '外键表显示字段'},
    midTable:{type: 'string', pattern: '^[a-zA-Z]*[a-zA-Z0-9_]*$', description: '^[a-zA-Z]*[a-zA-Z0-9_]*$', title: '对应的中间表'},
    midMainField:{type: 'string', pattern: '^[a-zA-Z]*[a-zA-Z0-9_]*$', description: '^[a-zA-Z]*[a-zA-Z0-9_]*$', title: '当前表与中间表的引用字段'},
    midSecondField:{type: 'string', pattern: '^[a-zA-Z]*[a-zA-Z0-9_]*$', description: '^[a-zA-Z]*[a-zA-Z0-9_]*$', title: '中间表对应的字段'},
    uniqueFields:{type: 'array', items:{type: 'string'}, title: '唯一字段'},
    ruleType:{type: 'string', title: '字段类型'},
    ruleRequired:{type: 'string', enum: ['Y','N'], title: '是否必须'},
    ruleRequiredMsg:{type: 'string', title: '是否必须的错误信息'},
    ruleRegex:{type: 'string', title: '正则检查'},
    ruleRegexMsg:{type: 'string', title: '正则检查的错误信息'},
    ruleRangeMin:{type: 'number', title: '最小值'},
    ruleRangeMax:{type: 'number', title: '最大值'},
    ruleRangeMsg:{type: 'string', title: '范围的错误信息'},
    ruleLength:{type: 'integer', title: '长度'},
    ruleLengthMsg:{type: 'string', title: '长度错误信息'},
    ruleEnums:{type: 'string', title: '枚举'},
    ruleEnumsMsg:{type: 'string', title: '枚举错误信息'},
    ruleNotBlank:{type: 'string', enum: ['Y','N'], title: '是否非空'},
    ruleNotBlankMsg:{type: 'string', title: '是否非空错误信息'},
    exJsonData:{type: 'string', title: '扩展JSON'}
};

var jsonSchema = {
    name:{type: 'string', pattern: '^[A-Z][a-zA-Z0-9_]*$', description: '大写开头，符合Java类命名：^[A-Z][a-zA-Z0-9_]*$', title: '模型的名称', required: true},
    tableName:{type: 'string', pattern: '^[A-Z0-9_]*$', description: '^[A-Z0-9_]*$', title: '模型的表名称', required: true},
    alias:{type: 'string', title: '模型的中文名称'},
    fields:{type: 'array', items: {type: 'object', title: '字段', properties: fieldSchema}}
};

var formSchema = [
    {key: 'name', onChange: capitalizeByEvent},
    {key: 'tableName', onChange: upperCaseByEvent},
    {key: 'alias'},
    {key: 'fields', type: 'tabarray', items: {
        type: 'section',
        legend: "{{value}}",
        items: [
            {type: 'fieldset', title: 'Fields', expandable: true, items: [
                {key: 'fields[].fieldName', valueInLegend: true, onChange:uncapitalizeByEvent},'fields[].fieldAlias',{key: 'fields[].fieldColumn', onChange:upperCaseByEvent},
                {key: 'fields[].fieldColType', type: 'radios'},
                {key: 'fields[].fieldJavaType', type: 'radios'},
                'fields[].fieldLength','fields[].fieldScale','fields[].fieldValue','fields[].fieldDesc','fields[].fieldSeq',
                {key: 'fields[].isNull', type: 'radios'},
                {key: 'fields[].isPk', type: 'radios'},
                {key: 'fields[].isIndex', type: 'radios'},
                {key: 'fields[].isUnique', type: 'radios'},
                {key: 'fields[].isAutoIncrease', type: 'radios'},
                {key: 'fields[].isNonTableColumn', type: 'radios'}
            ]},
            {type: 'fieldset', title: 'Form action', expandable: true, items: [
                {key: 'fields[].ableAdd', type: 'radios'},{key: 'fields[].ableEdit', type: 'radios'},{key: 'fields[].ableList', type: 'radios'},
                {key: 'fields[].ableInfo', type: 'radios'},{key: 'fields[].ableSearch', type: 'radios'},{key: 'fields[].ableSort', type: 'radios'}
             ]},
            {type: 'fieldset', title: 'Html & Data Setting', expandable: true, items: [
                'fields[].htmlWidth', {key: 'fields[].htmlWordbreak', type: 'radios'}, 'fields[].dataUrl', 'fields[].dataJson'
            ]},
            {type: 'fieldset', title: 'Form field type', expandable: true, items: [
                {key: 'fields[].typeAll', type: 'radios'},{key: 'fields[].typeAdd', type: 'radios'},{key: 'fields[].typeEdit', type: 'radios'},
                {key: 'fields[].typeGrid', type: 'radios'},{key: 'fields[].typeInfo', type: 'radios'},{key: 'fields[].typeSearch', type: 'radios'}
            ]},
            {type: 'fieldset', title: 'Reference & Unique', expandable: true, items: [
                'fields[].refTable', 'fields[].refLabelField', 'fields[].midTable', 'fields[].midMainField', 'fields[].midSecondField', 'fields[].uniqueFields'
            ]},
            {type: 'fieldset', title: 'Validate', expandable: true, items: [
                'fields[].ruleType', {key: 'fields[].ruleRequired', type: 'radios'}, 'fields[].ruleRequiredMsg', 'fields[].ruleRegex', 'fields[].ruleRegexMsg',
                'fields[].ruleRangeMin', 'fields[].ruleRangeMax', 'fields[].ruleRangeMsg', 'fields[].ruleLength', 'fields[].ruleLengthMsg',
                'fields[].ruleEnums', 'fields[].ruleEnumsMsg', {key: 'fields[].ruleNotBlank', type: 'radios'}, 'fields[].ruleNotBlankMsg'
            ]},
            'fields[].exJsonData'
        ]
    }},
    {type: 'submit', title: 'OK Go - This Too Shall Pass'}
];

var defaultValue={
    name: 'ModelTest',
    tableName: 'TABLE_TEST',
    alias:'测试表',
    fields:[
        {fieldName:'id', fieldAlias:'主键', fieldColumn:'ID', fieldColType:'VARCHAR', fieldJavaType: 'String', fieldLength:40, fieldDesc:'主键', fieldSeq:1,
            isNull:'N', isPk:'Y', isIndex:'N', isUnique:'N', isAutoIncrease:'N', isNonTableColumn:'N',
            ableAdd:'N', ableEdit:'N', ableList:'N', ableInfo:'Y', ableSearch:'N', ableSort:'N',
            htmlWordbreak:'Y', typeAll:'text', typeAdd:'text', typeEdit:'text', typeGrid:'text', typeInfo:'text', typeSearch:'text',},
        {fieldName:'createTime', fieldAlias:'创建时间', fieldColumn:'CREATE_TIME', fieldColType:'VARCHAR', fieldJavaType:'String', fieldValue:'1970-01-01 00:00:00', fieldDesc:'创建时间', fieldSeq:1000,
            isNull:'N', isPk:'N', isIndex:'N', isUnique:'N', isAutoIncrease:'N', isNonTableColumn:'N',
            ableAdd:'N', ableEdit:'N', ableList:'Y', ableInfo:'Y', ableSearch:'N', ableSort:'N',
            htmlWordbreak:'Y', typeAll:'text', typeAdd:'text', typeEdit:'text', typeGrid:'text', typeInfo:'text', typeSearch:'date',},
        {fieldName:'updateTime', fieldAlias:'更新时间', fieldColumn:'UPDATE_TIME', fieldColType:'DATETIME', fieldJavaType:'Date', fieldValue:'1970-01-01 00:00:00', fieldDesc:'更新时间', fieldSeq:1100,
            isNull:'N', isPk:'N', isIndex:'N', isUnique:'N', isAutoIncrease:'N', isNonTableColumn:'N',
            ableAdd:'N', ableEdit:'N', ableList:'Y', ableInfo:'Y', ableSearch:'N', ableSort:'N',
            htmlWordbreak:'Y', typeAll:'text', typeAdd:'text', typeEdit:'text', typeGrid:'text', typeInfo:'text', typeSearch:'date',},
    ]
};
