<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >

<mapper namespace="${map.packPath!''}.${map.classPri!''}Dao">
    <resultMap id="${map.classPri!''}Result" type="${map.packPath!''}.${map.classPri!''}">
        <#list map.colList as col>
        <result column="${col.colName!''}" property="${col.colObjectName!''}"/>
        </#list>
    </resultMap>
    <insert id="addModel" keyProperty="${map.keyCol.colObjectName!''}" useGeneratedKeys="<#if (map.keyCol.aiFlag==1)>true<#else>false</#if>" parameterType="${map.packPath!''}.${map.classPri!''}">
        insert into ${map.tableName!''} (<@compress single_line=true>
        <#assign colstart=0/>
        <#list map.colList as col>
            <#if (map.keyCol.colName!=col.colName || map.keyCol.aiFlag!=1)>
                <#if (colstart==1)>,</#if>${col.colName}<#assign colstart=1/>
            </#if>
        </#list>
        )</@compress>
        values (<@compress single_line=true>
        <#assign colstart=0/>
        <#list map.colList as col>
            <#if (map.keyCol.colName!=col.colName || map.keyCol.aiFlag!=1)>
                <#if (colstart==1)>,</#if><#noparse>#{</#noparse>${col.colObjectName}<#noparse>}</#noparse><#assign colstart=1/>
            </#if>
        </#list>
        )</@compress>
    </insert>
    <select id="findModelByKey" resultMap="${map.classPri!''}Result" parameterType="java.lang.${map.keyCol.dataType!''}">
        select * from ${map.tableName!''} where ${map.keyCol.colName}=<#noparse>#{</#noparse>${map.keyCol.colObjectName}<#noparse>}</#noparse>
    </select>
    <delete id="deleteModel" parameterType="java.lang.${map.keyCol.dataType!''}">
        delete from ${map.tableName!''} where ${map.keyCol.colName}=<#noparse>#{</#noparse>${map.keyCol.colObjectName}<#noparse>}</#noparse>
    </delete>
    <update id="updateModel" parameterType="${map.packPath!''}.${map.classPri!''}">
        update ${map.tableName!''}
        <set>
        <#assign colstart=0/>
        <#list map.colList as col>
            <#if (map.keyCol.colName!=col.colName || map.keyCol.aiFlag!=1)>
        <if test="${col.colObjectName}!= null"><![CDATA[${col.colName}=<#noparse>#{</#noparse>${col.colObjectName}<#noparse>}</#noparse>,]]></if>
            </#if>
        </#list>
        </set>
        where ${map.keyCol.colName}=<#noparse>#{</#noparse>${map.keyCol.colObjectName}<#noparse>}</#noparse>
    </update>
    <sql id="find${map.classPri!''}ListSql">
        select * from ${map.tableName!''} t
        <where>
            <#list map.colList as col>
            <if test="${col.colObjectName}!= null"><![CDATA[and t.${col.colName} = <#noparse>#{</#noparse>${col.colObjectName}<#noparse>}</#noparse>]]></if>
            </#list>
        </where>
        <if test="sort!=null">order by t.<#noparse>${</#noparse>sort<#noparse>}</#noparse> <#noparse>${</#noparse>dir<#noparse>}</#noparse></if>
    </sql>
    <select id="findModelList" parameterType="${map.packPath!''}.${map.classPri!''}" resultMap="${map.classPri!''}Result">
        <include refid="find${map.classPri!''}ListSql"/>
    </select>
    <select id="findModelCount"  parameterType="${map.packPath!''}.${map.classPri!''}" resultType="java.lang.Integer">
        SELECT count(1) PG_TOTALCOUNT FROM (
        <include refid="find${map.classPri!''}ListSql" />
        <if test="start!= null">  limit <#noparse>#{</#noparse>start<#noparse>}</#noparse>,<#noparse>#{</#noparse>limit<#noparse>}</#noparse> </if>
        ) m
    </select>
    <select id="findModelWithPg" parameterType="${map.packPath!''}.${map.classPri!''}" resultMap="${map.classPri!''}Result">
        <include refid="find${map.classPri!''}ListSql" />
        <if test="start!= null">  limit <#noparse>#{</#noparse>start<#noparse>}</#noparse>,<#noparse>#{</#noparse>limit<#noparse>}</#noparse> </if>
    </select>
</mapper>