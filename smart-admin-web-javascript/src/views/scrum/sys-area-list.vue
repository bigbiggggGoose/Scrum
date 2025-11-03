<!--
  * 地区表（省/市/区三级联动）
  *
  * @Author:    oyt
  * @Date:      2025-10-11 13:54:05
  * @Copyright  oyt
-->
<template>
    <a-row :gutter="16">
        <a-col :span="6">
            <a-card size="small" :bordered="false" :hoverable="true" title="地区树">
                <a-tree
                    :treeData="treeData"
                    :selectedKeys="selectedNodeId ? [selectedNodeId] : []"
                    :fieldNames="{ title: 'title', key: 'key', children: 'children' }"
                    :expandedKeys="expandedKeys"
                    :virtual="false"
                    :showLine="false"
                    :blockNode="true"
                    @select="onSelectNode"
                    @expand="onExpand"
                >
                    <template #switcherIcon="{ isLeaf, expanded }">
                        <span v-if="!isLeaf" class="tree-switcher">
                            <CaretDownOutlined v-if="expanded" />
                            <CaretRightOutlined v-else />
                        </span>
                    </template>
                </a-tree>
            </a-card>
        </a-col>
        <a-col :span="18">
            <a-card size="small" :bordered="false" :hoverable="true" title="子地区">
                <a-row class="smart-table-btn-block">
                    <div class="smart-table-operate-block">
                        <a-button @click="showAddChild" type="primary" size="small">
                            <template #icon>
                                <PlusOutlined />
                            </template>
                            添加
                        </a-button>
                        <a-button @click="confirmBatchDelete" type="primary" danger size="small" :disabled="selectedRowKeyList.length == 0">
                            <template #icon>
                                <DeleteOutlined />
                            </template>
                            批量删除
                        </a-button>
                    </div>
                    <div class="smart-table-setting-block">
                        <TableOperator v-model="columns" :tableId="null" :refresh="queryChildren" />
                    </div>
                </a-row>

                <a-table
                    size="small"
                    :scroll="{ y: 800 }"
                    :dataSource="tableData"
                    :columns="columns"
                    rowKey="id"
                    bordered
                    :loading="tableLoading"
                    :pagination="false"
                    :row-selection="{ selectedRowKeys: selectedRowKeyList, onChange: onSelectChange }"
                >
                    <template #bodyCell="{ text, record, column }">
                        <template v-if="column.dataIndex === 'action'">
                            <div class="smart-table-operate">
                                <a-button @click="showForm(record)" type="link">编辑</a-button>
                                <a-button @click="onDelete(record)" danger type="link">删除</a-button>
                            </div>
                        </template>
                    </template>
                </a-table>
            </a-card>
        </a-col>
    </a-row>

    <SysAreaForm  ref="formRef" @reloadList="queryChildren"/>
</template>
<script setup>
    import { reactive, ref, onMounted } from 'vue';
    import { message, Modal } from 'ant-design-vue';
    import { SmartLoading } from '/@/components/framework/smart-loading';
    import { sysAreaApi } from '/@/api/scrum/sys-area-api';
    import { PAGE_SIZE_OPTIONS } from '/@/constants/common-const';
    import { smartSentry } from '/@/lib/smart-sentry';
    import TableOperator from '/@/components/support/table-operator/index.vue';
    import SysAreaForm from './sys-area-form.vue';
    import { CaretRightOutlined, CaretDownOutlined } from '@ant-design/icons-vue';

    // ---------------------------- 表格列 ----------------------------

    const columns = ref([
        {
            title: '区域编号',
            dataIndex: 'id',
            ellipsis: false,
            width: 120,
        },
        {
            title: '区域名称',
            dataIndex: 'name',
            ellipsis: false,
        },
        {
            title: '区域上级标识',
            dataIndex: 'pid',
            ellipsis: false,
        },
        {
            title: '地名简称',
            dataIndex: 'simplename',
            ellipsis: false,
        },
        {
            title: '区域等级',
            dataIndex: 'level',
            ellipsis: false,
        },
        {
            title: '城市编码',
            dataIndex: 'citycode',
            ellipsis: false,
        },
        {
            title: '邮政编码',
            dataIndex: 'zipcode',
            ellipsis: false,
        },
        {
            title: '组合名称',
            dataIndex: 'mername',
            ellipsis: false,
        },
        {
            title: '经度',
            dataIndex: 'lng',
            ellipsis: false,
        },
        {
            title: '纬度',
            dataIndex: 'lat',
            ellipsis: false,
        },
        {
            title: '区域拼音',
            dataIndex: 'pinyin',
            ellipsis: false,
        },
        {
            title: '操作',
            dataIndex: 'action',
            fixed: 'right',
            width: 90,
        },
    ]);

    // ---------------------------- 查询数据表单和方法 ----------------------------

    // 顶级区域列表与当前选择
    const topAreaList = ref([]);
    const treeData = ref([]);
    const selectedNodeId = ref();
    const expandedKeys = ref([]);

    // 表格加载loading
    const tableLoading = ref(false);
    // 表格数据
    const tableData = ref([]);
    // 加载顶级区域
    async function loadTopAreas() {
        try {
            const res = await sysAreaApi.listTop();
            topAreaList.value = res.data || [];

            // 一次性构树：为每个顶级区域递归拉取其子节点
            const nodes = [];
            for (const item of topAreaList.value) {
                const node = await buildNodeFromArea(item);
                nodes.push(node);
            }
            treeData.value = nodes;
            // 默认展开首个顶级节点，避免“点击错位”的感知
            if (nodes.length > 0) {
                expandedKeys.value = [nodes[0].key];
            }

            if (treeData.value.length > 0) {
                selectedNodeId.value = treeData.value[0].key;
                await queryChildren();
            } else {
                tableData.value = [];
            }
        } catch (e) {
            smartSentry.captureError(e);
        }
    }

    // 切换选择的顶级区域
    async function onSelectNode(selectedKeys, info) {
        if (selectedKeys && selectedKeys.length > 0) {
            selectedNodeId.value = String(selectedKeys[0]);
            await queryChildren();
        }
    }

    // 控制展开：使用组件返回的 expandedKeys 保持与界面一致
    function onExpand(keys) {
        expandedKeys.value = keys.map((k) => String(k));
    }

    // ---------------------------- 非懒加载：递归构树 ----------------------------
    function nodeFromArea(area) {
        return {
            title: area.name,
            key: String(area.id),
            isLeaf: area.level === 3,
            level: area.level,
            dataRef: { title: area.name, key: String(area.id), level: area.level },
        };
    }

    async function buildNodeFromArea(area) {
        const node = nodeFromArea(area);
        node.isLeaf = false;
        node.children = await resolveChildrenForNode(String(area.id), area.name);
        return node;
    }

    async function resolveChildrenForNode(pid, parentTitle) {
        try {
            const res = await sysAreaApi.listChildren(pid);
            let list = res.data || [];

            // 顶级直辖市无二级数据，走 cityId 回退并合成市级节点
            if ((!list || list.length === 0) && pid.length === 6 && pid.endsWith('0000')) {
                const cityId = pid.substring(0, 2) + '0100';
                const res2 = await sysAreaApi.listChildren(cityId);
                const districts = res2.data || [];
                if (districts && districts.length > 0) {
                    const cityTitle = '市辖区';
                    const cityNode = {
                        title: cityTitle,
                        key: cityId,
                        isLeaf: false,
                        level: 2,
                        dataRef: { title: cityTitle, key: cityId, level: 2 },
                        children: districts.map((d) => {
                            const n = nodeFromArea(d);
                            n.isLeaf = true;
                            n.level = 3;
                            n.dataRef.level = 3;
                            return n;
                        }),
                    };
                    return [cityNode];
                }
                return [];
            }

            // 若只有一个“市辖区”节点，直接填充其三级子节点
            if (list && list.length === 1 && /市辖区/.test(list[0].name)) {
                const res3 = await sysAreaApi.listChildren(String(list[0].id));
                const third = res3.data || [];
                const cityNode = nodeFromArea(list[0]);
                cityNode.isLeaf = false;
                cityNode.children = third.map((d) => {
                    const n = nodeFromArea(d);
                    n.isLeaf = true;
                    return n;
                });
                return [cityNode];
            }

            // 常规情况：递归构建至三级
            const childrenNodes = [];
            for (const item of list) {
                const childNode = nodeFromArea(item);
                if (item.level < 3) {
                    childNode.isLeaf = false;
                    childNode.children = await resolveChildrenForNode(String(item.id), item.name);
                } else {
                    childNode.isLeaf = true;
                }
                childrenNodes.push(childNode);
            }
            return childrenNodes;
        } catch (e) {
            smartSentry.captureError(e);
            return [];
        }
    }

    // 查询子区域
    async function queryChildren() {
        tableLoading.value = true;
        try {
            // 1) 先按当前选择的节点ID查询直接子节点
            const res = await sysAreaApi.listChildren(selectedNodeId.value);
            let data = res.data || [];

            // 2) 如果没有直接子节点，针对直辖市编码做一次回退：
            //    顶级：110000/120000/310000/500000 -> 二级：110100/120100/310100/500100
            if (!data || data.length === 0) {
                const id = selectedNodeId.value;
                if (id && id.length === 6 && id.endsWith('0000')) {
                    const candidateId = id.substring(0, 2) + '0100';
                    // 避免重复调用同一个ID
                    if (candidateId !== id) {
                        const res2 = await sysAreaApi.listChildren(candidateId);
                        const data2 = res2.data || [];
                        if (data2 && data2.length > 0) {
                            data = data2;
                        }
                    }
                }
            }

            // 3) 如果只返回了一个“市辖区”节点（如110100/310100），则直接显示其下三级子区，让列表更直观
            if (data && data.length === 1 && /市辖区/.test(data[0].name) && data[0].id && String(data[0].id).length === 6) {
                const res3 = await sysAreaApi.listChildren(String(data[0].id));
                const data3 = res3.data || [];
                if (data3 && data3.length > 0) {
                    data = data3;
                }
            }

            tableData.value = data;
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            tableLoading.value = false;
        }
    }

    onMounted(loadTopAreas);

    // ---------------------------- 添加/修改 ----------------------------
    const formRef = ref();

    function showForm(data) {
        formRef.value.show(data);
    }

    function showAddChild() {
        formRef.value.show({ pid: selectedNodeId.value });
    }

    // ---------------------------- 单个删除 ----------------------------
    //确认删除
    function onDelete(data){
        Modal.confirm({
            title: '提示',
            content: '确定要删除选吗?',
            okText: '删除',
            okType: 'danger',
            onOk() {
                requestDelete(data);
            },
            cancelText: '取消',
            onCancel() {},
        });
    }

    //请求删除
    async function requestDelete(data){
        SmartLoading.show();
        try {
            let deleteForm = {
                goodsIdList: selectedRowKeyList.value,
            };
            await sysAreaApi.delete(data.id);
            message.success('删除成功');
            queryChildren();
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            SmartLoading.hide();
        }
    }

    // ---------------------------- 批量删除 ----------------------------

    // 选择表格行
    const selectedRowKeyList = ref([]);

    function onSelectChange(selectedRowKeys) {
        selectedRowKeyList.value = selectedRowKeys;
    }

    // 批量删除
    function confirmBatchDelete() {
        Modal.confirm({
            title: '提示',
            content: '确定要批量删除这些数据吗?',
            okText: '删除',
            okType: 'danger',
            onOk() {
                requestBatchDelete();
            },
            cancelText: '取消',
            onCancel() {},
        });
    }

    //请求批量删除
    async function requestBatchDelete() {
        try {
            SmartLoading.show();
            await sysAreaApi.batchDelete(selectedRowKeyList.value);
            message.success('删除成功');
            queryChildren();
        } catch (e) {
            smartSentry.captureError(e);
        } finally {
            SmartLoading.hide();
        }
    }
</script>

<style scoped>
.tree-switcher {
    color: #1890ff;
    font-size: 14px;
    display: inline-flex;
    align-items: center;
}

/* 统一树节点的点击高度与行高，避免命中偏移 */
:deep(.ant-tree-node-content-wrapper),
:deep(.ant-tree-switcher),
:deep(.ant-tree-title) {
    height: 24px;
    line-height: 24px;
}
:deep(.ant-tree-switcher) { width: 16px; display: inline-flex; align-items: center; }
:deep(.ant-tree-indent-unit) { width: 16px; }
:deep(.ant-tree-treenode) {
    margin: 2px 0;
}
</style>
