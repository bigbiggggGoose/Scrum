/**
 * 角色管理 api 封装
 *
 * @Author:    oyt
 * @Date:      2025-10-11 18:33:10
 * @Copyright  oyt
 */
import { postRequest, getRequest } from '/@/lib/axios';

export const sysRoleApi = {

  /**
   * 分页查询  @author  oyt
   */
  queryPage : (param) => {
    return postRequest('/sysRole/queryPage', param);
  },

  /**
   * 查询全部（用于左侧列表/树）
   */
  listAll: () => {
    return getRequest('/sysRole/listAll');
  },

  /**
   * 查询详情
   */
  get: (id) => {
    return getRequest(`/sysRole/get/${id}`);
  },

  /**
   * 增加  @author  oyt
   */
  add: (param) => {
      return postRequest('/sysRole/add', param);
  },

  /**
   * 修改  @author  oyt
   */
  update: (param) => {
      return postRequest('/sysRole/update', param);
  },


  /**
   * 删除  @author  oyt
   */
  delete: (id) => {
      return getRequest(`/sysRole/delete/${id}`);
  },

  /**
   * 批量删除  @author  oyt
   */
  batchDelete: (idList) => {
      return postRequest('/sysRole/batchDelete', idList);
  },

};
