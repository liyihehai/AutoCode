package ${map.packPath!''};
import com.nnte.framework.base.BaseModel;
import com.nnte.framework.annotation.DBPKColum;

<#if (map.existDate??)>import java.util.Date;</#if>
/*
 * 自动代码 请勿更改 <${.now?string["YYYY-MM-dd HH:mm:ss"]}>
 */
public class ${map.classPri!''} extends BaseModel {
<#if (map.colList??)>
    <#list map.colList as col>
    <#if (col.prk=='PRI')>@DBPKColum </#if>private ${col.dataType!''} ${col.colObjectName!''};
    </#list>
</#if>

    public ${map.classPri!''}(){}

<#if (map.colList??)>
<#list map.colList as col>
    public ${col.dataType!''}  get${col.colMethodName!''}(){ return ${col.colObjectName!''};}
    public void set${col.colMethodName!''}(${col.dataType!''}  ${col.colObjectName!''}){ this.${col.colObjectName!''} = ${col.colObjectName!''};}
</#list>
</#if>
}