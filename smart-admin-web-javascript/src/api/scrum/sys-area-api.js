/**
 * 地区表（省/市/区三级联动） api 封装
 *
 * @Author:    oyt
 * @Date:      2025-10-11 13:54:05
 * @Copyright  oyt
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const sysAreaApi = {

  /**
   * 分页查询  @author  oyt
   */
  queryPage : (param) => {
    return postRequest('/sysArea/queryPage', param);
  },

  /**
   * 顶级区域列表
   */
  listTop : () => {
    return getRequest('/sysArea/listTop');
  },

  /**
   * 按父级查询子区域
   */
  listChildren : (pid) => {
    return getRequest('/sysArea/listChildren', { pid });
  },

  /**
   * 增加  @author  oyt
   */
  add: (param) => {
      return postRequest('/sysArea/add', param);
  },

  /**
   * 修改  @author  oyt
   */
  update: (param) => {
      return postRequest('/sysArea/update', param);
  },


  /**
   * 删除  @author  oyt
   */
  delete: (id) => {
      return getRequest(`/sysArea/delete/${id}`);
  },

  /**
   * 批量删除  @author  oyt
   */
  batchDelete: (idList) => {
      return postRequest('/sysArea/batchDelete', idList);
  },

};
