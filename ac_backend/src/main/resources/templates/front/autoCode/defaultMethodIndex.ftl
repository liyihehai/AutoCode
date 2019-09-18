<html>
<head>
    <#include "../taglib.ftl">

</head>
<body>
<div class="row level0">
    <div class="col-xs-12">
        <div class="box">
            <div class="box-body">
                <table style="width: 100%; margin-bottom: 10px;">
                    <tr height="50px">
                        <td style="width: 100px;"><strong>默认方法名：</strong></td>
                        <td><select id="defaultModelName" class="select2 form-control"
                                    style="width: 80%;">
                                <option value="addModel">addModel</option>
                                <option value="findModelByKey">findModelByKey</option>
                                <option value="deleteModel">deleteModel</option>
                                <option value="updateModel">updateModel</option>
                                <option value="findModelList">findModelList</option>
                                <option value="findModelCount">findModelCount</option>
                                <option value="findModelWithPg">findModelWithPg</option>
                            </select>
                        </td>
                        <td>
                            <div class="nav-tabs-custom" style="cursor: move;">
                                <!-- Tabs within a box -->
                                <ul class="nav nav-tabs pull-left ui-sortable-handle"
                                    style="width: 100%;">
                                    <li style="float: left;">
                                        <button class="btn bg-blue"
                                                onclick="addDefaultModel()" type="button">新增
                                        </button>
                                    </li>
                                    <li style="float: left;">
                                        <button type="button"
                                                class="btn bg-maroon" onclick="modifyDefaultModel()">更改
                                        </button>
                                    </li>
                                    <li style="float: left;">
                                        <button type="button"
                                                class="btn bg-maroon" onclick="deleteDefaultModel()">删除
                                        </button>
                                    </li>
                                </ul>
                            </div>
                        </td>
                    </tr>
                </table>
            </div>
            <!-- /.box-body -->
        </div>
        <div class="box">
            <div class="box-body">
                <textarea id="methodMText" rows="25" cols="120">在w3school，你可以找到你所需要的所有的网站建设教程。</textarea>
            </div>
        </div>
    </div>
    <!-- /.col -->
</div>
<!-- /.row -->
</body>
</html>

